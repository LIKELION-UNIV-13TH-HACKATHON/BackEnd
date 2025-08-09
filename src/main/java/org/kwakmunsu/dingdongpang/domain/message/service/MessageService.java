package org.kwakmunsu.dingdongpang.domain.message.service;

import static org.kwakmunsu.dingdongpang.domain.message.service.Prompt.PROMPT;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.message.service.dto.MessagesSuggestionResponse;
import org.kwakmunsu.dingdongpang.domain.message.service.dto.MessagesSuggestionServiceRequest;
import org.kwakmunsu.dingdongpang.global.exception.InternalServerException;
import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final ChatClient chatClient;

    public MessagesSuggestionResponse suggestionMessage(MessagesSuggestionServiceRequest request) {
        String systemPrompt = PROMPT.getSystemPrompt(request.messageType().getDescription());

        ChatResponse chatResponse = chatClient.prompt()
                .system(systemPrompt)
                .user(request.message())
                .call()
                .chatResponse();

        return new MessagesSuggestionResponse(toMessages(chatResponse));
    }

    private List<String> toMessages(ChatResponse response) {
        if (response == null) {
            throw new InternalServerException(ErrorStatus.FAIL_GENERATE_MESSAGE);
        }

        return response.getResults().stream()
                .map(r -> r.getOutput().getText())
                .filter(s -> s != null && !s.isBlank())
                .toList();
    }

}