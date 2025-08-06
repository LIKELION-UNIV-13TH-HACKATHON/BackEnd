package org.kwakmunsu.dingdongpang.domain.member.controller.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.DayOfWeek;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.OperationTimeServiceRequest;

public record OperationTimeRequest(
        @NotBlank(message = "요일은 필수값입니다.")
        DayOfWeek dayOfWeek, // java.time.DayOfWeek

        @NotBlank(message = "시작 시간은 필수값입니다.")
        String openTime,     // "HH:mm" 포맷 (필요시 LocalTime으로 후처리)

        @NotBlank(message = "마감 시간은 필수값입니다.")
        String closeTime,

        @NotBlank(message = "휴무 여부는 필수값입니다.")
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