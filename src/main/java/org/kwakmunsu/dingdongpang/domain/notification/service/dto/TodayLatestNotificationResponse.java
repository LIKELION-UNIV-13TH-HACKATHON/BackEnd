package org.kwakmunsu.dingdongpang.domain.notification.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "최신 알림 응답")
public record TodayLatestNotificationResponse(

        @Schema(description = "최신 알림 응답 id", example = "1")
        Long notificationId,

        @Schema(description = "최신 알림 ", example = "message")
        String message
) {

}
