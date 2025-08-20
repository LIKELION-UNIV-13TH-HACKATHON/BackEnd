package org.kwakmunsu.dingdongpang.domain.notification.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.sql.Timestamp;
import java.util.List;
import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.notification.entity.Notification;

@Schema(description = "알림 상세 조회 응답 DTO")
@Builder
public record NotifyDetailResponse(
        @Schema(description = "알림 id", example = "1")
        Long notificationId,

        @Schema(description = "매장 id", example = "1")
        Long shopId,

        @Schema(description = "알림 메세지", example = "알림 메세지입니다.")
        String message,

        @Schema(description = "알림 보낸 시각 - Ms", example = "1724041650123")
        long sentAt,

        @Schema(description = "알림 이미지들")
        List<String> images
) {

    public static NotifyDetailResponse of(Notification notification, List<String> images) {
        return NotifyDetailResponse.builder()
                .notificationId(notification.getId())
                .shopId(notification.getShop().getId())
                .message(notification.getMessage())
                .images(images)
                .sentAt(Timestamp.valueOf(notification.getUpdatedAt()).getTime())
                .build();
    }

}