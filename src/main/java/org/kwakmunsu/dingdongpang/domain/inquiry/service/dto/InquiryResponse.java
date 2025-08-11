package org.kwakmunsu.dingdongpang.domain.inquiry.service.dto;

import static org.kwakmunsu.dingdongpang.global.util.TimeConverter.dateTimeToString;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.Inquiry;

@Schema(description = "문의 목록 조회 정보 ")
@Builder
public record InquiryResponse(
        @Schema(description = "문의 id", example = "1")
        Long inquiryId,

        @Schema(description = "문의 질문", example = "질문이요?")
        String question,

        @Schema(description = "문의 답변 - 답변이 없는 경우 Null", example = "답변이요")
        String answer,

        @Schema(description = "문의 등록 시간", example = "2025-08-11 오후 12:55")
        String createdAt
) {

    public static InquiryResponse of(Inquiry inquiry) {
        return InquiryResponse.builder()
                .inquiryId(inquiry.getId())
                .question(inquiry.getQuestion())
                .answer(inquiry.getAnswer())
                .createdAt(dateTimeToString(inquiry.getCreatedAt()))
                .build();
    }

}