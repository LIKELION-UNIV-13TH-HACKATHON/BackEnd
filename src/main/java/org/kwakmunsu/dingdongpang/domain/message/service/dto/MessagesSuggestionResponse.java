package org.kwakmunsu.dingdongpang.domain.message.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.message.service.MessageType;

@Schema(description = "알림 샘플 메세지 응답 DTO")
@Builder
public record MessagesSuggestionResponse(
        @Schema(description = "샘플 매세지 - 총 4개")
        Map<MessageType, List<String>> messagesByType
) {

        public List<String> getKindMessages() {
                return messagesByType.getOrDefault(MessageType.KIND, List.of());
        }

        public List<String> getHumorMessages() {
                return messagesByType.getOrDefault(MessageType.HUMOR, List.of());
        }

        public List<String> getConciseMessages() {
                return messagesByType.getOrDefault(MessageType.CONCISE, List.of());
        }

        public int getTotalMessageCount() {
                return messagesByType.values().stream()
                        .mapToInt(List::size)
                        .sum();
        }
}