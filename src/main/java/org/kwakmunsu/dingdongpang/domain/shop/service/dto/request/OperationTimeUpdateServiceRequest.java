package org.kwakmunsu.dingdongpang.domain.shop.service.dto.request;

import java.time.DayOfWeek;
import lombok.Builder;

@Builder
public record OperationTimeUpdateServiceRequest(
        DayOfWeek dayOfWeek, // java.time.DayOfWeek
        String openTime,     // "HH:mm" 포맷 (필요시 LocalTime으로 후처리)
        String closeTime,
        boolean isClosed
) {

}