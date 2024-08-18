package com.alperen.notificationsystem.service;

import com.alperen.notificationsystem.configuration.RabbitMqConfig;
import com.alperen.notificationsystem.entity.Notification;
import com.alperen.notificationsystem.entity.TargetPatient;
import com.alperen.notificationsystem.entity.patientEntity.Patient;
import com.alperen.notificationsystem.repository.INotificationRepository;
import com.alperen.notificationsystem.repository.ITargetPatientRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NotificationServiceImpl implements INotificationService {

    private final INotificationRepository notificationRepository;
    private final ITargetPatientRepository targetPatientRepository;
    private final RestTemplate restTemplate;
    @Value("${patient.api.url}")
    private String patientApiUrl;

    private ObjectMapper objectMapper;
    @Autowired
    public NotificationServiceImpl(INotificationRepository notificationRepository, ObjectMapper objectMapper, ITargetPatientRepository targetPatientRepository,
                                   RestTemplate restTemplate) {
        this.notificationRepository = notificationRepository;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.targetPatientRepository = targetPatientRepository;
    }

    @Override
    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    @Override
    public Notification findById(int id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
    }

    @Override
    public Notification save(Notification notification) {
        // make a request based on criteria to patient it will get a list of patient
        // get all patient
        // save them in target patient table

//        String url = String.format("%s/listAgeBetweenAndGender/%d/%d/%s", patientApiUrl,
//                notification.getNotificationCriteria().getStartAge(), notification.getNotificationCriteria().getEndAge(),
//                notification.getNotificationCriteria().getGender());
        //restTemplate.
        String url = String.format("%s/%d/%d/%c", patientApiUrl, notification.getNotificationCriteria().getStartAge(),
                notification.getNotificationCriteria().getEndAge(), notification.getNotificationCriteria().getGender());
        restTemplate.getForObject(url, Void.class); // We don't need the response

        return notificationRepository.save(notification);
    }

    @Override
    public void deleteById(int id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public Notification update(int id, Notification updatedNotification) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));

        notification.setNotificationCriteria(updatedNotification.getNotificationCriteria());
        notification.setNotificationMessage(updatedNotification.getNotificationMessage());

        return notificationRepository.save(notification);
    }

    @RabbitListener(queues = RabbitMqConfig.SECOND_QUEUE_NAME)
    public void setNotificationToPatientList(Message message){
        System.out.println("liste alındı! -------------*-**************************");
        List<Patient> patients = new ArrayList<>();
        try {
            String messageBody = new String(message.getBody());
            // Deserialize the message body into a List of Patient
            patients = objectMapper.readValue(messageBody, objectMapper.getTypeFactory().constructCollectionType(List.class, Patient.class));
            System.out.println("Received patients: " + patients);
        } catch (Exception e) {
            System.err.println("Message conversion error: " + e.getMessage());
        }

        for(Patient p:patients){
            // add them to target table
            Notification notification = notificationRepository.find
            TargetPatient targetPatient = new TargetPatient();
            targetPatient.setPatientId(p.getId());
            targetPatient.setPrimaryPhone(p.getPhoneNumbers().get(0).getPhoneNumber());
            targetPatient.setPrimaryMail(p.getEmailAddresses().get(0).getEmailAddress());
            targetPatient.setNotificationId();
            targetPatientRepository.save(targetPatient);
        }
    }

    @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
    public void setNotificationToPatient(Message message){
        Patient patient = null;
        try {
            String messageBody = new String(message.getBody());
            patient = objectMapper.readValue(messageBody, Patient.class);
            System.out.println(patient);
        } catch (Exception e) {
            System.err.println("Message conversion error " + e.getMessage());
        }
        if (patient != null){
            // check new patient's age and gender criteria if there is a notification fit it
            // bind them
            Date birthDate = patient.getDateOfBirth();
            LocalDate birthLocalDate = birthDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate today = LocalDate.now();
            int age = Period.between(birthLocalDate, today).getYears();

            System.out.println("age = " + age);
            List<Notification> notifications = findAll();
            for(Notification n:notifications){
                if (age >= n.getNotificationCriteria().getStartAge() && age <= n.getNotificationCriteria().getEndAge()
                        && n.getNotificationCriteria().getGender() == patient.getGender()){
                    TargetPatient targetPatient = new TargetPatient();
                    targetPatient.setPatientId(patient.getId());
                    targetPatient.setNotificationId(n.getId());
                    if(patient.isEmailActive()) targetPatient.setPrimaryMail(patient.getEmailAddresses().get(0).getEmailAddress());
                    if(patient.isSmsActive()) targetPatient.setPrimaryPhone(patient.getPhoneNumbers().get(0).getPhoneNumber());

                    targetPatientRepository.save(targetPatient);
                }
            }
        }
    }
}

