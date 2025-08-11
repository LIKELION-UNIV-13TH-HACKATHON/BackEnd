package org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.request;

import lombok.Builder;

@Builder
public record InquiryAnswerModifyServiceRequest(
        String answer,
        Long shopId,
        Long inquiryId,
        Long merchantId
) {

}