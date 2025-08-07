package org.kwakmunsu.dingdongpang.domain.auth.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.kwakmunsu.dingdongpang.global.jwt.dto.TokenResponse;

@Builder
@Schema(description = "로그인 응답 DTO")
public record SignInResponse(
        @Schema(description = "기존 회원이 로그인할 경우 false, 새로운 회원일 경우 true", example = "true")
        boolean isNewMember, // 기존 회원일 경우 바로 홈, 새로운 회원일 경우 정보 등록

        TokenResponse response
) {

}