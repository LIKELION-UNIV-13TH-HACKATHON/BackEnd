package org.kwakmunsu.dingdongpang.domain.shop.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(description = "매장 요약 대쉬보드 응답 ")
@Builder
public record ShopDashboardResponse(
        @Schema(description = "오늘 알림 발송 수", example = "1")
        Long todayNotificationSentCount,

        @Schema(description = "매장 조회수", example = "10")
        Long shopViewCount,

        @Schema(description = "신규 구독자 수", example = "1")
        Long todaySubscribedCount,

        @Schema(description = "매장 총 구독자 수", example = "1")
        Long totalSubscribedCount
) {

}