package org.kwakmunsu.dingdongpang.domain.member.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "상인 정보 응답 DTO")
public record MerchantProfileResponse(
        @Schema(description = "상인 id", example = "1")
        Long merchantId,

        @Schema(description = "매장명", example = "대훈이네 곱창")
        String shopName,

        @Schema(description = "매장주소", example = "경기도 광주시 경충대로 1480번길")
        String address,

        @Schema(description = "매장 메인 이미지", example = "https:sa212313sd")
        String mainImage
) {

}