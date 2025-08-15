package org.kwakmunsu.dingdongpang.domain.notification.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.dingdongpang.domain.notification.entity.Notification;
import org.kwakmunsu.dingdongpang.domain.notification.entity.NotificationType;
import org.kwakmunsu.dingdongpang.domain.notification.repository.NotificationImageRepository;
import org.kwakmunsu.dingdongpang.domain.notification.repository.NotificationRepository;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.kwakmunsu.dingdongpang.infrastructure.firebase.service.dto.FcmSendEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationSchedulerService {

    private final NotificationRepository notificationRepository;
    private final NotificationImageRepository notificationImageRepository;
    private final ShopRepository shopRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(fixedDelay = 60000) // 매 1분마다
    @Transactional
    public void sendScheduledNotifications() {
        LocalDateTime now = LocalDateTime.now();

        // 예약 발송만 조회
        List<Notification> readyToSend = notificationRepository.findReadyToSendScheduledNotifications(NotificationType.SCHEDULED, now);

        if (readyToSend.isEmpty()) {
            return;
        }
        log.info("예약 알림 발송 시작: {}건", readyToSend.size());

        for (Notification notification : readyToSend) {
            sendNotification(notification);
            log.info("예약 알림 이벤트 발행 완료: notificationId={}", notification.getId());
        }
    }

    private void sendNotification(Notification notification) {
        String message = notification.getMessage();
        Long notificationId = notification.getId();

        Shop shop = shopRepository.findById(notification.getShopId());
        List<String> notificationImages = notificationImageRepository.findByNotificationId(notificationId);

        notification.markAsSent();

        FcmSendEvent event = FcmSendEvent.of(message, notificationId, shop, notificationImages);
        eventPublisher.publishEvent(event);
    }

}