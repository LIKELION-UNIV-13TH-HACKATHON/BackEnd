package org.kwakmunsu.dingdongpang.domain.shop.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shopoperation.dto.ShopOperationTimeResponse;

@Schema(description = "매장 정보 응답 DTO")
@Builder
public record ShopResponse(
        @Schema(description = "매장 이름", example = "문수네닭발")
        String shopName,

        @Schema(description = "매장 업종", example = "FOOD")
        ShopType shopType,

        @Schema(description = "매장 대표명", example = "곽태풍")
        String ownerName,

        @Schema(description = "사업자 등록번호", example = "1234209213")
        String businessNumber,

        @Schema(description = "매장 주소", example = "경기도 광주시 경충대로1461번길 12-4 코오롱 세이브 프라자 202호")
        String address,

        @Schema(description = "매장 이름", example = "010-1234-5678")
        String shopTellNumber,

        @Schema(description = "매장 이름", example = "https:1231312312313")
        String mainImage,

        @Schema(description = "매장 이름", example = "false")
        boolean isSubscribe,

        @Schema(description = "매장 운영 시간 응답 DTO - 월~일 총 7개")
        List<ShopOperationTimeResponse> operationTimeResponses,

        @Schema(description = "매장 이미지 리스트")
        List<String> shopImages
) {

    public static ShopResponse from(
            Shop shop,
            boolean isSubscribe,
            List<ShopOperationTimeResponse> operationTimeResponses,
            List<String> shopImages
    ) {
        return ShopResponse.builder()
                .shopName(shop.getShopName())
                .shopType(shop.getShopType())
                .ownerName(shop.getOwnerName())
                .businessNumber(shop.getBusinessNumber())
                .address(shop.getAddress())
                .shopTellNumber(shop.getShopTellNumber())
                .mainImage(shop.getMainImage())
                .isSubscribe(isSubscribe)
                .operationTimeResponses(operationTimeResponses)
                .shopImages(shopImages)
                .build();
    }

}