package org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "구독 매장 목록 조회시 보여지는 응답")
public record SubscribeShopPreviewResponse(
        @Schema(description = "구독 Id", example = "1")
        Long subscriptionId,

        @Schema(description = "매장 Id", example = "1")
        Long shopId,

        @Schema(description = "매장 명", example = "대훈이네")
        String shopName,

        @Schema(description = "매장 대표 이미지", example = "https:sd21313")
        String mainImage
) {

}