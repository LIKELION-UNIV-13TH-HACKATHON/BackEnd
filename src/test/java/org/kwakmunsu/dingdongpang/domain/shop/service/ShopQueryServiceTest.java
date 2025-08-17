package org.kwakmunsu.dingdongpang.domain.shop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.DayOfWeek;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shopoperation.ShopOperationTimeRepository;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.request.OperationTimeServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.request.ShopNearbySearchServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.request.ShopRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.response.ShopNearbySearchListResponse;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.response.ShopResponse;
import org.kwakmunsu.dingdongpang.domain.shopimage.repository.ShopImageRepository;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.SubscribeShopRepository;
import org.kwakmunsu.dingdongpang.global.GeoFixture;
import org.kwakmunsu.dingdongpang.global.exception.NotFoundException;
import org.kwakmunsu.dingdongpang.infrastructure.geocoding.KakaoGeocodingProvider;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@SpringBootTest
record ShopQueryServiceTest(
        ShopQueryService shopQueryService,
        ShopCommandService shopCommandService,
        ShopRepository shopRepository,
        ShopImageRepository shopImageRepository,
        ShopOperationTimeRepository shopOperationTimeRepository,
        SubscribeShopRepository subscribeShopRepository,
        KakaoGeocodingProvider kakaoGeocodingProvider
) {

    @DisplayName("매장 정보를 조회한다.")
    @Test
    void getShop() {
        var registerRequest = getShopRegisterServiceRequest("title", "010-1234-5678","12343","경기도 광주시 경충대로 1480번길");
        var point = GeoFixture.createPoint(1.2, 2.3);
        long merchantId = 1L;
        long memberId = 2L;
        shopCommandService.register(registerRequest, point, merchantId);

        var shop = shopRepository.findByMerchantId(merchantId);

        ShopResponse response = shopQueryService.getShop(shop.getId(), memberId);

        assertThat(response)
                .extracting(
                        ShopResponse::shopName, ShopResponse::shopType, ShopResponse::ownerName,
                        ShopResponse::businessNumber, ShopResponse::address,
                        ShopResponse::shopTellNumber, ShopResponse::mainImage, ShopResponse::isSubscribe
                )
                .containsExactly(
                        shop.getShopName(), shop.getShopType(), shop.getOwnerName(),
                        shop.getBusinessNumber(), shop.getAddress(),
                        shop.getShopTellNumber(), shop.getMainImage(), false /*isSubscribe*/
                );
        assertThat(response.operationTimeResponses()).hasSize(registerRequest.operationTimeRequests().size());
        assertThat(response.shopImages()).hasSize(registerRequest.imageFiles().size());
    }

    @DisplayName("존재하지 않는 매장을 조회할경우 예외를 던진다.")
    @Test
    void failGetShop() {
        assertThatThrownBy(() -> shopQueryService.getShop(-99999L, 1L))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("주어진 반경에 포함되는 매장들을 조회한다.")
    @Test
    void getNearbyShops() {
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

        var nearbySearchServiceRequest = ShopNearbySearchServiceRequest.builder()
                .longitude(127.271143598307)
                .latitude(37.4013921881645)
                .radiusMeters(1000)
                .build();
        ShopNearbySearchListResponse response = shopQueryService.getNearbyShops(nearbySearchServiceRequest);

        assertThat(response.responses()).hasSize(2);
    }

    private ShopRegisterServiceRequest getShopRegisterServiceRequest(
            String shopName,
            String shopPhoneNumber,
            String businessNumber,
            String address
    ) {
        List<OperationTimeServiceRequest> timeServiceRequests = List.of(
                getOperationTimeServiceRequest(DayOfWeek.MONDAY, false),
                getOperationTimeServiceRequest(DayOfWeek.TUESDAY, false),
                getOperationTimeServiceRequest(DayOfWeek.WEDNESDAY, false),
                getOperationTimeServiceRequest(DayOfWeek.THURSDAY, false),
                getOperationTimeServiceRequest(DayOfWeek.FRIDAY, false),
                getOperationTimeServiceRequest(DayOfWeek.SATURDAY, false),
                getOperationTimeServiceRequest(DayOfWeek.SUNDAY, true)
        );

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
                .operationTimeRequests(timeServiceRequests)
                .build();// 매장 주소
    }

    private OperationTimeServiceRequest getOperationTimeServiceRequest(DayOfWeek dayOfWeek, boolean isClosed) {
        return new OperationTimeServiceRequest(
                dayOfWeek,
                "09:00",
                "22:00",
                isClosed
        );
    }

}