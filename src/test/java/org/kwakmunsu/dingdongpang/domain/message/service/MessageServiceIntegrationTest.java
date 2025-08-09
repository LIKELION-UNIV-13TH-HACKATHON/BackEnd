package org.kwakmunsu.dingdongpang.domain.message.service;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.message.controller.dto.MessagesSuggestionRequest;
import org.kwakmunsu.dingdongpang.domain.message.service.dto.MessagesSuggestionResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Disabled("토큰 사용 절감을 위해 비활성화")
@Slf4j
@Transactional
@SpringBootTest
record MessageServiceIntegrationTest(
        MessageService messageService,
        ChatClient chatClient
) {

    @DisplayName("AI가 생성한 홍보 알림 메세지를 가져온다.")
    @Test
    void getMessage() {
        var message = "오늘 18시까지 사과 박스당 만원!";

        var request = new MessagesSuggestionRequest(message, MessageType.HUMOR);
        MessagesSuggestionResponse response = messageService.suggestionMessage(request.toServiceRequest());

        response.responses().forEach(log::info);

        assertThat(response.responses()).hasSize(4); // 반환갯수를 4개로 설정함.
    }

}