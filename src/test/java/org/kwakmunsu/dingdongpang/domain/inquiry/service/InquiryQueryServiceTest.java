package org.kwakmunsu.dingdongpang.domain.inquiry.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.InquiryFilter;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.InquiryStatus;
import org.kwakmunsu.dingdongpang.domain.inquiry.repository.InquiryRepository;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.request.InquiryReadByMerchantServiceRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.request.InquiryReadServiceRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.response.InquiryListByMerchantResponse;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.response.InquiryListResponse;
import org.kwakmunsu.dingdongpang.domain.member.entity.Member;
import org.kwakmunsu.dingdongpang.domain.member.repository.MemberRepository;
import org.kwakmunsu.dingdongpang.domain.shop.ShopFixture;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.kwakmunsu.dingdongpang.domain.shop.service.ShopCommandService;
import org.kwakmunsu.dingdongpang.global.GeoFixture;
import org.kwakmunsu.dingdongpang.global.exception.NotFoundException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@SpringBootTest
record InquiryQueryServiceTest(
        InquiryQueryService inquiryQueryService,
        ShopCommandService shopCommandService,
        ShopRepository shopRepository,
        InquiryRepository inquiryRepository,
        MemberRepository memberRepository
) {

    @DisplayName("매장 문의 내용 전체를 조회힌다.")
    @Test
    void getInquiryListByMerchantByGeneral() {
        var author = Member.createMember("email@gmail.com", "nickname", "12345");
        memberRepository.save(author);

        List<Inquiry> inquires = List.of(
                Inquiry.create(1L, author, "testTitle", "testQuestion1"),
                Inquiry.create(1L, author, "testTitle", "testQuestion2"),
                Inquiry.create(1L, author, "testTitle", "testQuestion3")
        );
        inquires.forEach(inquiryRepository::save);

        var request = new InquiryReadServiceRequest(InquiryFilter.GENERAL, 1L, author.getId());

        InquiryListResponse response = inquiryQueryService.getInquiryList(request);

        assertThat(response.responses()).hasSize(3);
    }

    @DisplayName("해당 매장의 내가 문의한 것만 조회힌다.")
    @Test
    void getInquiryListByMerchantByMy() {
        var author = Member.createMember("email@gmail.com", "nickname", "12345");
        var author2 = Member.createMember("emaiasdl@gmail.com", "nweickname", "123fds45");
        memberRepository.save(author);
        memberRepository.save(author2);

        List<Inquiry> inquires = List.of(
                Inquiry.create(1L, author, "testTitle", "testQuestion1"),
                Inquiry.create(1L, author, "testTitle", "testQuestion2"),
                Inquiry.create(1L, author2, "testTitle", "testQuestion3")
        );
        inquires.forEach(inquiryRepository::save);

        var request = new InquiryReadServiceRequest(InquiryFilter.MY, 1L, author.getId());

        InquiryListResponse response = inquiryQueryService.getInquiryList(request);

        assertThat(response.responses()).hasSize(2);
    }

    @DisplayName("상인 매장의 문의 목록을 조회한다.")
    @Test
    void getInquiryListByMerchantByMerchants() {
        var merchantId = 1L;
        var shopRegisterServiceRequest = ShopFixture.getShopRegisterServiceRequest();
        var point = GeoFixture.createPoint(1.2, 2.3);
        shopCommandService.register(shopRegisterServiceRequest, point, merchantId);

        var author = Member.createMember("email@gmail.com", "nickname", "12345");
        memberRepository.save(author);

        var shop = shopRepository.findByMerchantId(merchantId);
        List<Inquiry> inquires = List.of(
                Inquiry.create(shop.getId(), author, "testTitle", "testQuestion1"),
                Inquiry.create(shop.getId(), author, "testTitle", "testQuestion2"),
                Inquiry.create(shop.getId(), author, "testTitle", "testQuestion3")
        );
        inquires.forEach(inquiryRepository::save);
        inquires.getFirst().updateStatusToCompleted();
        var request = new InquiryReadByMerchantServiceRequest(merchantId, InquiryStatus.COMPLETED);
        InquiryListByMerchantResponse response = inquiryQueryService.getInquiryListByMerchant(request);
        assertThat(response.responses()).hasSize(1);
    }

    @DisplayName("매장이 없는 회원이 자신 매장의 문의 목록을 조회할 경우 예외를 던진다.")
    @Test
    void failGetInquiryListByMerchantByMerchants() {
        var merchantId = 1L;
        var author = Member.createMember("email@gmail.com", "nickname", "12345");
        memberRepository.save(author);

        List<Inquiry> inquires = List.of(
                Inquiry.create(1L, author, "testTitle", "testQuestion1"),
                Inquiry.create(1L, author, "testTitle", "testQuestion2"),
                Inquiry.create(1L, author, "testTitle", "testQuestion3")
        );
        inquires.forEach(inquiryRepository::save);
        var request = new InquiryReadByMerchantServiceRequest(merchantId, InquiryStatus.PADDING);
        assertThatThrownBy(() -> inquiryQueryService.getInquiryListByMerchant(request))
                .isInstanceOf(NotFoundException.class);
    }

}