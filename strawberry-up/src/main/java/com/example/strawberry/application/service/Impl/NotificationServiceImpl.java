package com.example.strawberry.application.service.Impl;

import com.example.strawberry.application.constants.MessageConstant;
import com.example.strawberry.application.dai.IUserRepository;
import com.example.strawberry.application.dai.NotificationRepository;
import com.example.strawberry.application.service.INotificationService;
import com.example.strawberry.config.exception.NotFoundException;
import com.example.strawberry.domain.dto.NotificationDTO;
import com.example.strawberry.domain.entity.Notification;
import com.example.strawberry.domain.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements INotificationService {

    private final NotificationRepository notificationRepository;
    private final IUserRepository userRepository;
    private final ModelMapper modelMapper;

    public NotificationServiceImpl(NotificationRepository notificationRepository, IUserRepository userRepository, ModelMapper modelMapper) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Notification> getAllNotification(Long idUser) {
        Optional<User> user = userRepository.findById(idUser);
        UserServiceImpl.checkUserExists(user);
        List<Notification> notifications = notificationRepository.findByUserIdUser(idUser);

//        Sắp xếp thông báo mới nhất ở trên
        notifications.sort((l1, l2) -> (l2.getIdNoti()).compareTo(l1.getIdNoti()));
        return notifications;
    }

    @Override
    public Notification createNotification(Long idUser, NotificationDTO notificationDTO) {
        Optional<User> user = userRepository.findById(idUser);
        UserServiceImpl.checkUserExists(user);
        Notification notification = modelMapper.map(notificationDTO, Notification.class);
        notification.setUser(user.get());
        notificationRepository.save(notification);
        return notification;
    }

    @Override
    public Notification markAsRead(Long idNoti) {
        Optional<Notification> notification = notificationRepository.findById(idNoti);
        checkNotificationExists(notification);
        notification.get().setStatus(Boolean.TRUE);
        notificationRepository.save(notification.get());
        return notification.get();
    }

    @Override
    public Notification markAsUnRead(Long idNoti) {
        Optional<Notification> notification = notificationRepository.findById(idNoti);
        checkNotificationExists(notification);
        notification.get().setStatus(Boolean.FALSE);
        notificationRepository.save(notification.get());
        return notification.get();
    }

    @Override
    public String deleteNotification(Long idNoti) {
        Optional<Notification> notification = notificationRepository.findById(idNoti);
        checkNotificationExists(notification);
        notificationRepository.delete(notification.get());

        return "Deleted notification";
    }

    public void checkNotificationExists(Optional<Notification> notification) {
        if (notification.isEmpty()) {
            throw new NotFoundException(MessageConstant.NOTIFICATION_NOT_EXISTS);
        }
    }
}
