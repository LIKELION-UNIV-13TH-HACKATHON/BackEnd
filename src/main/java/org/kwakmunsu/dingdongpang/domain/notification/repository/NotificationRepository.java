package org.kwakmunsu.dingdongpang.domain.notification.repository;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.notification.entity.Notification;
import org.kwakmunsu.dingdongpang.domain.notification.entity.NotificationType;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class NotificationRepository {

    private final NotificationJpaRepository notificationJpaRepository;

    public Long save(Notification notification) {
        return notificationJpaRepository.save(notification).getId();
    }

    public List<Notification> findReadyToSendScheduledNotifications(NotificationType type, LocalDateTime scheduledAt) {
        return notificationJpaRepository.findReadyToSendScheduledNotifications(type, scheduledAt);
    }

    public List<Notification> findByIdInAndIsSentTrue(List<Long> ids) {
        return notificationJpaRepository.findByIdInAndIsSentTrue(ids);
    }

}