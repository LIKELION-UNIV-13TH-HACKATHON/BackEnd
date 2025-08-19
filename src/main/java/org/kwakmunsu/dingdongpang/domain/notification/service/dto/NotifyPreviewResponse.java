package org.kwakmunsu.dingdongpang.domain.notification.service.dto;

import static org.kwakmunsu.dingdongpang.global.util.TimeConverter.dateTimeToString;

import io.swagger.v3.oas.annotations.media.Schema;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.notification.entity.Notification;

@Schema(description = "알림 목록 정보  DTO")
@Builder
public record NotifyPreviewResponse(
        @Schema(description = "매장 id", example = "1")
        Long shopId,

        @Schema(description = "매장 이름", example = "대훈이네 곱창")
        String shopName,

        @Schema(description = "알림 메세지", example = "알림 메세지입니다")
        String message,

        @Schema(description = "알림 전송 시각 - Ms", example = "1724041650123")
        long sentAt
) {

    public static NotifyPreviewResponse from(Notification notification) {
        return NotifyPreviewResponse.builder()
                .shopId(notification.getShop().getId())
                .shopName(notification.getShop().getShopName())
                .message(notification.getMessage())
                .sentAt(Timestamp.valueOf(notification.getUpdatedAt()).getTime())
                .build();
    }

}