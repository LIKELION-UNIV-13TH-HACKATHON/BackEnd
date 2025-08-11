package org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.request;

import lombok.Builder;

@Builder
public record InquiryDeleteServiceRequest(
        Long shopId,
        Long inquiryId,
        Long memberId
) {

}