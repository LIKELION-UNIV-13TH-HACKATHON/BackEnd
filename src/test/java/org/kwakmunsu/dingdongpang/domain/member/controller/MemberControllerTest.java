package org.kwakmunsu.dingdongpang.domain.member.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.ControllerTestSupport;
import org.kwakmunsu.dingdongpang.domain.member.controller.dto.CustomerRegisterRequest;
import org.kwakmunsu.dingdongpang.global.TestMember;
import org.springframework.http.MediaType;

class MemberControllerTest extends ControllerTestSupport {

    @TestMember
    @DisplayName("고객 등록을 한다.")
    @Test
    void register() throws JsonProcessingException {
        var request = new CustomerRegisterRequest("new-nickname");
        var requestJson = objectMapper.writeValueAsString(request);

        assertThat(mvcTester.post().uri("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .apply(print())
                .hasStatusOk();
    }

    @TestMember
    @DisplayName("고객 등록 요청 시 닉네임 값이 유효하지 않아 에러를 던진다.")
    @Test
    void failRegisterWhenBadCustomerRegisterRequest() throws JsonProcessingException {
        var request = new CustomerRegisterRequest("");
        var requestJson = objectMapper.writeValueAsString(request);

        assertThat(mvcTester.post().uri("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        )
                .apply(print())
                .hasStatus(400);
    }

}