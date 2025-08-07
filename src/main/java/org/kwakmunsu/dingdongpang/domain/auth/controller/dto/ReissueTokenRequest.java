package org.kwakmunsu.dingdongpang.domain.auth.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "토큰 재발급 요청 DTO")
public record ReissueTokenRequest(
        @Schema(example = "your-refreshToken")
        @NotBlank(message = "refreshToken은 필수 값입니다.")
        String refreshToken
) {

}