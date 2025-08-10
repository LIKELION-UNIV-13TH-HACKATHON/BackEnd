package org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto;

import lombok.Builder;

@Builder
public record SubscribeShopReadDomainRequest(
        Long memberId,
        Long lastShopId,
        Double longitude,
        Double latitude
) {

}