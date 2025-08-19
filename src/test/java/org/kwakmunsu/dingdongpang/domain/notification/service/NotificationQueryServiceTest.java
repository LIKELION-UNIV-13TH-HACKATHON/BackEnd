package org.kwakmunsu.dingdongpang.domain.notification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kwakmunsu.dingdongpang.global.util.TimeConverter.dateTimeToString;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.notification.entity.Notification;
import org.kwakmunsu.dingdongpang.domain.notification.entity.NotificationImage;
import org.kwakmunsu.dingdongpang.domain.notification.entity.NotificationReceiver;
import org.kwakmunsu.dingdongpang.domain.notification.repository.NotificationImageRepository;
import org.kwakmunsu.dingdongpang.domain.notification.repository.NotificationReceiverRepository;
import org.kwakmunsu.dingdongpang.domain.notification.repository.NotificationRepository;
import org.kwakmunsu.dingdongpang.domain.notification.service.dto.NotifyDetailResponse;
import org.kwakmunsu.dingdongpang.domain.shop.ShopFixture;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.kwakmunsu.dingdongpang.global.GeoFixture;
import org.locationtech.jts.geom.Point;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
record NotificationQueryServiceTest(
        NotificationQueryService notificationQueryService,
        NotificationRepository notificationRepository,
        NotificationReceiverRepository notificationReceiverRepository,
        NotificationImageRepository notificationImageRepository,
        ShopRepository shopRepository
) {

    @DisplayName("회원에 대한 알림 목록")
    @Test
    void getNotifications() {
        var memberId = 1L;
        Point point = GeoFixture.createPoint(1.2, 2.3);
        var domainRequest = ShopFixture.getShopRegisterServiceRequest().toDomainRequest(1L, point, "string");
        Shop shop = Shop.create(domainRequest);
        shopRepository.save(shop);

        var notification = Notification.createImmediate(shop, "testMessage");
        var notification2 = Notification.createImmediate(shop, "testMessage2");
        var notification3 = Notification.createScheduled(shop, "testMessage3", LocalDateTime.now().plusHours(10));
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
        Point point = GeoFixture.createPoint(1.2, 2.3);
        var domainRequest = ShopFixture.getShopRegisterServiceRequest().toDomainRequest(1L, point, "string");
        Shop shop = Shop.create(domainRequest);
        shopRepository.save(shop);
        var notification = Notification.createImmediate(shop, "testMessage");
        var notification2 = Notification.createImmediate(shop, "testMessage2");
        var notification3 = Notification.createScheduled(shop, "testMessage3", LocalDateTime.now().plusHours(10));
        notificationRepository.save(notification);
        notificationRepository.save(notification2);
        notificationRepository.save(notification3);

        var receiver = NotificationReceiver.create(notification.getId(), memberId);
        var receiver1 = NotificationReceiver.create(notification2.getId(), memberId);
        var receiver2 = NotificationReceiver.create(notification3.getId(), memberId);
        notificationReceiverRepository.save(receiver);
        notificationReceiverRepository.save(receiver1);
        notificationReceiverRepository.save(receiver2);

        var response = notificationQueryService.getNotificationsByShop(shop.getId());
        assertThat(response.responses()).hasSize(2);
    }

    @DisplayName("알림 상세 조회를 한다.")
    @Test
    void getNotification() {
        Point point = GeoFixture.createPoint(1.2, 2.3);
        var domainRequest = ShopFixture.getShopRegisterServiceRequest().toDomainRequest(1L, point, "string");
        Shop shop = Shop.create(domainRequest);
        shopRepository.save(shop);

        var notification = Notification.createImmediate(shop, "testMessage");
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
                        notification.getShop().getId(),
                        notification.getMessage(),
                        notification.getUpdatedAt()
                );
    }

}