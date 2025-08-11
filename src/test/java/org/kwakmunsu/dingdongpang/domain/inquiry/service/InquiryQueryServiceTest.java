package org.kwakmunsu.dingdongpang.domain.inquiry.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.InquiryFilter;
import org.kwakmunsu.dingdongpang.domain.inquiry.repository.InquiryRepository;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.InquiryListResponse;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.InquiryReadServiceRequest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@SpringBootTest
record InquiryQueryServiceTest(
        InquiryQueryService inquiryQueryService,
        InquiryRepository inquiryRepository
) {

    @DisplayName("매장 문의 내용 전체를 조회힌다.")
    @Test
    void getInquiryListByGeneral() {
        List<Inquiry> inquires = List.of(
                Inquiry.create(1L, 2L, "testQuestion1"),
                Inquiry.create(1L, 3L, "testQuestion2"),
                Inquiry.create(1L, 4L, "testQuestion3")
        );
        inquires.forEach(inquiryRepository::save);

        var request = new InquiryReadServiceRequest(InquiryFilter.GENERAL, 1L, 2L);

        InquiryListResponse response = inquiryQueryService.getInquiryList(request);

        assertThat(response.responses()).hasSize(3);
    }

    @DisplayName("해당 매장의 내가 문의한 것만 조회힌다.")
    @Test
    void getInquiryListByMy() {
        List<Inquiry> inquires = List.of(
                Inquiry.create(1L, 2L, "testQuestion1"),
                Inquiry.create(1L, 2L, "testQuestion2"),
                Inquiry.create(1L, 4L, "testQuestion3")
        );
        inquires.forEach(inquiryRepository::save);

        var request = new InquiryReadServiceRequest(InquiryFilter.MY, 1L, 2L);

        InquiryListResponse response = inquiryQueryService.getInquiryList(request);

        assertThat(response.responses()).hasSize(2);
    }

    @DisplayName("문의가 없을 시 빈값이 반환된다.")
    @Test
    void getInquiryListToEmpty() {
        var request = new InquiryReadServiceRequest(InquiryFilter.GENERAL, 1L, 2L);

        InquiryListResponse response = inquiryQueryService.getInquiryList(request);

        assertThat(response.responses()).isEmpty();
    }

}