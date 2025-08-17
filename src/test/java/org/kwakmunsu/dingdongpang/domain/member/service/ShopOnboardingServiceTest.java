package org.kwakmunsu.dingdongpang.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.member.entity.Member;
import org.kwakmunsu.dingdongpang.domain.member.repository.MemberRepository;
import org.kwakmunsu.dingdongpang.domain.shop.ShopFixture;
import org.kwakmunsu.dingdongpang.domain.shop.service.ShopOnboardingService;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.kwakmunsu.dingdongpang.domain.shop.service.ShopCommandService;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.request.ShopUpdateServiceRequest;
import org.kwakmunsu.dingdongpang.global.exception.DuplicationException;
import org.kwakmunsu.dingdongpang.global.exception.NotFoundException;
import org.kwakmunsu.dingdongpang.infrastructure.openapi.BusinessRegisterProvider;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
record ShopOnboardingServiceTest(
        ShopOnboardingService shopOnboardingService,
        MemberCommandService memberCommandService,
        ShopCommandService shopCommandService,
        ShopRepository shopRepository,
        MemberRepository memberRepository,
        BusinessRegisterProvider businessRegisterProvider
) {

//    @Disabled("IP 주소 상시 변경으로 비활성화")
    @DisplayName("온보딩 매장 관련 정보들을 등록한다.")
    @Test
    void register() {
        var guest = Member.createGuest("email@naver.com", "nickname", "12345");
        memberRepository.save(guest);
        var request = ShopFixture.getShopRegisterServiceRequest(guest.getId(), "8962801461");

        shopOnboardingService.register(request);

        var shop = shopRepository.findByMerchantId(guest.getId());

        assertThat(shop)
                .extracting(
                        Shop::getAddress,
                        Shop::getBusinessNumber,
                        Shop:: getOwnerName,
                        Shop:: getShopName
                )
                .containsExactly(
                        request.address(),
                        request.businessNumber(),
                        request.ownerName(),
                        request.shopName()
                        );
    }

//    @Disabled("IP 주소 상시 변경으로 비활성화")
    @DisplayName("이미 매장이 등록되어있다면 재등록이 안된다.")
    @Test
    void failRegister() {
        var guest = Member.createGuest("email@naver.com", "nickname", "12345");
        memberRepository.save(guest);
        var request = ShopFixture.getShopRegisterServiceRequest(guest.getId(), "8962801461");

        shopOnboardingService.register(request);
        assertThatThrownBy(() ->   shopOnboardingService.register(request))
            .isInstanceOf(DuplicationException.class);
    }

    @DisplayName("유효하지 않은 사업자 등록 번호일 경우 에러를 던진다.")
    @Test
    void failRegisterWhenInvalidNumber() {
        var guest = Member.createGuest("email@naver.com", "nickname", "12345");
        memberRepository.save(guest);
        var request = ShopFixture.getShopRegisterServiceRequest(guest.getId(), "invalid-number");

        assertThatThrownBy(() ->   shopOnboardingService.register(request))
                .isInstanceOf(NotFoundException.class);
    }
//
//
//    @DisplayName("매장 정보를 업데이트 한다.")
//    @Test
//    void update() throws IOException {
//        var guest = Member.createGuest("email@naver.com", "nickname", "12345");
//        memberRepository.save(guest);
//        var request = getMerchantRegisterServiceRequest(guest.getId(), "8962801461");
//
//        shopOnboardingService.register(request);
//
//        var shop = shopRepository.findByMerchantId(guest.getId());
//        var updateServiceRequest = getMerchantUpdateServiceRequest(guest.getId(), "8962801461");
//
//        shopOnboardingService.update(updateServiceRequest);
//
//        assertThat(shop).extracting(Shop::getShopName,Shop::getMainImage)
//                .containsExactly(updateServiceRequest.shopUpdateServiceRequest().shopName(), null);
//    }



    private MockMultipartFile getMockMultipartFile() throws IOException {
        File image = new File("src/test/resources/test.png");
        return new MockMultipartFile(
                "image",                         // 파라미터 이름
                "test.png",                      // 파일 이름
                "image/png",                    // Content-Type
                new FileInputStream(image)
        );
    }

    private ShopUpdateServiceRequest getShopUpdateServiceRequest(String businessNumber) throws IOException {
        return new ShopUpdateServiceRequest(
                "updateShopName",                                  // shopName
                ShopType.FOOD,                              // shopType (예: enum)
                "010-1234-5678",                            // shopPhoneNumber
                "경기도 광주시 경충대로1461번길 12-4 코오롱 세이브 프라자 202호", // address
                businessNumber,                               // businessNumber
                "홍길동",                                     // ownerName
                null,                                  // mainImage
                List.of(),                    // imageFiles
                List.of()  // operationTimeRequests
        );
    }
}