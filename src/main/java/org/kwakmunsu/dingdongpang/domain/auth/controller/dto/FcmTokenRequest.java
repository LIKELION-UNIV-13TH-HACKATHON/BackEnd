package org.kwakmunsu.dingdongpang.domain.auth.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.kwakmunsu.dingdongpang.domain.auth.service.dto.FcmTokenServiceRequest;

@Schema(description = "알림 fcmtoken 업데이트 DTO")
public record FcmTokenRequest(

        @Schema(description = "fcm 디바이스 토큰", example = "your-fcm-token")
        @NotBlank(message = "fcmToken은 필수값입니다.")
        String fcmToken
) {

    public FcmTokenServiceRequest toServiceRequest(Long memberId) {
        return FcmTokenServiceRequest.builder()
                .fcmToken(fcmToken)
                .memberId(memberId)
                .build();
    }

}