package org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.request;

import lombok.Builder;

@Builder
public record InquiryModifyServiceRequest(
        String title,
        String question,
        Long inquiryId,
        Long authorId
) {

}