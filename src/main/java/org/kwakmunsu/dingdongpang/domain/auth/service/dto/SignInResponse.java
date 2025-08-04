package org.kwakmunsu.dingdongpang.domain.auth.service.dto;

import lombok.Builder;
import org.kwakmunsu.dingdongpang.global.jwt.dto.TokenResponse;

@Builder
public record SignInResponse(
        boolean isNewMember, // 기존 회원일 경우 바로 홈, 새로운 회원일 경우 정보 등록
        TokenResponse response
) {

}