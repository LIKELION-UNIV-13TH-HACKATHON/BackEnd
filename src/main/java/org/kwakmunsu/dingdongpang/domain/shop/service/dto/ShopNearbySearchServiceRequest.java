package org.kwakmunsu.dingdongpang.domain.shop.service.dto;

import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.dto.ShopNearbySearchDomainRequest;

@Builder
public record ShopNearbySearchServiceRequest(
        Double longitude,
        Double latitude,
        int radiusMeters
) {

    public ShopNearbySearchDomainRequest toDomainRequest() {
        return new ShopNearbySearchDomainRequest(longitude, latitude, radiusMeters);
    }

}