package org.kwakmunsu.dingdongpang.domain.member.service.dto;

import java.time.DayOfWeek;
import lombok.Builder;

@Builder
public record OperationTimeServiceRequest(
        DayOfWeek dayOfWeek, // java.time.DayOfWeek
        String openTime,     // "HH:mm" 포맷 (필요시 LocalTime으로 후처리)
        String closeTime,
        boolean isClosed
) {

}