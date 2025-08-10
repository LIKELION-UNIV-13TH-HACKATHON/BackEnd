package org.kwakmunsu.dingdongpang.domain.shop.service.dto;

import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.shop.entity.SortBy;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.dto.ShopReadDomainRequest;

@Builder
public record ShopReadServiceRequest(
        Long memberId,
        String q,
        SortBy sortBy,
        Long lastShopId,
        Long lastSubscribeCount,
        Double lastDistance,
        Double longitude,
        Double latitude
) {

    public ShopReadDomainRequest toDomainRequest() {
        return ShopReadDomainRequest.builder()
                .memberId(memberId)
                .q(q)
                .sortBy(sortBy)
                .lastShopId(lastShopId)
                .lastSubscribeCount(lastSubscribeCount)
                .lastDistance(lastDistance)
                .longitude(longitude)
                .latitude(latitude)
                .build();

    }
}