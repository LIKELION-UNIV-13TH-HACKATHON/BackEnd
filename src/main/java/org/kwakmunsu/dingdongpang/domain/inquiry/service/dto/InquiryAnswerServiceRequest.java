package org.kwakmunsu.dingdongpang.domain.inquiry.service.dto;

import lombok.Builder;

@Builder
public record InquiryAnswerServiceRequest(
        String answer,
        Long shopId,
        Long inquiryId,
        Long merchantId
) {

}