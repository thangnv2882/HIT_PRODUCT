package com.example.strawberry.adapter.web.v1.controller;

import com.example.strawberry.adapter.web.base.RestApiV1;
import com.example.strawberry.adapter.web.base.VsResponseUtil;
import com.example.strawberry.application.constants.UrlConstant;
import com.example.strawberry.application.service.INotificationService;
import com.example.strawberry.domain.dto.NotificationDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestApiV1
public class NotificationController {

    private final INotificationService notificationService;

    public NotificationController(INotificationService notificationService) {
        this.notificationService = notificationService;
    }


    @ApiOperation(value = "Xem tất cả thông báo của người dùng.")
    @GetMapping(UrlConstant.Notification.DATA_NOTIFICATION_ALL)
    public ResponseEntity<?> getAllUsers(
            @PathVariable("idUser") Long idUser) {
        return VsResponseUtil.ok(notificationService.getAllNotification(idUser));
    }

    @ApiOperation(value = "Tạo thông báo của người dùng.")
    @PostMapping(UrlConstant.Notification.DATA_NOTIFICATION_CREATE)
    public ResponseEntity<?> createNotification(
            @PathVariable("idUser") Long idUser,
            @RequestBody NotificationDTO notificationDTO) {
        return VsResponseUtil.ok(notificationService.createNotification(idUser, notificationDTO));
    }

    @ApiOperation(value = "Đánh dấu là đã đọc")
    @GetMapping(UrlConstant.Notification.DATA_NOTIFICATION_MARK_AS_READ)
    public ResponseEntity<?> markAsRead(
            @PathVariable("idNoti") Long idNoti) {
        return VsResponseUtil.ok(notificationService.markAsRead(idNoti));
    }

    @ApiOperation(value = "Đánh dấu là chưa đọc")
    @GetMapping(UrlConstant.Notification.DATA_NOTIFICATION_MARK_AS_UNREAD)
    public ResponseEntity<?> markAsUnRead(
            @PathVariable("idNoti") Long idNoti) {
        return VsResponseUtil.ok(notificationService.markAsUnRead(idNoti));
    }

    @ApiOperation(value = "Xoá thông báo")
    @DeleteMapping(UrlConstant.Notification.DATA_NOTIFICATION_DELETE)
    public ResponseEntity<?> deleteNotification(
            @PathVariable("idNoti") Long idNoti) {
        return VsResponseUtil.ok(notificationService.deleteNotification(idNoti));
    }

}
