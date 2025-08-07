package org.kwakmunsu.dingdongpang.domain.menu.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.ControllerTestSupport;
import org.kwakmunsu.dingdongpang.domain.menu.controller.dto.MenuRegisterRequest;
import org.kwakmunsu.dingdongpang.global.TestMember;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;


class MenuControllerTest extends ControllerTestSupport {

    @TestMember
    @DisplayName("매장 메뉴를 등록한다.")
    @Test
    void register() throws JsonProcessingException {
        var menuRegisterRequest = new MenuRegisterRequest("test-name", 10000, "test-description");
        var mainImage = new MockMultipartFile(
                "mainImage",
                "profile.jpg",
                "image/jpeg",
                "profile image content".getBytes()
        );
        var requestPart = new MockMultipartFile(
                "request",
                "request.json",
                "application/json",
                objectMapper.writeValueAsString(menuRegisterRequest).getBytes()
        );

        MvcResult result = mvcTester.perform(multipart("/shops/menus")
                .file(requestPart)
                .file(mainImage)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(objectMapper.writeValueAsString(menuRegisterRequest))).getMvcResult();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    @TestMember
    @DisplayName("요청 Data의 유효성 검증 실패로 메뉴 등록에 실패한다.")
    @Test
    void failRegister() throws JsonProcessingException {
        var menuRegisterRequest = new MenuRegisterRequest("", -10000, "");
        var requestPart = new MockMultipartFile(
                "request",
                "request.json",
                "application/json",
                objectMapper.writeValueAsString(menuRegisterRequest).getBytes()
        );

        MvcResult result = mvcTester.perform(multipart("/shops/menus")
                .file(requestPart)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(objectMapper.writeValueAsString(menuRegisterRequest))).getMvcResult();

        assertThat(result.getResponse().getStatus()).isEqualTo(400);
    }

}