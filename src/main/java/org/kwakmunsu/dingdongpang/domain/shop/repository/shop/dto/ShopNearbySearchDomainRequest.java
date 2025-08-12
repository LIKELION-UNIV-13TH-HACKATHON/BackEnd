package org.kwakmunsu.dingdongpang.domain.shop.repository.shop.dto;

import lombok.Builder;

@Builder
public record ShopNearbySearchDomainRequest(Double longitude, Double latitude, int radiusMeters) {

}