package com.alperen.notificationsystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private int id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "message_id", nullable = false)
    private NotificationMessage notificationMessage;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "criteria_id", nullable = false)
    private NotificationCriteria notificationCriteria;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public NotificationMessage getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(NotificationMessage notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public NotificationCriteria getNotificationCriteria() {
        return notificationCriteria;
    }

    public void setNotificationCriteria(NotificationCriteria notificationCriteria) {
        this.notificationCriteria = notificationCriteria;
    }
}