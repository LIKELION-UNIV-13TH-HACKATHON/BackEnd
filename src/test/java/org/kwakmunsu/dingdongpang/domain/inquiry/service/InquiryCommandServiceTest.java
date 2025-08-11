package org.kwakmunsu.dingdongpang.domain.inquiry.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.dingdongpang.domain.inquiry.repository.InquiryRepository;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.InquiryAnswerServiceRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.InquiryRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.member.entity.Member;
import org.kwakmunsu.dingdongpang.domain.member.repository.MemberRepository;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.ShopRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.kwakmunsu.dingdongpang.domain.shop.service.ShopCommandService;
import org.kwakmunsu.dingdongpang.global.GeoFixture;
import org.kwakmunsu.dingdongpang.global.exception.ForbiddenException;
import org.kwakmunsu.dingdongpang.global.exception.NotFoundException;
import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
record InquiryCommandServiceTest(
        InquiryCommandService inquiryCommandService,
        ShopCommandService shopCommandService,
        ShopRepository shopRepository,
        InquiryRepository inquiryRepository,
        MemberRepository memberRepository
) {

    @DisplayName("문의를 등록한다.")
    @Test
    void register() {
        var author = Member.createMember("email@gmail.com", "nickname", "12345");
        memberRepository.save(author);

        var shopRegisterServiceRequest = getShopRegisterServiceRequest();
        var point = GeoFixture.createPoint(1.2, 2.3);
        shopCommandService.register(shopRegisterServiceRequest, point, 1L);

        var shop = shopRepository.findByMerchantId(1L);
        var inquiryRegisterServiceRequest = new InquiryRegisterServiceRequest("testQuestion", shop.getId(), author.getId());

        inquiryCommandService.register(inquiryRegisterServiceRequest);
        List<Inquiry> inquiries = inquiryRepository.findByShopId(shop.getId());

        assertThat(inquiries).hasSize(1);
        assertThat(inquiries.getFirst())
                .extracting(
                        Inquiry::getQuestion,
                        Inquiry::getShopId,
                        Inquiry::getAuthor
                )
                .containsExactly(
                        inquiryRegisterServiceRequest.question(),
                        inquiryRegisterServiceRequest.shopId(),
                        author
                );
    }

    @DisplayName("문의를 등록할 매장이 존재하지 않을 경우 예외를 반환한다.")
    @Test
    void failRegister() {
        var inquiryRegisterServiceRequest = new InquiryRegisterServiceRequest("testQuestion", -999L, 1L);

        assertThatThrownBy(() -> inquiryCommandService.register(inquiryRegisterServiceRequest))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("문의에 대한 답변을 생성한다.")
    @Test
    void registerAnswer() {
        var author = Member.createMember("email@gmail.com", "nickname", "12345");
        memberRepository.save(author);
        var merchantId = 1L;
        var shopRegisterServiceRequest = getShopRegisterServiceRequest();
        var point = GeoFixture.createPoint(1.2, 2.3);
        shopCommandService.register(shopRegisterServiceRequest, point, merchantId);

        var shop = shopRepository.findByMerchantId(merchantId);
        var inquiryRegisterServiceRequest = new InquiryRegisterServiceRequest("testQuestion", shop.getId(), author.getId());
        inquiryCommandService.register(inquiryRegisterServiceRequest);

        List<Inquiry> inquiries = inquiryRepository.findByShopIdAndAuthorId(shop.getId(), author.getId());
        Long inquiryId = inquiries.getFirst().getId();
        var answerServiceRequest = new InquiryAnswerServiceRequest("answer", shop.getId(), inquiryId, merchantId);

        inquiryCommandService.registerAnswer(answerServiceRequest);

        var inquiry = inquiryRepository.findById(inquiryId);
        assertThat(inquiry.getAnswer()).isEqualTo(answerServiceRequest.answer());
    }

    @DisplayName("답변 등록시 해당 답변에 대한 문의가 존재하지 않는다.")
    @Test
    void failRegisterAnswerWhenNotExistsInquiry() {
        var answerServiceRequest = new InquiryAnswerServiceRequest("answer", 999L, 999L, 999L);

        assertThatThrownBy(() -> inquiryCommandService.registerAnswer(answerServiceRequest))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("매장 관리자가 아니면 답변을 등록할 수 없다.")
    @Test
    void failRegisterAnswerInvalidMember() {
        var author = Member.createMember("email@gmail.com", "nickname", "12345");
        memberRepository.save(author);
        var merchantId = 1L;
        var shopRegisterServiceRequest = getShopRegisterServiceRequest();
        var point = GeoFixture.createPoint(1.2, 2.3);
        shopCommandService.register(shopRegisterServiceRequest, point, merchantId);

        var shop = shopRepository.findByMerchantId(merchantId);
        var inquiryRegisterServiceRequest = new InquiryRegisterServiceRequest("testQuestion", shop.getId(), author.getId());
        inquiryCommandService.register(inquiryRegisterServiceRequest);

        List<Inquiry> inquiries = inquiryRepository.findByShopIdAndAuthorId(shop.getId(), author.getId());
        Long inquiryId = inquiries.getFirst().getId();
        var answerServiceRequest = new InquiryAnswerServiceRequest("answer", shop.getId(), inquiryId, 999L);

        assertThatThrownBy(() -> inquiryCommandService.registerAnswer(answerServiceRequest))
                .isInstanceOf(ForbiddenException.class);
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