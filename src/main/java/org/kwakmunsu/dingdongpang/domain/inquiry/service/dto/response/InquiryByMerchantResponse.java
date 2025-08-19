package org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.response;

import static org.kwakmunsu.dingdongpang.global.util.TimeConverter.dateTimeToString;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.Inquiry;

@Schema(description = "상인 전용 문의 목록 조회 정보 ")
@Builder
public record InquiryByMerchantResponse(
        @Schema(description = "문의 id", example = "1")
        Long inquiryId,

        @Schema(description = "작성자 닉네임", example = "곽태풍")
        String authorName,

        @Schema(description = "문의 제목", example = "제목")
        String title,

        @Schema(description = "문의 질문", example = "질문이요?")
        String question,

        @Schema(description = "문의 답변 - 답변이 없는 경우 Null", example = "답변이요")
        String answer,

        @Schema(description = "문의 등록 시간", example = "2025-08-11 오후 12:55")
        LocalDateTime createdAt
) {

    public static InquiryByMerchantResponse of(Inquiry inquiry) {
        return InquiryByMerchantResponse.builder()
                .inquiryId(inquiry.getId())
                .authorName(inquiry.getAuthor().getNickname())
                .question(inquiry.getQuestion())
                .answer(inquiry.getAnswer())
                .createdAt(inquiry.getCreatedAt())
                .build();
    }

}