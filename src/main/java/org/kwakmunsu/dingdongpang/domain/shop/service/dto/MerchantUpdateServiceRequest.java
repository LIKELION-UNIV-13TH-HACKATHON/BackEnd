package org.kwakmunsu.dingdongpang.domain.shop.service.dto;

import lombok.Builder;

@Builder
public record MerchantUpdateServiceRequest(
        String nickname,
        String businessRegistrationNumber, // 사업자 등록 번호
        Long memberId,
        ShopUpdateServiceRequest shopUpdateServiceRequest
) {

}