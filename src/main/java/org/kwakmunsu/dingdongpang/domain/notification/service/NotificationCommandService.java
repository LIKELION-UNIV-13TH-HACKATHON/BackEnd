package org.kwakmunsu.dingdongpang.domain.notification.service;

import static org.kwakmunsu.dingdongpang.domain.notification.entity.SendType.IMMEDIATE;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.dingdongpang.domain.notification.entity.Notification;
import org.kwakmunsu.dingdongpang.domain.notification.entity.NotificationImage;
import org.kwakmunsu.dingdongpang.domain.notification.entity.NotificationReceiver;
import org.kwakmunsu.dingdongpang.domain.notification.repository.NotificationImageRepository;
import org.kwakmunsu.dingdongpang.domain.notification.repository.NotificationReceiverRepository;
import org.kwakmunsu.dingdongpang.domain.notification.repository.NotificationRepository;
import org.kwakmunsu.dingdongpang.domain.notification.service.dto.NotifyAllowServiceRequest;
import org.kwakmunsu.dingdongpang.domain.notification.service.dto.NotifyCreateServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.entity.SubscribeShop;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.SubscribeShopRepository;
import org.kwakmunsu.dingdongpang.infrastructure.firebase.entity.FcmToken;
import org.kwakmunsu.dingdongpang.infrastructure.firebase.repository.FcmTokenRepository;
import org.kwakmunsu.dingdongpang.infrastructure.firebase.service.dto.FcmSendEvent;
import org.kwakmunsu.dingdongpang.infrastructure.s3.S3Provider;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationCommandService {

    private final NotificationRepository notificationRepository;
    private final NotificationReceiverRepository notificationReceiverRepository;
    private final NotificationImageRepository notificationImageRepository;
    private final SubscribeShopRepository subscribeShopRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final ShopRepository shopRepository;
    private final S3Provider s3Provider;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 알림 즉시 발송 - scheduledAt = null
     *  예약 발송 - scheduledAt = 예약 시간
     **/
    @Transactional
    public void register(NotifyCreateServiceRequest request) {
        // 즉시 발송
        if (request.sendType().equals(IMMEDIATE)) {
            registerByShop(request);
            return;
        }
        // 예약 발송
        registerScheduledNotification(request);
    }

    @Transactional
    public void allow(NotifyAllowServiceRequest request) {
        List<FcmToken> fcmTokens = fcmTokenRepository.findByMemberId(request.memberId());
        fcmTokens.forEach(fcmToken -> fcmToken.updateEnabled(request.isAllow()));
    }

    private void registerByShop(NotifyCreateServiceRequest request) {
        Shop shop = shopRepository.findById(request.shopId());

        Long notificationId = createNotification(request, shop);

        createNotificationReceivers(notificationId, request.shopId());

        List<String> notificationImages = saveNotificationImages(request.images(), notificationId);

        FcmSendEvent event = FcmSendEvent.of(request.message(), notificationId, shop, notificationImages);
        eventPublisher.publishEvent(event);
    }

    private void registerScheduledNotification(NotifyCreateServiceRequest request) {
        Shop shop = shopRepository.findById(request.shopId());

        Long notificationId = createNotification(request, shop);

        createNotificationReceivers(notificationId, shop.getId());
        saveNotificationImages(request.images(), notificationId);

        log.info("예약 알림 저장: notificationId={}, scheduledAt={}", notificationId, request.scheduledAt());
    }

    private Long createNotification(NotifyCreateServiceRequest request, Shop shop) {
        Notification notification;
        if (request.sendType().equals(IMMEDIATE)) {
            notification = Notification.createImmediate(shop, request.message());
        } else {
            notification = Notification.createScheduled(shop, request.message(), request.scheduledAt());
        }
        return notificationRepository.save(notification);
    }

    private void createNotificationReceivers(Long notificationId, Long shopId) {
        List<SubscribeShop> subscribeShops = subscribeShopRepository.findByShopId(shopId);
        if (subscribeShops.isEmpty()) {
            return;
        }

        List<NotificationReceiver> receivers = subscribeShops.stream()
                .map(subscribeShop -> NotificationReceiver.create(notificationId, subscribeShop.getMemberId()))
                .toList();

        notificationReceiverRepository.saveAll(receivers); // 배치 저장 하기
    }

    private List<String> saveNotificationImages(List<MultipartFile> images, Long notificationId) {
        if (images == null || images.isEmpty()) {
            return Collections.emptyList();
        }
        return uploadImagesAndGetUrl(images, notificationId);
    }

    private List<String> uploadImagesAndGetUrl(List<MultipartFile> images, Long notificationId) {
        List<String> uploadImages = s3Provider.uploadImages(images);

        uploadImages.forEach(uploadImage -> {
            NotificationImage image = NotificationImage.create(notificationId, uploadImage);
            notificationImageRepository.save(image);
        });

        return uploadImages;
    }

}