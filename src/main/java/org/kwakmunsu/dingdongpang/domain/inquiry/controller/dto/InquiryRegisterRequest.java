package org.kwakmunsu.dingdongpang.domain.inquiry.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.InquiryRegisterServiceRequest;

@Schema(description = "문의 등록 요청 DTO")
public record InquiryRegisterRequest(
        @Schema(description = "문의 내용", example = "이 집에 쥐가 나온다는데 맞나요?")
        @NotBlank(message = "문의 내용은 필수값입니다.")
        String question
) {

    public InquiryRegisterServiceRequest toServiceRequest(Long shopId, Long memberId) {
        return InquiryRegisterServiceRequest.builder()
                .question(question)
                .shopId(shopId)
                .memberId(memberId)
                .build();
    }

}