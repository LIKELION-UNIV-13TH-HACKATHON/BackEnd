package org.kwakmunsu.dingdongpang.domain.notification.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.notification.controller.dto.NotifyCreateRequest;
import org.kwakmunsu.dingdongpang.domain.notification.service.NotificationCommandService;
import org.kwakmunsu.dingdongpang.global.annotation.AuthMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationCommandService notificationCommandService;

    @PostMapping("/{shopId}")
    public ResponseEntity<Void> create(
            @Valid @RequestPart NotifyCreateRequest request,
            @RequestPart List<MultipartFile> files,
            @PathVariable Long shopId,
            @AuthMember Long memberId
    ) {
        notificationCommandService.register(request.toServiceRequest(files, shopId, memberId));

        return ResponseEntity.ok().build();
    }

}