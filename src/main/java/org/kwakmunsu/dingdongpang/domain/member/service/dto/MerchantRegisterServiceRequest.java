package org.kwakmunsu.dingdongpang.domain.member.service.dto;

import lombok.Builder;

@Builder
public record MerchantRegisterServiceRequest(
        String nickname,
        String businessRegistrationNumber, // 사업자 등록 번호
        Long memberId,
        ShopRegisterServiceRequest shopRegisterServiceRequest
) {

}