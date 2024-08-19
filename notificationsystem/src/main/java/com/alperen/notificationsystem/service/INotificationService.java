package com.alperen.notificationsystem.service;

import com.alperen.notificationsystem.entity.Notification;
import com.alperen.notificationsystem.entity.TargetPatient;

import java.util.List;

public interface INotificationService {
    public List<Notification> findAll();
    public Notification findById(int id);
    public Notification save(Notification notification);
    public void deleteById(int id);
    public Notification update(int id, Notification notification);

    public List<TargetPatient> findAllTargetPatient();
}
