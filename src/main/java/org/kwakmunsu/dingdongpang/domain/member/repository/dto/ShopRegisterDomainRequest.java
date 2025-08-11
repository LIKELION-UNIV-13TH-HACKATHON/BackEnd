package org.kwakmunsu.dingdongpang.domain.member.repository.dto;

import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.locationtech.jts.geom.Point;

@Builder
public record ShopRegisterDomainRequest(
        Long merchantId,
        String shopName,
        String businessNumber,
        String ownerName,
        ShopType shopType,
        String address,
        String shopTellNumber,
        Point location,
        String mainImage
) {

}
