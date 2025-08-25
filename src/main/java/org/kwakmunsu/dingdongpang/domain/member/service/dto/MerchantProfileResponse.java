package org.kwakmunsu.dingdongpang.domain.member.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;

@Schema(description = "상인 정보 응답 DTO")
@Builder
public record MerchantProfileResponse(
        @Schema(description = "매장 id", example = "1")
        Long shopId,

        @Schema(description = "매장명", example = "대훈이네 곱창")
        String shopName,

        @Schema(description = "매장주소", example = "경기도 광주시 경충대로 1480번길")
        String address,

        @Schema(description = "매장 메인 이미지", example = "https:sa212313sd")
        String mainImage,

        @Schema(description = "매장 경도", example = "126.213123")
        Double longitude,

        @Schema(description = "매장 위도", example = "43.314134")
        Double latitude,

        @Schema(description = "약관 동의 여부", example = "true")
        boolean isTermAgreed
) {

    public static MerchantProfileResponse from(Shop shop, boolean termAgreed) {
        return MerchantProfileResponse.builder()
                .shopId(shop.getId())
                .shopName(shop.getShopName())
                .address(shop.getAddress())
                .mainImage(shop.getMainImage())
                .longitude(shop.getLocation().getX())
                .latitude(shop.getLocation().getY())
                .isTermAgreed(termAgreed)
                .build();
    }

}