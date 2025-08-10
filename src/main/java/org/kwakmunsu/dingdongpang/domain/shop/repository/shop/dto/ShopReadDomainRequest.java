package org.kwakmunsu.dingdongpang.domain.shop.repository.shop.dto;

import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.shop.entity.SortBy;

@Builder
public record ShopReadDomainRequest(
        Long memberId,
        String q,
        SortBy sortBy,
        Long lastShopId,
        Long lastSubscribeCount,
        Double lastDistance,
        Double longitude,
        Double latitude
) {

}