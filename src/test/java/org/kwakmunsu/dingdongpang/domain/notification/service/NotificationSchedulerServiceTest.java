package org.kwakmunsu.dingdongpang.domain.notification.service;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.notification.entity.Notification;
import org.kwakmunsu.dingdongpang.domain.notification.repository.NotificationImageRepository;
import org.kwakmunsu.dingdongpang.domain.notification.repository.NotificationRepository;
import org.kwakmunsu.dingdongpang.domain.shop.ShopFixture;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.kwakmunsu.dingdongpang.global.GeoFixture;
import org.locationtech.jts.geom.Point;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
record NotificationSchedulerServiceTest(
        NotificationSchedulerService notificationSchedulerService,
        NotificationRepository notificationRepository,
        NotificationImageRepository notificationImageRepository,
        ShopRepository shopRepository,
        ApplicationEventPublisher eventPublisher
) {

    @DisplayName("예약 알림 테스트 추가")
    @Test
    void sendScheduledNotifications() {
         var request = ShopFixture.getShopRegisterServiceRequest();
        Point point = GeoFixture.createPoint(1.2, 2.3);
        var merchantId = 1L;
        Shop shop = Shop.create(request.toDomainRequest(merchantId, point, null));
        shopRepository.save(shop);

        Notification notification = Notification.createScheduled(shop, "testmessage", LocalDateTime.now());
        Notification notification2 = Notification.createScheduled(shop, "testmessage", LocalDateTime.now());
        notificationRepository.save(notification);
        notificationRepository.save(notification2);

        notificationSchedulerService.sendScheduledNotifications();
    }
}