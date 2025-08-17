package org.kwakmunsu.dingdongpang.domain.auth.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.kwakmunsu.dingdongpang.domain.auth.service.dto.SignInServiceRequest;

@Schema(description = "로그인 요청 DTO")
public record SignInRequest(

        @Schema(description = "소셜 로그인에서 발급된 accessToken", example = "your-social-access-token")
        @NotBlank(message = "소셜 AccessToken은 필수값입니다.")
        String socialAccessToken
) {

    public SignInServiceRequest toServiceRequest() {
        return SignInServiceRequest.builder()
                .socialAccessToken(socialAccessToken)
                .build();
    }

}