package org.kwakmunsu.dingdongpang.domain.inquiry.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kwakmunsu.dingdongpang.global.util.TimeConverter.dateTimeToString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.ControllerTestSupport;
import org.kwakmunsu.dingdongpang.domain.inquiry.controller.dto.InquiryRegisterRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.InquiryFilter;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.InquiryListResponse;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.InquiryPreviewResponse;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.InquiryRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.global.TestMember;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MvcTestResult;


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

    @TestMember
    @DisplayName("문의 목록을 조회한다.")
    @Test
    void getInquiryList() {
        var inquiryPreviewResponse = InquiryPreviewResponse.builder()
                .inquiryId(1L)
                .question("question")
                .answer("answer")
                .createdAt(dateTimeToString(LocalDateTime.now()))
                .build();
        var inquiryListResponse = new InquiryListResponse(List.of(inquiryPreviewResponse));
        given(inquiryQueryService.getInquiryList(any())).willReturn(inquiryListResponse);

        MvcTestResult result = mvcTester.get().uri("/shops/{shopId}/inquiries", 1L)
                .param("filter", InquiryFilter.GENERAL.name())
                .contentType(MediaType.APPLICATION_JSON)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .apply(print())
                .bodyJson()
                .hasPathSatisfying("$.responses[0].inquiryId", v -> v.assertThat().isEqualTo(inquiryPreviewResponse.inquiryId().intValue()))
                .hasPathSatisfying("$.responses[0].question", v -> v.assertThat().isEqualTo(inquiryPreviewResponse.question()))
                .hasPathSatisfying("$.responses[0].answer", v -> v.assertThat().isEqualTo(inquiryPreviewResponse.answer()))
                .hasPathSatisfying("$.responses[0].createdAt", v -> v.assertThat().isEqualTo(inquiryPreviewResponse.createdAt()));
    }

    @TestMember
    @DisplayName("쿼리 파리미터의 Filter 포맷이 옳바르지 않으면 예외를 반환한다.")
    @Test
    void filaGetInquiryList() {
        assertThat(mvcTester.get().uri("/shops/{shopId}/inquiries", 1L)
                .param("filter", "invalidFilter")
                .contentType(MediaType.APPLICATION_JSON))
                .hasStatus(HttpStatus.BAD_REQUEST)
                .apply(print());
    }

}