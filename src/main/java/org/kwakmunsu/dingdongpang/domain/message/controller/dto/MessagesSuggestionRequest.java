package org.kwakmunsu.dingdongpang.domain.message.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.message.service.MessageType;
import org.kwakmunsu.dingdongpang.domain.message.service.dto.MessagesSuggestionServiceRequest;

@Schema(description = "메세지 샘플 생성 요청 DTO")
@Builder
public record MessagesSuggestionRequest(
        @Schema(description = "사용자 입력 메세지", example = "오늘 18시까지 사과 박스 당 만원!")
        @NotBlank(message = "메시지 요청값은 필수입니다.")
        String message,

        @Schema(description = "메세지 유형 타입", example = "KIND")
        MessageType messageType
) {

    public MessagesSuggestionServiceRequest toServiceRequest() {
        return MessagesSuggestionServiceRequest.builder()
                .message(message)
                .messageType(messageType)
                .build();
    }

}