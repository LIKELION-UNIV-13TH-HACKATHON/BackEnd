package org.kwakmunsu.dingdongpang.domain.member.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "고객 정보 응답 DTO")
public record CustomerProfileResponse(
        @Schema(description = "고객 id", example = "1")
        Long customerId,

        @Schema(description = "고객 닉네임", example = "강딸깍")
        String nickname,

        @Schema(description = "고객 프로필 이미지", example = "https:sa212313sd")
        String profileImage
) {

}