package org.kwakmunsu.dingdongpang.domain.inquiry.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.request.InquiryRegisterServiceRequest;

@Schema(description = "문의 등록 요청 DTO")
public record InquiryRegisterRequest(
        @Schema(description = "문의 제목", example = "몇번째입니까.")
        @NotBlank(message = "문의 제목은 필수값입니다.")
        String title,

        @Schema(description = "문의 내용", example = "이 집에 쥐가 나온다는데 맞나요?")
        @NotBlank(message = "문의 내용은 필수값입니다.")
        String question
) {

    public InquiryRegisterServiceRequest toServiceRequest(Long shopId, Long memberId) {
        return InquiryRegisterServiceRequest.builder()
                .title(title)
                .question(question)
                .shopId(shopId)
                .memberId(memberId)
                .build();
    }

}