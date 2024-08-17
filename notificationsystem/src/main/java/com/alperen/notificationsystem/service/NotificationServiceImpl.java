package com.alperen.notificationsystem.service;

import com.alperen.notificationsystem.entity.Notification;
import com.alperen.notificationsystem.repository.INotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements INotificationService {

    private final INotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(INotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
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
}

