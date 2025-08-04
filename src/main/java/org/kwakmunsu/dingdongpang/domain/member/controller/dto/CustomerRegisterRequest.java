package org.kwakmunsu.dingdongpang.domain.member.controller.dto;

import jakarta.validation.constraints.NotBlank;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.CustomerRegisterServiceRequest;

public record CustomerRegisterRequest(
        @NotBlank(message = "닉네임은 필수 값입니다.")
        String nickname
) {

    public CustomerRegisterServiceRequest toServiceRequest(Long memberId) {
        return CustomerRegisterServiceRequest.builder()
                .nickname(nickname)
                .memberId(memberId)
                .build();
    }

}