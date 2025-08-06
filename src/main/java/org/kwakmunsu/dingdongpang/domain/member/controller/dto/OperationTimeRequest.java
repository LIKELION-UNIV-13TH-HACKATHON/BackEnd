package org.kwakmunsu.dingdongpang.domain.member.controller.dto;

import java.time.DayOfWeek;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.OperationTimeServiceRequest;

public record OperationTimeRequest(
        DayOfWeek dayOfWeek, // java.time.DayOfWeek
        String openTime,     // "HH:mm" 포맷 (필요시 LocalTime으로 후처리)
        String closeTime,
        boolean isClosed
) {

    public OperationTimeServiceRequest toServiceRequest() {
        return OperationTimeServiceRequest.builder()
                .dayOfWeek(dayOfWeek)
                .openTime(openTime)
                .closeTime(closeTime)
                .isClosed(isClosed)
                .build();
    }

}