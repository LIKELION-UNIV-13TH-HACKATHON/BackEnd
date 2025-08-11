package org.kwakmunsu.dingdongpang.domain.inquiry.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.InquiryFilter;
import org.kwakmunsu.dingdongpang.domain.inquiry.repository.InquiryRepository;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.InquiryReadServiceRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.response.InquiryListByMerchantResponse;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.response.InquiryListResponse;
import org.kwakmunsu.dingdongpang.domain.member.entity.Member;
import org.kwakmunsu.dingdongpang.domain.member.repository.MemberRepository;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.ShopRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
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
    void getInquiryListByGeneral() {
        var author = Member.createMember("email@gmail.com", "nickname", "12345");
        memberRepository.save(author);

        List<Inquiry> inquires = List.of(
                Inquiry.create(1L, author, "testQuestion1"),
                Inquiry.create(1L, author, "testQuestion2"),
                Inquiry.create(1L, author, "testQuestion3")
        );
        inquires.forEach(inquiryRepository::save);

        var request = new InquiryReadServiceRequest(InquiryFilter.GENERAL, 1L, author.getId());

        InquiryListResponse response = inquiryQueryService.getInquiryList(request);

        assertThat(response.responses()).hasSize(3);
    }

    @DisplayName("해당 매장의 내가 문의한 것만 조회힌다.")
    @Test
    void getInquiryListByMy() {
        var author = Member.createMember("email@gmail.com", "nickname", "12345");
        var author2 = Member.createMember("emaiasdl@gmail.com", "nweickname", "123fds45");
        memberRepository.save(author);
        memberRepository.save(author2);

        List<Inquiry> inquires = List.of(
                Inquiry.create(1L, author, "testQuestion1"),
                Inquiry.create(1L, author, "testQuestion2"),
                Inquiry.create(1L, author2, "testQuestion3")
        );
        inquires.forEach(inquiryRepository::save);

        var request = new InquiryReadServiceRequest(InquiryFilter.MY, 1L, author.getId());

        InquiryListResponse response = inquiryQueryService.getInquiryList(request);

        assertThat(response.responses()).hasSize(2);
    }

    @DisplayName("상인 매장의 문의 목록을 조회한다.")
    @Test
    void getInquiryListByMerchants() {
        var merchantId = 1L;
        var shopRegisterServiceRequest = getShopRegisterServiceRequest();
        var point = GeoFixture.createPoint(1.2, 2.3);
        shopCommandService.register(shopRegisterServiceRequest, point, merchantId);

        var author = Member.createMember("email@gmail.com", "nickname", "12345");
        memberRepository.save(author);

        var shop = shopRepository.findByMerchantId(merchantId);
        List<Inquiry> inquires = List.of(
                Inquiry.create(shop.getId(), author, "testQuestion1"),
                Inquiry.create(shop.getId(), author, "testQuestion2"),
                Inquiry.create(shop.getId(), author, "testQuestion3")
        );
        inquires.forEach(inquiryRepository::save);

        InquiryListByMerchantResponse response = inquiryQueryService.getInquiryList(merchantId);
        assertThat(response.responses()).hasSize(3);
    }

    @DisplayName("매장이 없는 회원이 자신 매장의 문의 목록을 조회할 경우 예외를 던진다.")
    @Test
    void failGetInquiryListByMerchants() {
        var merchantId = 1L;
        var author = Member.createMember("email@gmail.com", "nickname", "12345");
        memberRepository.save(author);

        List<Inquiry> inquires = List.of(
                Inquiry.create(1L, author, "testQuestion1"),
                Inquiry.create(1L, author, "testQuestion2"),
                Inquiry.create(1L, author, "testQuestion3")
        );
        inquires.forEach(inquiryRepository::save);

        assertThatThrownBy(() -> inquiryQueryService.getInquiryList(merchantId))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("문의가 없을 시 빈값이 반환된다.")
    @Test
    void getInquiryListToEmpty() {
        var request = new InquiryReadServiceRequest(InquiryFilter.GENERAL, 1L, 2L);

        InquiryListResponse response = inquiryQueryService.getInquiryList(request);

        assertThat(response.responses()).isEmpty();
    }

    private ShopRegisterServiceRequest getShopRegisterServiceRequest() {
        return new ShopRegisterServiceRequest(
                "My Shop",                                  // shopName
                ShopType.FOOD,                              // shopType (예: enum)
                "010-1234-5678",                            // shopPhoneNumber
                "서울특별시 강남구 역삼동 123-45",                 // address
                "1234567890",                               // businessNumber
                "홍길동",                                     // ownerName
                null,                                     // mainImage
                List.of(),                    // imageFiles
                List.of()  // operationTimeRequests
        );
    }
}