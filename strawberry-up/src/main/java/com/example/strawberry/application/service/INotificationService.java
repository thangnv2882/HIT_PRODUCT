package com.example.strawberry.application.service;

import com.example.strawberry.domain.dto.NotificationDTO;
import com.example.strawberry.domain.entity.Notification;

import java.util.List;
import java.util.Set;

public interface INotificationService {

    List<Notification> getAllNotification(Long idUser);
    Notification createNotification(Long idUser, NotificationDTO notificationDTO);
    Notification markAsRead(Long idNoti);
    Notification markAsUnRead(Long idNoti);
    String deleteNotification(Long idNoti);


}
