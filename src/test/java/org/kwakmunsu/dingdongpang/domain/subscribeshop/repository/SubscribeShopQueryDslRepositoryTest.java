package org.kwakmunsu.dingdongpang.domain.subscribeshop.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.kwakmunsu.dingdongpang.domain.shop.service.ShopCommandService;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.request.ShopRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.entity.SubscribeShop;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto.SubscribeShopPreviewResponse;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto.SubscribeShopReadDomainRequest;
import org.kwakmunsu.dingdongpang.infrastructure.geocoding.KakaoGeocodingProvider;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
record SubscribeShopQueryDslRepositoryTest(
        ShopCommandService shopCommandService,
        ShopRepository shopRepository,
        SubscribeShopRepository subscribeShopRepository,
        KakaoGeocodingProvider kakaoGeocodingProvider,
        SubscribeShopQueryDslRepository subscribeShopQueryDslRepository
) {

    @DisplayName("구독한 매장 목록을 조회한다.")
    @Test
    void getSubscribeShop() {
        var address1 = "경기 광주시 경충대로 1411 투썸플레이스";
        var address2 = "경기 광주시 역동로63번길 1";
        var address3 = "경기 광주시 경충대로1481번길 4 1층";

        var registerRequest = getShopRegisterServiceRequest("대훈이네 곱창", "010-1234-5678", "businessNumber1", address1);
        var registerRequest2 = getShopRegisterServiceRequest("문수네 곱창", "010-1234-5679", "businessNumber2", address2);
        var registerRequest3 = getShopRegisterServiceRequest("순재네 막창", "010-1234-5690", "businessNumber3", address3);

        var point1 = kakaoGeocodingProvider.transferToGeocode(address1);
        var point2 = kakaoGeocodingProvider.transferToGeocode(address2);
        var point3 = kakaoGeocodingProvider.transferToGeocode(address3);

        shopCommandService.register(registerRequest, point1, 1L);
        shopCommandService.register(registerRequest2, point2, 2L);
        shopCommandService.register(registerRequest3, point3, 3L);

        long memberId = 99L;
        // 구독
        var shop = shopRepository.findByMerchantId(1L);
        var shop2 = shopRepository.findByMerchantId(3L);
        var subscribeShop = SubscribeShop.create(memberId, shop.getId());
        var subscribeShop2 = SubscribeShop.create(2332L, shop.getId());
        var subscribeShop3 = SubscribeShop.create(memberId, shop2.getId());
        subscribeShopRepository.save(subscribeShop);
        subscribeShopRepository.save(subscribeShop2);
        subscribeShopRepository.save(subscribeShop3);

        var request = SubscribeShopReadDomainRequest.builder()
                .memberId(memberId)
                .longitude(127.271143598307)
                .latitude(37.4013921881645)
                .build();

        var response = subscribeShopQueryDslRepository.getSubscribedShopList(request);

        assertThat(response.responses()).hasSize(2);

        assertThat(response.responses().get(0))
                .extracting(
                        SubscribeShopPreviewResponse::shopId,
                        SubscribeShopPreviewResponse::shopName,
                        SubscribeShopPreviewResponse::subscribeCount
                )
                .containsExactly(
                        shop2.getId(),
                        shop2.getShopName(),
                        1L
                );

        assertThat(response.responses().get(1))
                .extracting(
                        SubscribeShopPreviewResponse::shopId,
                        SubscribeShopPreviewResponse::shopName,
                        SubscribeShopPreviewResponse::subscribeCount
                )
                .containsExactly(shop.getId(), shop.getShopName(), 1L);
    }

    @DisplayName("구독한 매장 목록을 조회하는데 빈 값을 반환한다..")
    @Test
    void getEmptySubscribeShop() {

        var request = SubscribeShopReadDomainRequest.builder()
                .memberId(1L)
                .longitude(127.271143598307)
                .latitude(37.4013921881645)
                .build();

        var response = subscribeShopQueryDslRepository.getSubscribedShopList(request);

        assertThat(response.responses()).isEmpty();
    }

    private ShopRegisterServiceRequest getShopRegisterServiceRequest(
            String shopName,
            String shopPhoneNumber,
            String businessNumber,
            String address
    ) {
        return ShopRegisterServiceRequest.builder()
                .shopName(shopName)
                .shopPhoneNumber(shopPhoneNumber)
                .businessNumber(businessNumber)
                .address(address)
                .ownerName("김계란")
                .shopType(ShopType.FASHION)
                .mainImage(null)
                .merchantId(1L)
                .imageFiles(List.of())
                .operationTimeRequests(List.of())
                .build();// 매장 주소
    }
}