package org.kwakmunsu.dingdongpang.domain.menu.service.dto;

import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.menu.entity.dto.MenuUpdateDomainRequest;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record MenuUpdateServiceRequest(
        Long id,
        String name,
        int price,
        String description,
        MultipartFile image,
        Long merchantId
) {

    public MenuUpdateDomainRequest toDomainRequest(String image) {
        return MenuUpdateDomainRequest.builder()
                .name(name)
                .price(price)
                .description(description)
                .image(image)
                .build();
    }

}