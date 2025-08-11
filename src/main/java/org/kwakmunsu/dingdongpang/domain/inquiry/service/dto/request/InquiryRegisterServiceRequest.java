package org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.request;

import lombok.Builder;

@Builder
public record InquiryRegisterServiceRequest(
        String question,
        Long shopId,
        Long memberId
) {

}