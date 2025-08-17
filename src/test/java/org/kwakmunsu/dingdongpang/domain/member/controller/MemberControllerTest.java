package org.kwakmunsu.dingdongpang.domain.member.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.ControllerTestSupport;
import org.kwakmunsu.dingdongpang.domain.auth.controller.dto.FcmTokenRequest;
import org.kwakmunsu.dingdongpang.domain.member.controller.dto.CustomerRegisterRequest;
import org.kwakmunsu.dingdongpang.domain.member.controller.dto.MerchantRegisterRequest;
import org.kwakmunsu.dingdongpang.domain.member.controller.dto.OperationTimeRequest;
import org.kwakmunsu.dingdongpang.domain.member.controller.dto.ShopRegisterRequest;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.CheckNicknameResponse;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.kwakmunsu.dingdongpang.global.TestMember;
import org.kwakmunsu.dingdongpang.global.exception.DuplicationException;
import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;

class MemberControllerTest extends ControllerTestSupport {

    @TestMember
    @DisplayName("고객 등록을 한다.")
    @Test
    void register() throws JsonProcessingException {
        var request = new CustomerRegisterRequest("new-nickname");
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
        var request = new CustomerRegisterRequest("");
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
    @DisplayName("상인 회원을 등록한다.")
    @Test
    void registerMerchant() throws IOException {
        var shopRegisterRequest = getShopRegisterRequest();
        var request = new MerchantRegisterRequest("nickname", "123456", "kwak", shopRegisterRequest);

        var mainImage = new MockMultipartFile(
                "mainImage",
                "profile.jpg",
                "image/jpeg",
                "profile image content".getBytes()
        );
        var imageFile1 = new MockMultipartFile(
                "imageFiles",
                "profile.jpg",
                "image/jpeg",
                "profile image content".getBytes()
        );
        var imageFile2 = new MockMultipartFile(
                "imageFiles",
                "profile.jpg",
                "image/jpeg",
                "profile image content".getBytes()
        );
        var requestPart = new MockMultipartFile(
                "request",
                "request.json",
                "application/json",
                objectMapper.writeValueAsString(request).getBytes()
        );

        MvcResult result = mvcTester.perform(multipart("/members/merchants")
                .file(requestPart)
                .file(mainImage)
                .file(imageFile1)
                .file(imageFile2)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(objectMapper.writeValueAsString(request))

        ).getMvcResult();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
    }

    @TestMember
    @DisplayName("상인 회원을 등록한다.")
    @Test
    void failRegisterMerchant() throws IOException {
        var shopRegisterRequest = getShopRegisterRequest();
        var request = new MerchantRegisterRequest("nickname", "123456", "kwak", shopRegisterRequest);

        var requestPart = new MockMultipartFile(
                "request",
                "request.json",
                "application/json",
                objectMapper.writeValueAsString(request).getBytes()
        );
        doThrow(new DuplicationException(ErrorStatus.DUPLICATE_MERCHANT))
                .when(merchantOnboardingService)
                .register(any());
        MvcResult result = mvcTester.perform(multipart("/members/merchants")
                .file(requestPart)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(objectMapper.writeValueAsString(request))

        ).getMvcResult();

        assertThat(result.getResponse().getStatus()).isEqualTo(409);
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

    private ShopRegisterRequest getShopRegisterRequest() {
        OperationTimeRequest mondayOperation = new OperationTimeRequest(
                DayOfWeek.MONDAY,
                "09:00",
                "22:00",
                false
        );
        OperationTimeRequest tuesdayOperation = new OperationTimeRequest(
                DayOfWeek.TUESDAY,
                "09:00",
                "22:00",
                false
        );

        return new ShopRegisterRequest(
                "My Shop",                                  // shopName
                ShopType.FOOD,                              // shopType (예: enum)
                "010-1234-5678",                            // shopPhoneNumber
                "경기도 광주시 경충대로1461번길 12-4 코오롱 세이브 프라자 202호", // address
                List.of(mondayOperation, tuesdayOperation)
        );

    }
}