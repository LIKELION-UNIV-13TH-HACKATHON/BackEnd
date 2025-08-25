package org.kwakmunsu.dingdongpang.domain.message.service;

import static org.kwakmunsu.dingdongpang.domain.message.service.Prompt.PROMPT;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.dingdongpang.domain.message.service.dto.MessagesSuggestionResponse;
import org.kwakmunsu.dingdongpang.domain.message.service.dto.MessagesSuggestionServiceRequest;
import org.kwakmunsu.dingdongpang.global.exception.InternalServerException;
import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class MessageService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public MessagesSuggestionResponse suggestionMessage(MessagesSuggestionServiceRequest request) {
        log.info("🤖 AI 메시지 생성 시작 - 입력: {}", request.message());

        String systemPrompt = PROMPT.getValue();

        ChatResponse chatResponse = chatClient.prompt()
                .system(systemPrompt)
                .user(request.message())
                .call()
                .chatResponse();

        Map<MessageType, List<String>> messagesByType = parseJsonResponse(chatResponse);

        log.info("✅ AI 메시지 생성 완료 - KIND: {}개, HUMOR: {}개, CONCISE: {}개",
                messagesByType.get(MessageType.KIND).size(),
                messagesByType.get(MessageType.HUMOR).size(),
                messagesByType.get(MessageType.CONCISE).size());

        return new MessagesSuggestionResponse(messagesByType);
    }

    private Map<MessageType, List<String>> parseJsonResponse(ChatResponse response) {
        if (response == null || response.getResults().isEmpty()) {
            log.error("❌ AI 응답이 비어있습니다");
            throw new InternalServerException(ErrorStatus.FAIL_GENERATE_MESSAGE);
        }

        String jsonResponse = response.getResults().stream()
                .map(r -> r.getOutput().getText())
                .filter(s -> s != null && !s.isBlank())
                .findFirst()
                .orElse("");

        if (jsonResponse.isBlank()) {
            log.error("❌ AI 응답 텍스트가 비어있습니다");
            throw new InternalServerException(ErrorStatus.FAIL_GENERATE_MESSAGE);
        }

        log.debug("🔍 AI JSON 응답: {}", jsonResponse);

        try {
            // JSON 파싱
            JsonNode jsonNode = objectMapper.readTree(jsonResponse);

            Map<MessageType, List<String>> result = new HashMap<>();

            // 각 MessageType별로 처리
            for (MessageType type : MessageType.values()) {
                List<String> messages = new ArrayList<>();
                JsonNode messagesNode = jsonNode.get(type.name());

                if (messagesNode != null && messagesNode.isArray()) {
                    messagesNode.forEach(messageNode -> {
                        if (messageNode.isTextual()) {
                            messages.add(messageNode.asText());
                        }
                    });
                }

                result.put(type, messages);
                log.debug("📝 {} 타입 메시지 {}개 파싱 완료", type.name(), messages.size());
            }

            return result;

        } catch (Exception e) {
            log.error("❌ JSON 파싱 실패: {}", e.getMessage());
            log.error("응답 내용: {}", jsonResponse);
            throw new InternalServerException(ErrorStatus.FAIL_GENERATE_MESSAGE);
        }
    }
}
