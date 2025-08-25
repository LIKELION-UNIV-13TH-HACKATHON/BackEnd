package org.kwakmunsu.dingdongpang.domain.message.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kwakmunsu.dingdongpang.domain.message.service.dto.MessagesSuggestionServiceRequest;
import org.kwakmunsu.dingdongpang.global.exception.InternalServerException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @InjectMocks
    private MessageService messageService;

    @Mock
    private ChatClient chatClient;

    private ChatClient.ChatClientRequestSpec requestBuilder;
    private ChatClient.CallResponseSpec responseBuilder;

    @BeforeEach
    void setUp() {
        // 테스트 메서드 실행 전에 Mock 객체들을 초기화
        requestBuilder = mock(ChatClient.ChatClientRequestSpec.class);
        responseBuilder = mock(ChatClient.CallResponseSpec.class);

        // 메서드 체인의 기본 구조를 미리 설정
        given(chatClient.prompt()).willReturn(requestBuilder);
        given(requestBuilder.system(anyString())).willReturn(requestBuilder);
        given(requestBuilder.user(anyString())).willReturn(requestBuilder);
        given(requestBuilder.call()).willReturn(responseBuilder);
    }

    @DisplayName("메세지 생성에 실패할 경우 예외를 던진다..")
    @Test
    void failGetMessage() {

        given(responseBuilder.chatResponse()).willReturn(null);

        var request = new MessagesSuggestionServiceRequest("test message");

        assertThatThrownBy(() -> messageService.suggestionMessage(request))
                .isInstanceOf(InternalServerException.class);
    }

}