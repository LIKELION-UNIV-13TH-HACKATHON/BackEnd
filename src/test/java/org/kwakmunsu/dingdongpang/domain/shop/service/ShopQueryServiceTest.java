package org.kwakmunsu.dingdongpang.domain.shop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.OperationTimeServiceRequest;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.ShopRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shopoperation.ShopOperationTimeRepository;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.ShopResponse;
import org.kwakmunsu.dingdongpang.domain.shopimage.repository.ShopImageRepository;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.SubscribeShopRepository;
import org.kwakmunsu.dingdongpang.global.GeoFixture;
import org.kwakmunsu.dingdongpang.global.exception.NotFoundException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Transactional
@SpringBootTest
record ShopQueryServiceTest(
        ShopQueryService shopQueryService,
        ShopCommandService shopCommandService,
        ShopRepository shopRepository,
        ShopImageRepository shopImageRepository,
        ShopOperationTimeRepository shopOperationTimeRepository,
        SubscribeShopRepository subscribeShopRepository
) {

    @DisplayName("매장 정보를 조회한다.")
    @Test
    void getShop() throws IOException {
        var registerRequest = getShopRegisterServiceRequest();
        var point = GeoFixture.createPoint(1.2, 2.3);
        long merchantId = 1L;
        long memberId = 2L;
        shopCommandService.register(registerRequest, point, merchantId);

        var shop = shopRepository.findByMerchantId(merchantId);

        ShopResponse response = shopQueryService.getShop(shop.getId(), memberId);

        assertThat(response)
                .extracting(
                        ShopResponse::shopName, ShopResponse::shopType, ShopResponse::address,
                        ShopResponse::shopTellNumber, ShopResponse::mainImage, ShopResponse::isSubscribe
                )
                .containsExactly(
                        shop.getShopName(), shop.getShopType(), shop.getAddress(),
                        shop.getShopTellNumber(), shop.getMainImage(), false /*isSubscribe*/
                );
        assertThat(response.operationTimeResponses()).hasSize(getShopRegisterServiceRequest().operationTimeRequests().size());
        assertThat(response.shopImages()).hasSize(getShopRegisterServiceRequest().imageFiles().size());
    }

    @DisplayName("존재하지 않는 매장을 조회할경우 예외를 던진다.")
    @Test
    void failGetShop() {
        assertThatThrownBy(() ->shopQueryService.getShop(-99999L, 1L))
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

    private ShopRegisterServiceRequest getShopRegisterServiceRequest() throws IOException {
        MultipartFile mainImage = getMockMultipartFile();
        MultipartFile image1 = getMockMultipartFile();
        MultipartFile image2 = getMockMultipartFile();

        List<OperationTimeServiceRequest> timeServiceRequests = List.of(
                getOperationTimeServiceRequest(DayOfWeek.MONDAY, false),
                getOperationTimeServiceRequest(DayOfWeek.TUESDAY, false),
                getOperationTimeServiceRequest(DayOfWeek.WEDNESDAY, false),
                getOperationTimeServiceRequest(DayOfWeek.THURSDAY, false),
                getOperationTimeServiceRequest(DayOfWeek.FRIDAY, false),
                getOperationTimeServiceRequest(DayOfWeek.SATURDAY, false),
                getOperationTimeServiceRequest(DayOfWeek.SUNDAY, true)
        );

        return new ShopRegisterServiceRequest(
                "My Shop",                                  // shopName
                ShopType.FOOD,                              // shopType (예: enum)
                "010-1234-5678",                            // shopPhoneNumber
                "경기 광주시 경충대로 1411 투썸플레이스",                 // address
                "1234567890",                               // businessNumber
                "홍길동",                                     // ownerName
                mainImage,                                  // mainImage
                List.of(image1, image2),                    // imageFiles
                timeServiceRequests                         // operationTimeRequests
        );
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