package org.kwakmunsu.dingdongpang.domain.message.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Schema(description = "알림 샘플 메세지 응답 DTO")
@Builder
public record MessagesSuggestionResponse(
        @Schema(description = "샘플 매세지 - 총 4개")
        List<String> responses
) {

}