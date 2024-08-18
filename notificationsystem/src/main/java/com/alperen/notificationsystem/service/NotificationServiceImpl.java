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
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class NotificationServiceImpl implements INotificationService {

    private final INotificationRepository notificationRepository;
    private final ITargetPatientRepository targetPatientRepository;
    private final RestTemplate restTemplate;
    @Value("${patient.api.url}")
    private String patientApiUrl;

    private final AmqpTemplate rabbitTemplate;

    private final DirectExchange secondExchange;
    @Value("${rabbitmq.second.routing.key}")
    private String secondRoutingKey;

    private ObjectMapper objectMapper;
    @Autowired
    public NotificationServiceImpl(INotificationRepository notificationRepository, ObjectMapper objectMapper, ITargetPatientRepository targetPatientRepository,
                                   RestTemplate restTemplate, DirectExchange exchange, AmqpTemplate rabbitTemplate) {
        this.notificationRepository = notificationRepository;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.targetPatientRepository = targetPatientRepository;
        this.secondExchange = exchange;
        this.rabbitTemplate = rabbitTemplate;
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
        Notification newNotification = notificationRepository.save(notification);
        rabbitTemplate.convertAndSend(secondExchange.getName(), secondRoutingKey, newNotification);
        return newNotification;
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
    public void setNotificationToPatientList(Message message) throws JsonProcessingException {

        String messageBody = new String(message.getBody());
        Notification notification = objectMapper.readValue(messageBody, Notification.class );

        String url = String.format("%s/%d/%d/%c", patientApiUrl, notification.getNotificationCriteria().getStartAge(),
                notification.getNotificationCriteria().getEndAge(), notification.getNotificationCriteria().getGender());

        List<Patient> patients = restTemplate.exchange(url, HttpMethod.GET,null, new ParameterizedTypeReference<List<Patient>>() {}).getBody();
        System.out.println("list len" + patients.size());
        for(Patient p : patients){
            TargetPatient targetPatient = new TargetPatient();
            targetPatient.setPatientId(p.getId());
            targetPatient.setNotificationId(notification.getId());
            if(p.getEmailAddresses() != null && p.getPhoneNumbers() != null && !p.getPhoneNumbers().isEmpty() && !p.getEmailAddresses().isEmpty()){
                targetPatient.setPrimaryMail(p.getEmailAddresses().get(0).getEmailAddress());
                targetPatient.setPrimaryPhone(p.getPhoneNumbers().get(0).getPhoneNumber());
            }
            targetPatientRepository.save(targetPatient);
        }
    }

    @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
    public void setNotificationToPatient(Patient patient){
        //Patient patient = null;
       // System.out.println(message.getClass());
//        try {
//            String messageBody = new String(message.getBody());
//            patient = objectMapper.readValue(messageBody, Patient.class);
//            System.out.println(patient);
//        } catch (Exception e) {
//            System.err.println("Message conversion error " + e.getMessage());
//        }
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

