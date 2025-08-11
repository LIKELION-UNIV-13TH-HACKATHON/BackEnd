package org.kwakmunsu.dingdongpang.domain.inquiry.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "문의 목록 조회 응답 DTO")
public record InquiryListResponse(
        List<InquiryPreviewResponse> responses
) {

}