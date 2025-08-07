package org.kwakmunsu.dingdongpang.global.jwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "JWT 응답 DTO")
public record TokenResponse(
        @Schema(example = "new-access-token")
        String accessToken,

        @Schema(example = "new-refresh-token")
        String refreshToken
) {

}