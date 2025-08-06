package org.kwakmunsu.dingdongpang.domain.auth.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record ReissueTokenRequest(
        @NotBlank(message = "refreshToken은 필수 값입니다.")
        String refreshToken
) {

}