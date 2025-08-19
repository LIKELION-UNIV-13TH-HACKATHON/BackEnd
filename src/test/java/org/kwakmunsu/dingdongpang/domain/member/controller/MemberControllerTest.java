package org.kwakmunsu.dingdongpang.domain.member.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.ControllerTestSupport;
import org.kwakmunsu.dingdongpang.domain.auth.controller.dto.FcmTokenRequest;
import org.kwakmunsu.dingdongpang.domain.member.controller.dto.CustomerRegisterRequest;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.CheckNicknameResponse;
import org.kwakmunsu.dingdongpang.global.TestMember;
import org.springframework.http.MediaType;

class MemberControllerTest extends ControllerTestSupport {

    @TestMember
    @DisplayName("고객 등록을 한다.")
    @Test
    void register() throws JsonProcessingException {
        var request = new CustomerRegisterRequest("new-nickname", true);
        var requestJson = objectMapper.writeValueAsString(request);

        assertThat(mvcTester.post().uri("/members/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .apply(print())
                .hasStatusOk();
    }

    @TestMember
    @DisplayName("고객 등록 요청 시 닉네임 값이 유효하지 않아 에러를 던진다.")
    @Test
    void failRegisterWhenBadCustomerRegisterRequest() throws JsonProcessingException {
        var request = new CustomerRegisterRequest("", false);
        var requestJson = objectMapper.writeValueAsString(request);

        assertThat(mvcTester.post().uri("/members/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .apply(print())
                .hasStatus(400);
    }

    @DisplayName("닉네임 중복 조회")
    @Test
    void checkNameDuplicate() {
        var nickname = "nickname";
        var response = new CheckNicknameResponse(true);
        given(memberQueryService.isExistsNickname(any())).willReturn(response);

        assertThat(mvcTester.get().uri("/members/check-nickname")
                .param("nickname", nickname)
                .contentType(MediaType.APPLICATION_JSON))
                .apply(print())
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.isExistsNickname").isEqualTo(true);
    }

    @TestMember
    @DisplayName("fcm-token을 업데이트 한다.")
    @Test
    void updateFcmToken() throws JsonProcessingException {
        var fcmTokenRequest = new FcmTokenRequest("fcm-token");
        var jsonToString = objectMapper.writeValueAsString(fcmTokenRequest);

        assertThat(mvcTester.post().uri("/members/fcm-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString))
                .hasStatusOk()
                .apply(print());

    }

}