package org.kwakmunsu.dingdongpang.domain.menu.service.dto;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record MenuRegisterServiceRequest(
        String name,
        int price,
        String description,
        MultipartFile image,
        Long merchantId
) {

}