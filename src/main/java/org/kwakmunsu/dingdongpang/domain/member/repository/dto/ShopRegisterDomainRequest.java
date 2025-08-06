package org.kwakmunsu.dingdongpang.domain.member.repository.dto;

import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;

@Builder
public record ShopRegisterDomainRequest(
        Long memberId,
        String shopName,
        String businessNumber,
        String ownerName,
        ShopType shopType,
        String address,
        String shopTellNumber,
        String latitude,
        String longitude,
        String mainImage
) {

}
