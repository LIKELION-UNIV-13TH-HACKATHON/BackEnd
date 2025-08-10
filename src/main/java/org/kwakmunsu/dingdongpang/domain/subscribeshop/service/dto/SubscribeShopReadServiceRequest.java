package org.kwakmunsu.dingdongpang.domain.subscribeshop.service.dto;

import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto.SubscribeShopReadDomainRequest;

@Builder
public record SubscribeShopReadServiceRequest(
        Long memberId,
        Long lastShopId,
        Double longitude,
        Double latitude
) {

    public SubscribeShopReadDomainRequest toDomainRequest() {
        return SubscribeShopReadDomainRequest.builder()
                .memberId(memberId)
                .lastShopId(lastShopId)
                .longitude(longitude)
                .latitude(latitude)
                .build();
    }

}