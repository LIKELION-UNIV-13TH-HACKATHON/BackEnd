package org.kwakmunsu.dingdongpang.domain.notification.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.sql.Timestamp;
import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.notification.entity.Notification;

@Schema(description = "최신 알림 응답")
@Builder
public record TodayLatestNotificationResponse(

        @Schema(description = "최신 알림 응답 id", example = "1")
        Long notificationId,

        @Schema(description = "최신 알림 ", example = "message")
        String message,

        @Schema(description = "알림 전송 시각", example = "1234567890")
        long sentAt
) {

    public static TodayLatestNotificationResponse from(Notification notification) {
        return TodayLatestNotificationResponse.builder()
                .notificationId(notification.getId())
                .message(notification.getMessage())
                .sentAt(Timestamp.valueOf(notification.getUpdatedAt()).getTime())
                .build();
    }

}
