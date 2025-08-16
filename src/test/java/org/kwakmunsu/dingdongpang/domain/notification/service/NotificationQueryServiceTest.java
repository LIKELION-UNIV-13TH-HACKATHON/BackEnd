package org.kwakmunsu.dingdongpang.domain.notification.service;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.kwakmunsu.dingdongpang.global.util.TimeConverter.dateTimeToString;

import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.notification.entity.Notification;
import org.kwakmunsu.dingdongpang.domain.notification.entity.NotificationImage;
import org.kwakmunsu.dingdongpang.domain.notification.entity.NotificationReceiver;
import org.kwakmunsu.dingdongpang.domain.notification.repository.NotificationImageRepository;
import org.kwakmunsu.dingdongpang.domain.notification.repository.NotificationReceiverRepository;
import org.kwakmunsu.dingdongpang.domain.notification.repository.NotificationRepository;
import org.kwakmunsu.dingdongpang.domain.notification.service.dto.NotifyDetailResponse;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
record NotificationQueryServiceTest(
        NotificationQueryService notificationQueryService,
        NotificationRepository notificationRepository,
        NotificationReceiverRepository notificationReceiverRepository,
        NotificationImageRepository notificationImageRepository
) {

    @DisplayName("회원에 대한 알림 목록")
    @Test
    void getNotifications() {
        var memberId = 1L;
        var notification = Notification.createImmediate(1L, "testMessage");
        var notification2 = Notification.createImmediate(2L, "testMessage2");
        var notification3 = Notification.createScheduled(3L, "testMessage3", LocalDateTime.now().plusHours(10));
        notificationRepository.save(notification);
        notificationRepository.save(notification2);
        notificationRepository.save(notification3);

        var receiver = NotificationReceiver.create(notification.getId(), memberId);
        var receiver1 = NotificationReceiver.create(notification2.getId(), memberId);
        var receiver2 = NotificationReceiver.create(notification3.getId(), memberId);
        notificationReceiverRepository.save(receiver);
        notificationReceiverRepository.save(receiver1);
        notificationReceiverRepository.save(receiver2);

        var response = notificationQueryService.getNotifications(receiver.getReceiverId());
        assertThat(response.responses()).hasSize(2);
    }

    @DisplayName("매장에 대한 알림을 가져온다.")
    @Test
    void getNotificationByShop() {
        var memberId = 1L;
        var notification = Notification.createImmediate(1L, "testMessage");
        var notification2 = Notification.createImmediate(1L, "testMessage2");
        var notification3 = Notification.createScheduled(1L, "testMessage3", LocalDateTime.now().plusHours(10));
        notificationRepository.save(notification);
        notificationRepository.save(notification2);
        notificationRepository.save(notification3);

        var receiver = NotificationReceiver.create(notification.getId(), memberId);
        var receiver1 = NotificationReceiver.create(notification2.getId(), memberId);
        var receiver2 = NotificationReceiver.create(notification3.getId(), memberId);
        notificationReceiverRepository.save(receiver);
        notificationReceiverRepository.save(receiver1);
        notificationReceiverRepository.save(receiver2);

        var response = notificationQueryService.getNotificationsByShop(1L);
        assertThat(response.responses()).hasSize(2);
    }

    @DisplayName("알림 상세 조회를 한다.")
    @Test
    void getNotification() {
        var notification = Notification.createImmediate(1L, "testMessage");
        notificationRepository.save(notification);
        var notificationImage = NotificationImage.create(notification.getId(), "image");
        notificationImageRepository.save(notificationImage);

        NotifyDetailResponse response = notificationQueryService.getNotification(notification.getId());
        assertThat(response.images()).hasSize(1);

        assertThat(response)
                .extracting(
                        NotifyDetailResponse::notificationId,
                        NotifyDetailResponse::shopId,
                        NotifyDetailResponse::message,
                        NotifyDetailResponse::sentAt
                )
                .containsExactly(
                        notification.getId(),
                        notification.getShopId(),
                        notification.getMessage(),
                        dateTimeToString(notification.getUpdatedAt())
                );
    }

}