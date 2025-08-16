package org.kwakmunsu.dingdongpang.domain.notification.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.notification.entity.Notification;
import org.kwakmunsu.dingdongpang.domain.notification.entity.NotificationReceiver;
import org.kwakmunsu.dingdongpang.domain.notification.repository.NotificationReceiverRepository;
import org.kwakmunsu.dingdongpang.domain.notification.repository.NotificationRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
record NotificationQueryServiceTest(
        NotificationQueryService notificationQueryService,
        NotificationRepository notificationRepository,
        NotificationReceiverRepository notificationReceiverRepository
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


}