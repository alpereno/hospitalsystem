package com.alperen.notificationsystem.controller;

import com.alperen.notificationsystem.entity.Notification;
import com.alperen.notificationsystem.service.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private INotificationService notificationService;

    @Autowired
    public NotificationController(INotificationService notificationService){
        this.notificationService = notificationService;
    }

    @PostMapping("/addNotification")
    public ResponseEntity<Notification> save(@RequestBody Notification notification){
        return new ResponseEntity<>(notificationService.save(notification), HttpStatus.CREATED);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Notification>> getAllPatient(){
        return new ResponseEntity<>(notificationService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Notification> searchById(@PathVariable("id") int id){
        return new ResponseEntity<>(notificationService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable("id")int id){
        notificationService.deleteById(id);
    }
}
