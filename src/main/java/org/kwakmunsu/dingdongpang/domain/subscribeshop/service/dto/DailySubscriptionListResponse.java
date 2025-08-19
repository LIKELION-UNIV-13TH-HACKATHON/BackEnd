package org.kwakmunsu.dingdongpang.domain.subscribeshop.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto.DailySubscriptionResponse;

@Schema(description = "일주일간 신규 구독자 수 응답 DTO")
public record DailySubscriptionListResponse(List<DailySubscriptionResponse> responses) {

}