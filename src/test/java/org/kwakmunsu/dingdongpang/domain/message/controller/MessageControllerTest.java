package org.kwakmunsu.dingdongpang.domain.message.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        var request = new MessagesSuggestionRequest("testMessage");
        var requestJson = objectMapper.writeValueAsString(request);
        var response = createMockResponse();

        given(messageService.suggestionMessage(any())).willReturn(response);

        MvcTestResult result = mvcTester.post().uri("/messages/suggestions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .apply(print())
                .bodyJson();
//                .hasPathSatisfying("$.responses[0]", v -> v.assertThat().isEqualTo(response.responses().get(0)))
//                .hasPathSatisfying("$.responses[1]", v -> v.assertThat().isEqualTo(response.responses().get(1)))
//                .hasPathSatisfying("$.responses[2]", v -> v.assertThat().isEqualTo(response.responses().get(2)))
//                .hasPathSatisfying("$.responses[3]", v -> v.assertThat().isEqualTo(response.responses().get(3)));
    }

    private MessagesSuggestionResponse createMockResponse() {
        Map<MessageType, List<String>> mockData = new HashMap<>();

        // KIND 스타일 목 데이터
        mockData.put(MessageType.KIND, List.of(
                "저희 카페에서 따뜻한 시간 보내시면 어떨까요?",
                "정성껏 준비한 음료로 여러분을 기다리고 있습니다.",
                "편안한 분위기에서 맛있는 커피 한 잔 드세요.",
                "언제나 고객을 먼저 생각하는 카페입니다."
        ));

        // HUMOR 스타일 목 데이터
        mockData.put(MessageType.HUMOR, List.of(
                "커피 마시러 오세요! 저희 원두는 잠이 안 올 정도로 신선해요 ㅋㅋ",
                "라떼 한 잔에 인생이 바뀔 수도 있어요 (진짜에요!)",
                "카페인 중독자들의 성지! 어서오세요~",
                "우리 커피는 월요병도 치료합니다 (효과 개인차 있음)"
        ));

        // CONCISE 스타일 목 데이터
        mockData.put(MessageType.CONCISE, List.of(
                "신선한 원두, 합리적 가격",
                "매일 오전 7시 오픈, 프리미엄 커피",
                "WiFi 완비, 조용한 카페",
                "테이크아웃 할인 10%"
        ));

        return new MessagesSuggestionResponse(mockData);
    }
}