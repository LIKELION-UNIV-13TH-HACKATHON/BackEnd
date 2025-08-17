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
import org.kwakmunsu.dingdongpang.domain.inquiry.controller.dto.InquiryAnswerRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.controller.dto.InquiryModifyRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.controller.dto.InquiryRegisterRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.InquiryFilter;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.request.InquiryAnswerServiceRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.request.InquiryDeleteServiceRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.request.InquiryReadServiceRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.request.InquiryRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.response.InquiryByMerchantResponse;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.response.InquiryListByMerchantResponse;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.response.InquiryListResponse;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.response.InquiryResponse;
import org.kwakmunsu.dingdongpang.global.TestMember;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MvcTestResult;


class InquiryControllerTest extends ControllerTestSupport {

    @TestMember
    @DisplayName("문의 등록을 한다.")
    @Test
    void register() throws JsonProcessingException {
        var request = new InquiryRegisterRequest("testTitle", "testQuestion");
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
        var request = new InquiryRegisterRequest("", "");
        String jsonToString = objectMapper.writeValueAsString(request);

        assertThat(mvcTester.post().uri("/shops/{shopId}/inquiries", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString))
                .hasStatus(HttpStatus.BAD_REQUEST)
                .apply(print());

    }

    @TestMember
    @DisplayName("고객이 문의 목록을 조회한다.")
    @Test
    void getInquiryListByCustomer() {
        var inquiryPreviewResponse = InquiryResponse.builder()
                .inquiryId(1L)
                .question("question")
                .answer("answer")
                .createdAt(dateTimeToString(LocalDateTime.now()))
                .build();
        var inquiryListResponse = new InquiryListResponse(List.of(inquiryPreviewResponse));
        given(inquiryQueryService.getInquiryList(any(InquiryReadServiceRequest.class))).willReturn(inquiryListResponse);

        MvcTestResult result = mvcTester.get().uri("/shops/{shopId}/inquiries/customers", 1L)
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
        assertThat(mvcTester.get().uri("/shops/{shopId}/inquiries/customers", 1L)
                .param("filter", "invalidFilter")
                .contentType(MediaType.APPLICATION_JSON))
                .hasStatus(HttpStatus.BAD_REQUEST)
                .apply(print());
    }

    @TestMember
    @DisplayName("상인이 문의 목록을 조회한다.")
    @Test
    void getInquiryListByMerchant() {
        var inquiryByMerchantResponse = InquiryByMerchantResponse.builder()
                .inquiryId(1L)
                .question("question")
                .answer("answer")
                .createdAt(dateTimeToString(LocalDateTime.now()))
                .build();
        var inquiryListResponse = new InquiryListByMerchantResponse(List.of(inquiryByMerchantResponse));

        given(inquiryQueryService.getInquiryListByMerchant(any())).willReturn(inquiryListResponse);

        MvcTestResult result = mvcTester.get().uri("/shops/inquiries/merchants", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .apply(print())
                .bodyJson()
                .hasPathSatisfying("$.responses[0].inquiryId", v -> v.assertThat().isEqualTo(inquiryByMerchantResponse.inquiryId().intValue()))
                .hasPathSatisfying("$.responses[0].question", v -> v.assertThat().isEqualTo(inquiryByMerchantResponse.question()))
                .hasPathSatisfying("$.responses[0].answer", v -> v.assertThat().isEqualTo(inquiryByMerchantResponse.answer()))
                .hasPathSatisfying("$.responses[0].createdAt", v -> v.assertThat().isEqualTo(inquiryByMerchantResponse.createdAt()));
    }

    @TestMember
    @DisplayName("문의 답변을 등록한다.")
    @Test
    void registerAnswer() throws JsonProcessingException {
        var request = new InquiryAnswerRequest("testAnswer");
        String jsonToString = objectMapper.writeValueAsString(request);

        assertThat(mvcTester.post().uri("/shops/{shopId}/inquiries/{inquiryId}/answer", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString))
                .hasStatusOk()
                .apply(print());

        verify(inquiryCommandService).registerAnswer(any(InquiryAnswerServiceRequest.class));
    }

    @TestMember
    @DisplayName("문의 내용을 수정한다.")
    @Test
    void modifyInquiry() throws JsonProcessingException {
        var request = new InquiryModifyRequest("updateTitle", "updateQuestion");
        String jsonToString = objectMapper.writeValueAsString(request);

        assertThat(mvcTester.patch().uri("/shops/inquiries/{inquiryId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString))
                .hasStatus(HttpStatus.NO_CONTENT)
                .apply(print());
    }

    @TestMember
    @DisplayName("문의 내용을 삭제한다")
    @Test
    void delete() {

        assertThat(mvcTester.delete().uri("/shops/{shopId}/inquiries/{inquiryId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .hasStatus(HttpStatus.NO_CONTENT)
                .apply(print());

        verify(inquiryCommandService).delete(any(InquiryDeleteServiceRequest.class));
    }

}