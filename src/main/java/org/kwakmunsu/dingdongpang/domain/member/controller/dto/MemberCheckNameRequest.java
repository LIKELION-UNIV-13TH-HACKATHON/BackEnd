package org.kwakmunsu.dingdongpang.domain.member.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record MemberCheckNameRequest(
        @NotBlank(message = "닉네임은 필수 값입니다.")
        String nickname
) {

}