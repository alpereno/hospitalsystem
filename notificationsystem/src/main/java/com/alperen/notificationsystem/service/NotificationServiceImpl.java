package com.alperen.notificationsystem.service;

import com.alperen.notificationsystem.configuration.RabbitMqConfig;
import com.alperen.notificationsystem.entity.Notification;
import com.alperen.notificationsystem.entity.NotificationCriteria;
import com.alperen.notificationsystem.entity.TargetPatient;
import com.alperen.notificationsystem.entity.patientEntity.Patient;
import com.alperen.notificationsystem.exception.InappropriateRequestException;
import com.alperen.notificationsystem.repository.INotificationRepository;
import com.alperen.notificationsystem.repository.ITargetPatientRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    public Notification save(Notification notification) throws InappropriateRequestException {
        if (notification.getNotificationCriteria() != null & notification.getNotificationMessage() != null){
            if (notification.getNotificationCriteria().getStartAge() == notification.getNotificationCriteria().getEndAge())
                throw new InappropriateRequestException("Age range cannot consist of the same numbers");
            Notification newNotification = notificationRepository.save(notification);
            rabbitTemplate.convertAndSend(secondExchange.getName(), secondRoutingKey, newNotification);
            return newNotification;
        }
        throw new InappropriateRequestException("Notification criteria and message cannot be null");
    }

    @Override
    public void deleteById(int id) {
        targetPatientRepository.deleteByNotificationId(id);
        notificationRepository.deleteById(id);
    }

    @Override
    public Notification update(int id, Notification updatedNotification) throws InappropriateRequestException{
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new InappropriateRequestException("Notification not found with id: " + id));

        notification.setNotificationCriteria(updatedNotification.getNotificationCriteria());
        notification.setNotificationMessage(updatedNotification.getNotificationMessage());

        return notificationRepository.save(notification);
    }

    @Override
    public List<TargetPatient> findAllTargetPatient() {
        return targetPatientRepository.findAll();
    }

    @RabbitListener(queues = RabbitMqConfig.SECOND_QUEUE_NAME)
    public void setNotificationToPatientList(Message message) throws JsonProcessingException {
        if (message != null){
            String messageBody = new String(message.getBody());
            Notification notification = objectMapper.readValue(messageBody, Notification.class );

            String url = String.format("%s/%d/%d/%c", patientApiUrl, notification.getNotificationCriteria().getStartAge(),
                    notification.getNotificationCriteria().getEndAge(), notification.getNotificationCriteria().getGender());

            List<Patient> patients = restTemplate.exchange(url, HttpMethod.GET,null, new ParameterizedTypeReference<List<Patient>>() {}).getBody();
            System.out.println("list len" + patients.size());
            for(Patient p : patients){
                boolean willSave = false;
                TargetPatient targetPatient = new TargetPatient();
                if (p.isEmailActive() && p.getEmailAddresses() != null && !p.getEmailAddresses().isEmpty()){
                    willSave = true;
                    targetPatient.setPrimaryMail(p.getEmailAddresses().get(0).getEmailAddress());
                }
                if (p.isSmsActive() && p.getPhoneNumbers() != null && !p.getPhoneNumbers().isEmpty()){
                    willSave = true;
                    targetPatient.setPrimaryPhone(p.getPhoneNumbers().get(0).getPhoneNumber());
                }
                if (willSave){
                    targetPatient.setNotificationId(notification.getId());
                    targetPatient.setPatientId(p.getId());
                    targetPatientRepository.save(targetPatient);
                }
            }
        }
    }

    @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
    public void setNotificationToPatient(Patient patient){
        if (patient != null){
            // check new patient's age and gender criteria if there is a notification fit it
            // bind them
            Date birthDate = patient.getDateOfBirth();
            LocalDate birthLocalDate = birthDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate today = LocalDate.now();
            int age = Period.between(birthLocalDate, today).getYears();


            List<Notification> notifications = findAll();
            for(Notification n:notifications){
                if (age >= n.getNotificationCriteria().getStartAge() && age <= n.getNotificationCriteria().getEndAge()
                        && n.getNotificationCriteria().getGender() == patient.getGender()){
                    boolean willSave = false;
                    TargetPatient targetPatient = new TargetPatient();
                    if (patient.isEmailActive() && patient.getEmailAddresses() != null & !patient.getEmailAddresses().isEmpty()){
                        willSave = true;
                        targetPatient.setPrimaryMail(patient.getEmailAddresses().get(0).getEmailAddress());
                    }
                    if (patient.isSmsActive() && patient.getPhoneNumbers() != null && !patient.getPhoneNumbers().isEmpty()){
                        willSave = true;
                        targetPatient.setPrimaryPhone(patient.getPhoneNumbers().get(0).getPhoneNumber());
                    }
                    if(willSave){
                        targetPatient.setPatientId(patient.getId());
                        targetPatient.setNotificationId(n.getId());
                        targetPatientRepository.save(targetPatient);
                    }
                }
            }
        }
    }
}

