package org.kwakmunsu.dingdongpang.domain.shop.service.dto;

import java.util.List;
import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.kwakmunsu.dingdongpang.domain.shop.repository.dto.ShopOperationTimeResponse;

@Builder
public record ShopResponse(
        String shopName,
        ShopType shopType,
        String address,
        String shopTellNumber,
        String mainImage,
        boolean isSubscribe,
        List<ShopOperationTimeResponse> operationTimeResponses,
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
                .address(shop.getAddress())
                .shopTellNumber(shop.getShopTellNumber())
                .mainImage(shop.getMainImage())
                .isSubscribe(isSubscribe)
                .operationTimeResponses(operationTimeResponses)
                .shopImages(shopImages)
                .build();
    }

}