package org.kwakmunsu.dingdongpang.domain.notification.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.kwakmunsu.dingdongpang.domain.notification.service.dto.NotifyAllowServiceRequest;

@Schema(description = "알림 허용 여부 DTO")
public record NotifyAllowRequest(
        @Schema(description = "알림 허용 여부", example = "true")
        @NotBlank(message = "알림 허용은 필수값입니다.")
        boolean isAllow
) {

    public NotifyAllowServiceRequest toServiceRequest(Long memberId) {
        return NotifyAllowServiceRequest.builder()
                .isAllow(isAllow)
                .memberId(memberId)
                .build();
    }

}