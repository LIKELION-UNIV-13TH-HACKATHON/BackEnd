package org.kwakmunsu.dingdongpang.domain.notification.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.notification.controller.dto.NotifyAllowRequest;
import org.kwakmunsu.dingdongpang.domain.notification.controller.dto.NotifyCreateRequest;
import org.kwakmunsu.dingdongpang.domain.notification.service.NotificationCommandService;
import org.kwakmunsu.dingdongpang.domain.notification.service.NotificationQueryService;
import org.kwakmunsu.dingdongpang.domain.notification.service.dto.NotifyDetailResponse;
import org.kwakmunsu.dingdongpang.domain.notification.service.dto.NotifyListResponse;
import org.kwakmunsu.dingdongpang.domain.notification.service.dto.TodayLatestNotificationResponse;
import org.kwakmunsu.dingdongpang.global.annotation.AuthMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class NotificationController extends NotificationDocsController {

    private final NotificationCommandService notificationCommandService;
    private final NotificationQueryService notificationQueryService;

    @Override
    @PostMapping("/notifications/{shopId}")
    public ResponseEntity<Void> create(
            @Valid @RequestPart NotifyCreateRequest request,
            @RequestPart List<MultipartFile> files,
            @PathVariable Long shopId,
            @AuthMember Long memberId
    ) {
        notificationCommandService.register(request.toServiceRequest(files, shopId, memberId));

        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/notifications")
    public ResponseEntity<NotifyListResponse> getNotifications(@AuthMember Long memberId) {
        NotifyListResponse response = notificationQueryService.getNotifications(memberId);

        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/shops/notifications/{shopId}")
    public ResponseEntity<NotifyListResponse> getNotificationsByShop(@PathVariable Long shopId) {
        NotifyListResponse response = notificationQueryService.getNotificationsByShop(shopId);

        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/notifications/{shopId}/latest")
    public ResponseEntity<TodayLatestNotificationResponse> getLatestNotification(@PathVariable Long shopId) {
        TodayLatestNotificationResponse response = notificationQueryService.getTodayLatestNotification(shopId);

        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/notifications/{notificationId}")
    public ResponseEntity<NotifyDetailResponse> getNotification(@PathVariable Long notificationId) {
        NotifyDetailResponse response = notificationQueryService.getNotification(notificationId);

        return ResponseEntity.ok(response);
    }

    @Override
    @PatchMapping("/notifications/allow")
    public ResponseEntity<Void> allow(
            @Valid @RequestPart NotifyAllowRequest request,
            @AuthMember Long memberId
    ) {
        notificationCommandService.allow(request.toServiceRequest(memberId));

        return  ResponseEntity.noContent().build();
    }


}