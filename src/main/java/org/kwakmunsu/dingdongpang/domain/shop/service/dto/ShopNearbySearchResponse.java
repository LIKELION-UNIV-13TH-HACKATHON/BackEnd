package org.kwakmunsu.dingdongpang.domain.shop.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;

@Schema(description = "주변 매장 정보 ")
@Builder
public record ShopNearbySearchResponse(
        @Schema(description = "매장 Id", example = "1")
        Long shopId,

        @Schema(description = "매장 명", example = "대훈이네")
        String shopName,

        @Schema(description = "매장 경도", example = "126.213123")
        Double longitude,

        @Schema(description = "매장 위도", example = "43.314134")
        Double latitude
) {

    public static ShopNearbySearchResponse of(Shop shop) {
        return ShopNearbySearchResponse.builder()
                .shopId(shop.getId())
                .shopName(shop.getShopName())
                .longitude(shop.getLocation().getX())
                .latitude(shop.getLocation().getY())
                .build();
    }

}