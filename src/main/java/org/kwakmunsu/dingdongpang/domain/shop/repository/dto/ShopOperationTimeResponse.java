package org.kwakmunsu.dingdongpang.domain.shop.repository.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.DayOfWeek;
import java.time.LocalTime;
import lombok.Builder;

@Builder
public record ShopOperationTimeResponse(
        @Schema(description = "요일", example = "MONDAY")
        DayOfWeek dayOfWeek,

        @Schema(description = "시작 시간", example = "09:00")
        LocalTime openTime,

        @Schema(description = "마감 시간", example = "21:00")
        LocalTime closeTime,

        @Schema(description = "휴무 여부", example = "false")
        boolean isClosed
) {
}