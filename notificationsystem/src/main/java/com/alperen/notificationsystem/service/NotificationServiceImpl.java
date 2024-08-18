package com.alperen.notificationsystem.service;

import com.alperen.notificationsystem.configuration.RabbitMqConfig;
import com.alperen.notificationsystem.entity.Notification;
import com.alperen.notificationsystem.entity.patientEntity.Patient;
import com.alperen.notificationsystem.repository.INotificationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class NotificationServiceImpl implements INotificationService {

    private final INotificationRepository notificationRepository;
    private ObjectMapper objectMapper;
    @Autowired
    public NotificationServiceImpl(INotificationRepository notificationRepository, ObjectMapper objectMapper) {
        this.notificationRepository = notificationRepository;
        this.objectMapper = objectMapper;
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

    @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
    public void setNotificationToPatient(Message message){
        System.out.println("Yeni bir hasta kaydoldu notification systemden gorduk... ********************");
        System.out.println("Message type: " + message.getClass().getName());
        try {
            // Mesajın gövdesini String olarak çıkarma
            String messageBody = new String(message.getBody());

            // JSON'dan Patient nesnesine dönüştürme
            Patient patient = objectMapper.readValue(messageBody, Patient.class);

            // Mesaj başarıyla alındı ve dönüştürüldü
            System.out.println("Yeni bir hasta kaydoldu: " + patient.getFirstName());
            System.out.println("----- patient-----");
            System.out.println(patient);
        } catch (Exception e) {
            // Hata durumunda
            System.err.println("Mesaj dönüştürme hatası: " + e.getMessage());
        }


    }
}

