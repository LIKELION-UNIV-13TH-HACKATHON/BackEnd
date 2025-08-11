package org.kwakmunsu.dingdongpang.domain.inquiry.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.ControllerTestSupport;
import org.kwakmunsu.dingdongpang.domain.inquiry.controller.dto.InquiryRegisterRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.InquiryRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.global.TestMember;
import org.kwakmunsu.dingdongpang.global.annotation.AuthMember;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


class InquiryControllerTest extends ControllerTestSupport {

    @TestMember
    @DisplayName("문의 등록을 한다.")
    @Test
    void register() throws JsonProcessingException {
        var request = new InquiryRegisterRequest("testQuestion");
        String jsonToString = objectMapper.writeValueAsString(request);

        assertThat(mvcTester.post().uri("/shops/{shopId}/inquiries", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString))
                .hasStatusOk()
                .apply(print());

        verify(inquiryCommandService).register(any(InquiryRegisterServiceRequest.class));
    }

    @TestMember
    @DisplayName("문의 내용 없이 요청 시 예외를 반환한다.")
    @Test
    void failRegister() throws JsonProcessingException {
        var request = new InquiryRegisterRequest("");
        String jsonToString = objectMapper.writeValueAsString(request);

        assertThat(mvcTester.post().uri("/shops/{shopId}/inquiries", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString))
                .hasStatus(HttpStatus.BAD_REQUEST)
                .apply(print());

    }


    @PostMapping("/{shopId}/inquiries")
    public ResponseEntity<Void> register(
            @Valid @RequestBody InquiryRegisterRequest request,
            @PathVariable Long shopId,
            @AuthMember Long memberId
    ) {
        inquiryCommandService.register(request.toServiceRequest(shopId, memberId));

        return ResponseEntity.ok().build();
    }
}