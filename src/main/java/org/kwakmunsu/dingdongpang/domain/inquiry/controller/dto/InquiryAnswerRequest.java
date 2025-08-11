package org.kwakmunsu.dingdongpang.domain.inquiry.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.request.InquiryAnswerServiceRequest;

@Schema(description = "문의 답변 등록 요청 DTO")
public record InquiryAnswerRequest(
        @Schema(description = "답변 내용", example = "괴물쥐도 아니고 쥐는 안나와요. 허위 사실 금지입니다.")
        @NotBlank(message = "답변 내용은 필수값입니다.")
        String answer
) {

    public InquiryAnswerServiceRequest toServiceRequest(Long shopId, Long inquiryId, Long merchantId) {
        return InquiryAnswerServiceRequest.builder()
                .answer(answer)
                .shopId(shopId)
                .inquiryId(inquiryId)
                .merchantId(merchantId)
                .build();
    }

}