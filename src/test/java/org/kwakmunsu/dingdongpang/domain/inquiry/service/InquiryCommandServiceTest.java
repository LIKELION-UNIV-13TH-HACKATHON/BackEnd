package org.kwakmunsu.dingdongpang.domain.inquiry.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.dingdongpang.domain.inquiry.repository.InquiryRepository;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.InquiryRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.ShopRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.kwakmunsu.dingdongpang.domain.shop.service.ShopCommandService;
import org.kwakmunsu.dingdongpang.global.GeoFixture;
import org.kwakmunsu.dingdongpang.global.exception.NotFoundException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
record InquiryCommandServiceTest(
        InquiryCommandService inquiryCommandService,
        ShopCommandService shopCommandService,
        ShopRepository shopRepository,
        InquiryRepository inquiryRepository
) {

    @DisplayName("문의를 등록한다.")
    @Test
    void register() {
        var shopRegisterServiceRequest = getShopRegisterServiceRequest();
        var point = GeoFixture.createPoint(1.2, 2.3);
        shopCommandService.register(shopRegisterServiceRequest, point, 1L);

        var shop = shopRepository.findByMemberId(1L);
        var inquiryRegisterServiceRequest = new InquiryRegisterServiceRequest("testQuestion", shop.getId(), 1L);

        inquiryCommandService.register(inquiryRegisterServiceRequest);
        List<Inquiry> inquiries = inquiryRepository.findByShopId(shop.getId());

        assertThat(inquiries).hasSize(1);
        assertThat(inquiries.getFirst())
                .extracting(
                        Inquiry::getQuestion,
                        Inquiry::getShopId,
                        Inquiry::getAuthorId
                )
                .containsExactly(
                        inquiryRegisterServiceRequest.question(),
                        inquiryRegisterServiceRequest.shopId(),
                        inquiryRegisterServiceRequest.memberId()
                );
    }

    @DisplayName("문의를 등록할 매장이 존재하지 않을 경우 예외를 반환한다.")
    @Test
    void failRegister() {
        var inquiryRegisterServiceRequest = new InquiryRegisterServiceRequest("testQuestion", -999L, 1L);

        assertThatThrownBy(() -> inquiryCommandService.register(inquiryRegisterServiceRequest))
            .isInstanceOf(NotFoundException.class);
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