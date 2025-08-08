package org.kwakmunsu.dingdongpang.domain.menu.entity.dto;

import lombok.Builder;

@Builder
public record MenuUpdateDomainRequest(
        String name,
        int price,
        String description,
        String image
) {

}