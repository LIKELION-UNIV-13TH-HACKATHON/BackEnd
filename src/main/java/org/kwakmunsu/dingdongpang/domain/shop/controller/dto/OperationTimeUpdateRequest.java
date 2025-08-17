package org.kwakmunsu.dingdongpang.domain.shop.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.DayOfWeek;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.request.OperationTimeServiceRequest;

@Schema(description = "매장 운영시간")
public record OperationTimeUpdateRequest(
        @NotBlank(message = "요일은 필수값입니다.")
        @Schema(description = "요일", example = "MONDAY")
        DayOfWeek dayOfWeek, // java.time.DayOfWeek

        @NotBlank(message = "시작 시간은 필수값입니다.")
        @Schema(description = "시작 시간", example = "09:00")
        String openTime,     // "HH:mm" 포맷 (필요시 LocalTime으로 후처리)

        @NotBlank(message = "마감 시간은 필수값입니다.")
        @Schema(description = "마감 시간", example = "21:00")
        String closeTime,

        @NotBlank(message = "휴무 여부는 필수값입니다.")
        @Schema(description = "휴무 여부", example = "false")
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