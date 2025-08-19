package org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "일자별 구독자 수")
public record DailySubscriptionResponse(
        @Schema(description = "해당 일자", example = "1970-01-01")
        LocalDate date,

        @Schema(description = "신규 구독자 수", example = "12")
        Long subscriptionCount
) {

}