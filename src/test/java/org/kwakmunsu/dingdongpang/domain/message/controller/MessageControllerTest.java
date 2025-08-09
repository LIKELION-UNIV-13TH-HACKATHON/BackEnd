package org.kwakmunsu.dingdongpang.domain.message.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.ControllerTestSupport;
import org.kwakmunsu.dingdongpang.domain.message.controller.dto.MessagesSuggestionRequest;
import org.kwakmunsu.dingdongpang.domain.message.service.MessageType;
import org.kwakmunsu.dingdongpang.domain.message.service.dto.MessagesSuggestionResponse;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MvcTestResult;


class MessageControllerTest extends ControllerTestSupport {

    @DisplayName("알림 메세지 샘플들을 가져온다.")
    @Test
    void getNotificationMessage() throws JsonProcessingException {
        var request = new MessagesSuggestionRequest("testMessage", MessageType.HUMOR);
        var requestJson = objectMapper.writeValueAsString(request);
        var response = new MessagesSuggestionResponse(List.of("1", "2", "3", "4"));

        given(messageService.suggestionMessage(any())).willReturn(response);

        MvcTestResult result = mvcTester.post().uri("/messages/suggestions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .apply(print())
                .bodyJson()
                .hasPathSatisfying("$.responses[0]", v -> v.assertThat().isEqualTo(response.responses().get(0)))
                .hasPathSatisfying("$.responses[1]", v -> v.assertThat().isEqualTo(response.responses().get(1)))
                .hasPathSatisfying("$.responses[2]", v -> v.assertThat().isEqualTo(response.responses().get(2)))
                .hasPathSatisfying("$.responses[3]", v -> v.assertThat().isEqualTo(response.responses().get(3)));
    }

}