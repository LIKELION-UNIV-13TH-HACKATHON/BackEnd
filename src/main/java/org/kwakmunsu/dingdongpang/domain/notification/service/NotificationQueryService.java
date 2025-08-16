package org.kwakmunsu.dingdongpang.domain.notification.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.notification.entity.Notification;
import org.kwakmunsu.dingdongpang.domain.notification.repository.NotificationImageRepository;
import org.kwakmunsu.dingdongpang.domain.notification.repository.NotificationReceiverRepository;
import org.kwakmunsu.dingdongpang.domain.notification.repository.NotificationRepository;
import org.kwakmunsu.dingdongpang.domain.notification.service.dto.NotifyDetailResponse;
import org.kwakmunsu.dingdongpang.domain.notification.service.dto.NotifyListResponse;
import org.kwakmunsu.dingdongpang.domain.notification.service.dto.NotifyPreviewResponse;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationQueryService {

    private final NotificationRepository notificationRepository;
    private final NotificationReceiverRepository notificationReceiverRepository;
    private final NotificationImageRepository notificationImageRepository;

    public NotifyListResponse getNotifications(Long receiverId) {
        List<Long> notificationIdList = notificationReceiverRepository.findByReceiverId(receiverId);
        List<Notification> notifications = notificationRepository.findByIdInAndIsSentTrue(notificationIdList);

        return getNotifyListResponse(notifications);
    }

    public NotifyListResponse getNotificationsByShop(Long shopId) {
        List<Notification> notifications = notificationRepository.findByShopIdInAndIsSentTrue(shopId);

        return getNotifyListResponse(notifications);
    }

    public NotifyDetailResponse getNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId);
        List<String> images = notificationImageRepository.findByNotificationId(notificationId);

        return NotifyDetailResponse.of(notification, images);
    }

    private NotifyListResponse getNotifyListResponse(List<Notification> notifications) {
        List<NotifyPreviewResponse> notifyPreviewResponses = notifications.stream()
                .map(NotifyPreviewResponse::from)
                .toList();
        return new NotifyListResponse(notifyPreviewResponses);
    }

    public String getTodayLatestNotification(Long shopId) {
        Optional<Notification> optionalNotification = notificationRepository.findTodayLatestByShopId(shopId);

        return optionalNotification.map(Notification::getMessage)
                .orElse(null);
    }
}