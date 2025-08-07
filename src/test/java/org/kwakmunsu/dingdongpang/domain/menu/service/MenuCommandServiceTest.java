package org.kwakmunsu.dingdongpang.domain.menu.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.OperationTimeServiceRequest;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.ShopRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.menu.repository.MenuRepository;
import org.kwakmunsu.dingdongpang.domain.menu.service.dto.MenuRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.kwakmunsu.dingdongpang.domain.shop.repository.ShopRepository;
import org.kwakmunsu.dingdongpang.global.exception.DuplicationException;
import org.kwakmunsu.dingdongpang.infrastructure.geocoding.GeocodeResponse;
import org.kwakmunsu.dingdongpang.infrastructure.s3.S3Provider;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@SpringBootTest
record MenuCommandServiceTest(
        MenuCommandService menuCommandService,
        MenuRepository menuRepository,
        ShopRepository shopRepository,
        S3Provider s3Provider
) {

    @DisplayName("매장 메뉴를 등록한다.")
    @Test
    void register() throws IOException {
        var shopRegisterServiceRequest = getShopRegisterServiceRequest();
        var memberId = 1L;
        var geocodeResponse = new GeocodeResponse("1","10");
        var shop = Shop.create(shopRegisterServiceRequest.toDomainRequest(memberId, geocodeResponse, null));
        shopRepository.save(shop);
        MockMultipartFile file = getMockMultipartFile();
        var menuRegisterServiceRequest = new MenuRegisterServiceRequest("불닭", 20000, "아주 매워", file, memberId);
        menuCommandService.register(menuRegisterServiceRequest);

        boolean exists = menuRepository.existsByShopIdAndName(shop.getId(), menuRegisterServiceRequest.name());
        assertThat(exists).isTrue();
    }

    @DisplayName("이미 등록된 메뉴라면 등록을 실패한다.")
    @Test
    void failRegister() throws IOException {
        var shopRegisterServiceRequest = getShopRegisterServiceRequest();
        var memberId = 1L;
        var geocodeResponse = new GeocodeResponse("1","10");
        var shop = Shop.create(shopRegisterServiceRequest.toDomainRequest(memberId, geocodeResponse, null));
        shopRepository.save(shop);

        var menuRegisterServiceRequest = new MenuRegisterServiceRequest("불닭", 20000, "아주 매워", null, memberId);
        menuCommandService.register(menuRegisterServiceRequest);

        assertThatThrownBy(() ->  menuCommandService.register(menuRegisterServiceRequest))
            .isInstanceOf(DuplicationException.class);
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
                "서울특별시 강남구 역삼동 123-45",                 // address
                "1234567890",                               // businessNumber
                "홍길동",                                     // ownerName
                mainImage,                                  // mainImage
                List.of(image1, image2),                    // imageFiles
                List.of(mondayOperation, tuesdayOperation)  // operationTimeRequests
        );
    }
}