package org.kwakmunsu.dingdongpang.domain.inquiry.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.InquiryStatus;
import org.kwakmunsu.dingdongpang.domain.inquiry.repository.InquiryRepository;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.request.InquiryAnswerServiceRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.request.InquiryDeleteServiceRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.request.InquiryModifyServiceRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.request.InquiryRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.member.entity.Member;
import org.kwakmunsu.dingdongpang.domain.member.repository.MemberRepository;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.kwakmunsu.dingdongpang.domain.shop.service.ShopCommandService;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.request.ShopRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.global.GeoFixture;
import org.kwakmunsu.dingdongpang.global.exception.ForbiddenException;
import org.kwakmunsu.dingdongpang.global.exception.NotFoundException;
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
        var inquiryRegisterServiceRequest = new InquiryRegisterServiceRequest("testTitle", "testQuestion", shop.getId(), author.getId());

        inquiryCommandService.register(inquiryRegisterServiceRequest);
        List<Inquiry> inquiries = inquiryRepository.findByShopId(shop.getId());

        assertThat(inquiries).hasSize(1);
        assertThat(inquiries.getFirst())
                .extracting(
                        Inquiry::getTitle,
                        Inquiry::getQuestion,
                        Inquiry::getShopId,
                        Inquiry::getAuthor
                )
                .containsExactly(
                        inquiryRegisterServiceRequest.title(),
                        inquiryRegisterServiceRequest.question(),
                        inquiryRegisterServiceRequest.shopId(),
                        author
                );
    }

    @DisplayName("문의를 등록할 매장이 존재하지 않을 경우 예외를 반환한다.")
    @Test
    void failRegister() {
        var inquiryRegisterServiceRequest = new InquiryRegisterServiceRequest("testTitle", "testQuestion", -999L, 1L);

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
        var inquiryRegisterServiceRequest = new InquiryRegisterServiceRequest("testTitle", "testQuestion", shop.getId(), author.getId());
        inquiryCommandService.register(inquiryRegisterServiceRequest);

        List<Inquiry> inquiries = inquiryRepository.findByShopIdAndAuthorId(shop.getId(), author.getId());
        Long inquiryId = inquiries.getFirst().getId();
        var answerServiceRequest = new InquiryAnswerServiceRequest("answer", shop.getId(), inquiryId, merchantId);

        inquiryCommandService.registerAnswer(answerServiceRequest);

        var inquiry = inquiryRepository.findById(inquiryId);
        assertThat(inquiry.getAnswer()).isEqualTo(answerServiceRequest.answer());
        assertThat(inquiry.getStatus()).isEqualTo(InquiryStatus.COMPLETED);
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
        var inquiryRegisterServiceRequest = new InquiryRegisterServiceRequest("testTitle", "testQuestion", shop.getId(), author.getId());
        inquiryCommandService.register(inquiryRegisterServiceRequest);

        List<Inquiry> inquiries = inquiryRepository.findByShopIdAndAuthorId(shop.getId(), author.getId());
        Long inquiryId = inquiries.getFirst().getId();
        var answerServiceRequest = new InquiryAnswerServiceRequest("answer", shop.getId(), inquiryId, 999L);

        assertThatThrownBy(() -> inquiryCommandService.registerAnswer(answerServiceRequest))
                .isInstanceOf(ForbiddenException.class);
    }

    @DisplayName("문의 작성자가 문의 내용을 수정한다.")
    @Test
    void modifyQuestion() {
        var author = Member.createMember("email@gmail.com", "nickname", "12345");
        memberRepository.save(author);

        var shopRegisterServiceRequest = getShopRegisterServiceRequest();
        var point = GeoFixture.createPoint(1.2, 2.3);
        shopCommandService.register(shopRegisterServiceRequest, point, 1L);

        var shop = shopRepository.findByMerchantId(1L);
        var inquiryRegisterServiceRequest = new InquiryRegisterServiceRequest("testTitle", "testQuestion", shop.getId(), author.getId());
        inquiryCommandService.register(inquiryRegisterServiceRequest);

        List<Inquiry> inquiries = inquiryRepository.findByShopIdAndAuthorId(shop.getId(), author.getId());
        var inquiryId = inquiries.getFirst().getId();
        // 수정 전 확인
        assertThat(inquiries.getFirst().getQuestion()).isEqualTo(inquiryRegisterServiceRequest.question());

        var inquiryModifyServiceRequest = new InquiryModifyServiceRequest("updateTitle", "updateQuestion", inquiryId, author.getId());
        inquiryCommandService.modifyInquiry(inquiryModifyServiceRequest);

        var inquiry = inquiryRepository.findById(inquiryId);
        // 수정 후
        assertThat(inquiry.getQuestion()).isEqualTo(inquiryModifyServiceRequest.question());
    }

    @DisplayName("문의 작성자가 아닐 경우 문의 내용을 수정할 수 없다.")
    @Test
    void failModifyQuestion() {
        var author = Member.createMember("email@gmail.com", "nickname", "12345");
        memberRepository.save(author);

        var shopRegisterServiceRequest = getShopRegisterServiceRequest();
        var point = GeoFixture.createPoint(1.2, 2.3);
        shopCommandService.register(shopRegisterServiceRequest, point, 1L);

        var shop = shopRepository.findByMerchantId(1L);
        var inquiryRegisterServiceRequest = new InquiryRegisterServiceRequest("testTitle", "testQuestion", shop.getId(), author.getId());
        inquiryCommandService.register(inquiryRegisterServiceRequest);

        List<Inquiry> inquiries = inquiryRepository.findByShopIdAndAuthorId(shop.getId(), author.getId());
        var inquiryId = inquiries.getFirst().getId();
        var inquiryModifyServiceRequest = new InquiryModifyServiceRequest("updateTitle", "updateQuestion", inquiryId, 999L);

        assertThatThrownBy(() -> inquiryCommandService.modifyInquiry(inquiryModifyServiceRequest))
                .isInstanceOf(ForbiddenException.class);
    }

    @DisplayName("작성자거나 매장 관리자일 경우 문의를 삭제한다.")
    @Test
    void deleteInquiry() {
        var merchantId = 1L;
        var author = Member.createMember("email@gmail.com", "nickname", "12345");
        memberRepository.save(author);

        var shopRegisterServiceRequest = getShopRegisterServiceRequest();
        var point = GeoFixture.createPoint(1.2, 2.3);
        shopCommandService.register(shopRegisterServiceRequest, point, merchantId);

        var shop = shopRepository.findByMerchantId(merchantId);
        var inquiryRegisterServiceRequest = new InquiryRegisterServiceRequest("testTitle", "testQuestion", shop.getId(), author.getId());
        inquiryCommandService.register(inquiryRegisterServiceRequest);

        List<Inquiry> inquiries = inquiryRepository.findByShopIdAndAuthorId(shop.getId(), author.getId());
        var inquiryId = inquiries.getFirst().getId();

        var request = new InquiryDeleteServiceRequest(shop.getId(), inquiryId, author.getId()); // merchantId로 해도 통과
        inquiryCommandService.delete(request);

        // 예외가 던져지면 정상적으로 삭제 완료.
        assertThatThrownBy(() -> inquiryRepository.findById(inquiryId))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("작성자거나 매장 관리자아닐 경우 예외를 던진다.")
    @Test
    void failDeleteInquiry() {
        var merchantId = 1L;
        var author = Member.createMember("email@gmail.com", "nickname", "12345");
        memberRepository.save(author);

        var shopRegisterServiceRequest = getShopRegisterServiceRequest();
        var point = GeoFixture.createPoint(1.2, 2.3);
        shopCommandService.register(shopRegisterServiceRequest, point, merchantId);

        var shop = shopRepository.findByMerchantId(merchantId);
        var inquiryRegisterServiceRequest = new InquiryRegisterServiceRequest("testTitle", "testQuestion", shop.getId(), author.getId());
        inquiryCommandService.register(inquiryRegisterServiceRequest);

        List<Inquiry> inquiries = inquiryRepository.findByShopIdAndAuthorId(shop.getId(), author.getId());
        var inquiryId = inquiries.getFirst().getId();

        var request = new InquiryDeleteServiceRequest(shop.getId(), inquiryId, 999L);
        assertThatThrownBy(() -> inquiryCommandService.delete(request))
                .isInstanceOf(ForbiddenException.class);
    }

    private ShopRegisterServiceRequest getShopRegisterServiceRequest() {
        return ShopRegisterServiceRequest.builder()
                .businessNumber("8962801461")    // 사업자 등록 번호
                .ownerName("김계란")              // 대표자명
                .shopName("역전할머니맥주")         // 매장명
                .shopType(ShopType.FASHION)      // 매장 타입 (enum)
                .shopPhoneNumber("010-8742-1234")// 매장 전화번호
                .address("경기도 광주시 경충대로1461번길 12-4 코오롱 세이브 프라자 202호")
                .mainImage(null)
                .merchantId(1L)
                .imageFiles(List.of())
                .operationTimeRequests(List.of())
                .build();// 매장 주소
    }

}