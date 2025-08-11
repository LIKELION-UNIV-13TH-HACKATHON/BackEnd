package org.kwakmunsu.dingdongpang.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.member.entity.Member;
import org.kwakmunsu.dingdongpang.domain.member.repository.MemberRepository;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.MerchantRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.OperationTimeServiceRequest;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.ShopRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.kwakmunsu.dingdongpang.domain.shop.service.ShopCommandService;
import org.kwakmunsu.dingdongpang.global.exception.DuplicationException;
import org.kwakmunsu.dingdongpang.global.exception.NotFoundException;
import org.kwakmunsu.dingdongpang.infrastructure.openapi.BusinessRegisterProvider;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@SpringBootTest
record MerchantOnboardingServiceTest(
        MerchantOnboardingService merchantOnboardingService,
        MemberCommandService memberCommandService,
        ShopCommandService shopCommandService,
        ShopRepository shopRepository,
        MemberRepository memberRepository,
        BusinessRegisterProvider businessRegisterProvider
) {

    @Disabled("IP 주소 상시 변경으로 비활성화")
    @DisplayName("상인 회원을 등록한다.")
    @Test
    void register() throws IOException {
        var guest = Member.createGuest("email@naver.com", "nickname", "12345");
        memberRepository.save(guest);
        var request = getMerchantRegisterServiceRequest(guest.getId(), "8962801461");

        merchantOnboardingService.register(request);

        var shop = shopRepository.findByMerchantId(guest.getId());

        var shopRegisterRequest = request.shopRegisterServiceRequest();
        assertThat(shop)
                .extracting(
                        Shop::getAddress,
                        Shop::getBusinessNumber,
                        Shop:: getOwnerName,
                        Shop:: getShopName
                )
                .containsExactly(
                        shopRegisterRequest.address(),
                        shopRegisterRequest.businessNumber(),
                        shopRegisterRequest.ownerName(),
                        shopRegisterRequest.shopName()
                        );
    }

    @Disabled("IP 주소 상시 변경으로 비활성화")
    @DisplayName("이미 상인으로 등록된 회원일 경우 재등록이 안된다.")
    @Test
    void failRegister() throws IOException {
        var guest = Member.createGuest("email@naver.com", "nickname", "12345");
        memberRepository.save(guest);
        var request = getMerchantRegisterServiceRequest(guest.getId(), "8962801461");

        merchantOnboardingService.register(request);
        assertThatThrownBy(() ->   merchantOnboardingService.register(request))
            .isInstanceOf(DuplicationException.class);
    }

    @DisplayName("유효하지 않은 사업자 등록 번호일 경우 에러를 던진다.")
    @Test
    void failRegisterWhenInvalidNumber() throws IOException {
        var guest = Member.createGuest("email@naver.com", "nickname", "12345");
        memberRepository.save(guest);
        var request = getMerchantRegisterServiceRequest(guest.getId(), "invalid-number");

        assertThatThrownBy(() ->   merchantOnboardingService.register(request))
                .isInstanceOf(NotFoundException.class);
    }

    private MockMultipartFile getMockMultipartFile() throws IOException {
        File image = new File("src/test/resources/test.png");
        return new MockMultipartFile(
                "image",                         // 파라미터 이름
                "test.png",                      // 파일 이름
                "image/png",                    // Content-Type
                new FileInputStream(image)
        );
    }

    private MerchantRegisterServiceRequest getMerchantRegisterServiceRequest(Long memberId, String businessNumber) throws IOException {
        return MerchantRegisterServiceRequest.builder()
                .nickname("test")
                .businessRegistrationNumber(businessNumber)
                .memberId(memberId)
                .shopRegisterServiceRequest(getShopRegisterServiceRequest(businessNumber))
                .build();
    }

    private ShopRegisterServiceRequest getShopRegisterServiceRequest(String businessNumber) throws IOException {
        MultipartFile mainImage = getMockMultipartFile();
        MultipartFile image1 = getMockMultipartFile();
        MultipartFile image2 = getMockMultipartFile();

        OperationTimeServiceRequest mondayOperation = new OperationTimeServiceRequest(
                DayOfWeek.MONDAY,
                "09:00",
                "22:00",
                false
        );
        OperationTimeServiceRequest tuesdayOperation = new OperationTimeServiceRequest(
                DayOfWeek.TUESDAY,
                "09:00",
                "22:00",
                false
        );

        return new ShopRegisterServiceRequest(
                "My Shop",                                  // shopName
                ShopType.FOOD,                              // shopType (예: enum)
                "010-1234-5678",                            // shopPhoneNumber
                "경기도 광주시 경충대로1461번길 12-4 코오롱 세이브 프라자 202호", // address
                businessNumber,                               // businessNumber
                "홍길동",                                     // ownerName
                mainImage,                                  // mainImage
                List.of(image1, image2),                    // imageFiles
                List.of(mondayOperation, tuesdayOperation)  // operationTimeRequests
        );

    }
}