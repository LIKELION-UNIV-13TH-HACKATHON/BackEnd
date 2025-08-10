package org.kwakmunsu.dingdongpang.domain.shop.repository.shop.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "매장 목록 조회시 보여지는 응답")
public record ShopPreviewResponse(
        @Schema(description = "매장 Id", example = "1")
        Long shopId,

        @Schema(description = "매장 명", example = "대훈이네")
        String shopName,

        @Schema(description = "매장 대표 이미지", example = "https:sd21313")
        String mainImage,

        @Schema(description = "매장 주소", example = "경기도 광주시 경충대로 1480번길")
        String address,

        @Schema(description = "매장 구독 수", example = "10")
        Long subscribeCount,

        @Schema(description = "매장 구독 여부", example = "true")
        boolean isSubscribe,

        @Schema(description = "매장 거리 - 직선 거리여서 오차 있습니다. 단위는 M", example = "167.1231244")
        Double distance
) {

}