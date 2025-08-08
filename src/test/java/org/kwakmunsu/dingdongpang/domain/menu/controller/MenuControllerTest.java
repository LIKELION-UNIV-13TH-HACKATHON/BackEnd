package org.kwakmunsu.dingdongpang.domain.menu.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.ControllerTestSupport;
import org.kwakmunsu.dingdongpang.domain.menu.controller.dto.MenuRegisterRequest;
import org.kwakmunsu.dingdongpang.domain.menu.controller.dto.MenuUpdateRequest;
import org.kwakmunsu.dingdongpang.domain.menu.service.dto.MenuListResponse;
import org.kwakmunsu.dingdongpang.domain.menu.service.dto.MenuResponse;
import org.kwakmunsu.dingdongpang.global.TestMember;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.assertj.MvcTestResult;


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

    @TestMember
    @DisplayName("상인이 자신 매장의 메뉴 목록을 조회한다.")
    @Test
    void getMenusByMerchants() {
        var menuResponse = new MenuResponse(1L, "menu1", 10000, "description1", null);
        var menuListResponse = new MenuListResponse(List.of(menuResponse));

        given(menuQueryService.getMenusByMerchant(any())).willReturn(menuListResponse);

        MvcTestResult result = mvcTester.get().uri("/shops/menus")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.responses[0].id", v -> v.assertThat().isEqualTo(menuResponse.id().intValue()))
                .hasPathSatisfying("$.responses[0].name", v -> v.assertThat().isEqualTo(menuResponse.name()))
                .hasPathSatisfying("$.responses[0].price", v -> v.assertThat().isEqualTo(menuResponse.price()))
                .hasPathSatisfying("$.responses[0].description", v -> v.assertThat().isEqualTo(menuResponse.description()))
                .hasPathSatisfying("$.responses[0].image", v -> v.assertThat().isNull());
    }

    @DisplayName("메뉴를 상세 조회 한다.")
    @Test
    void getMenu() {
        var menuId = 1L;
        var menuResponse = new MenuResponse(1L, "menu1", 10000, "description1", "image");
        given(menuQueryService.getMenu(menuId)).willReturn(menuResponse);

        MvcTestResult result = mvcTester.get().uri("/shops/menus/{menuId}", menuId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPathSatisfying("$.id", v -> v.assertThat().isEqualTo(menuResponse.id().intValue()))
                .hasPathSatisfying("$.name", v -> v.assertThat().isEqualTo(menuResponse.name()))
                .hasPathSatisfying("$.price", v -> v.assertThat().isEqualTo(menuResponse.price()))
                .hasPathSatisfying("$.description", v -> v.assertThat().isEqualTo(menuResponse.description()))
                .hasPathSatisfying("$.image", v -> v.assertThat().isEqualTo(menuResponse.image()));
    }

    @TestMember
    @DisplayName("메뉴 정보를 수정한다.")
    @Test
    void update() throws JsonProcessingException {
        var menuUpdateRequest = new MenuUpdateRequest("updateName", 10000, "updateDescription");
        var requestPart = new MockMultipartFile(
                "request",
                "request.json",
                "application/json",
                objectMapper.writeValueAsString(menuUpdateRequest).getBytes()
        );

        doNothing().when(menuCommandService).update(any());

        MvcResult result = mvcTester.perform(multipart(HttpMethod.PATCH, "/shops/menus/{menuId}", 1L)
                .file(requestPart)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(objectMapper.writeValueAsString(menuUpdateRequest))
        ).getMvcResult();

        assertThat(result.getResponse().getStatus()).isEqualTo(204);
    }

    @TestMember
    @DisplayName("메뉴를 삭제 한다.")
    @Test
    void delete() {
        doNothing().when(menuCommandService).delete(any(), any());

        MvcTestResult result = mvcTester.delete().uri("/shops/menus/{menuId}", 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .exchange();

        assertThat(result)
                .hasStatus(HttpStatus.NO_CONTENT)
                .apply(print());
    }

}