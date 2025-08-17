package org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.request;

import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.InquiryStatus;

@Builder
public record InquiryReadByMerchantServiceRequest(
        Long merchantId,
        InquiryStatus status
) {

}
