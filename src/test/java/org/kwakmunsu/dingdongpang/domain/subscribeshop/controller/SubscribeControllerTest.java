package org.kwakmunsu.dingdongpang.domain.subscribeshop.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.ControllerTestSupport;
import org.kwakmunsu.dingdongpang.global.TestMember;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class SubscribeControllerTest extends ControllerTestSupport {

    @TestMember
    @DisplayName("매장 구독을 한다")
    @Test
    void subscribe() {
        assertThat(mvcTester.post().uri("/shops/{shopId}/subscriptions", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .hasStatusOk()
                .apply(print());
    }

    @TestMember
    @DisplayName("매장 구독을 취소 한다")
    @Test
    void unsubscribe() {
        assertThat(mvcTester.delete().uri("/shops/{shopId}/subscriptions", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .hasStatus(HttpStatus.NO_CONTENT)
                .apply(print());
    }

}