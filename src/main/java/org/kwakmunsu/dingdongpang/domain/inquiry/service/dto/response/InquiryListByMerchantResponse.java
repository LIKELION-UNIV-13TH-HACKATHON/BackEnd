package org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "상인 전용 문의 목록 조회 응답 DTO")
public record InquiryListByMerchantResponse(
        List<InquiryByMerchantResponse> responses
) {

}