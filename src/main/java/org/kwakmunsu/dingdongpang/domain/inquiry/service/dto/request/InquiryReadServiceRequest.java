package org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.request;

import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.InquiryFilter;

@Builder
public record InquiryReadServiceRequest(
        InquiryFilter filter,
        Long shopId,
        Long memberId
) {

}