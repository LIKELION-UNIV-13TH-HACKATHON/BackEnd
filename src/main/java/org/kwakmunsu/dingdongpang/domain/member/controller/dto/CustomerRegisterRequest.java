package org.kwakmunsu.dingdongpang.domain.member.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.CustomerRegisterServiceRequest;

@Schema(description = "고객 회원 생성 요청 DTO")
public record CustomerRegisterRequest(
        @NotBlank(message = "닉네임은 필수 값입니다.")
        @Schema(description = "닉네임", example = "강대훈")
        String nickname,

        @Schema(description = "약관 동의 여부", example = "true")
        boolean isTermAgreed
) {

    public CustomerRegisterServiceRequest toServiceRequest(Long memberId) {
        return CustomerRegisterServiceRequest.builder()
                .nickname(nickname)
                .isTermAgreed(isTermAgreed)
                .memberId(memberId)
                .build();
    }

}