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
import org.kwakmunsu.dingdongpang.domain.member.service.MemberCommandService;
import org.kwakmunsu.dingdongpang.domain.menu.entity.Menu;
import org.kwakmunsu.dingdongpang.domain.menu.repository.MenuRepository;
import org.kwakmunsu.dingdongpang.domain.menu.service.dto.MenuListResponse;
import org.kwakmunsu.dingdongpang.domain.menu.service.dto.MenuRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.menu.service.dto.MenuResponse;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.kwakmunsu.dingdongpang.domain.shop.service.ShopCommandService;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.request.OperationTimeServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.request.ShopRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.global.GeoFixture;
import org.kwakmunsu.dingdongpang.global.exception.NotFoundException;
import org.locationtech.jts.geom.Point;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@SpringBootTest
record MenuQueryServiceTest(
        MenuQueryService menuQueryService,
        MenuCommandService menuCommandService,
        ShopCommandService shopCommandService,
        MemberCommandService memberCommandService,
        MenuRepository menuRepository,
        ShopRepository shopRepository
) {

    @DisplayName("매장 메뉴 목록 조회")
    @Test
    void getMenusByMerchant() throws IOException {
        var shopRegisterServiceRequest = getShopRegisterServiceRequest();
        Point point = GeoFixture.createPoint(1.2, 2.3);
        long merchantId = 1L;
        shopCommandService.register(shopRegisterServiceRequest, point, merchantId);

        MockMultipartFile file = getMockMultipartFile();
        var menuRegisterRequest1 = new MenuRegisterServiceRequest("테스트1", 20000, "설명1", file, merchantId);
        var menuRegisterRequest2 = new MenuRegisterServiceRequest("테스트2", 20000, "설명2", file, merchantId);
        menuCommandService.register(menuRegisterRequest1);
        menuCommandService.register(menuRegisterRequest2);

        MenuListResponse response = menuQueryService.getMenusByMerchant(merchantId);

        assertThat(response.responses()).hasSize(2);
        assertThat(response.responses().getFirst())
                .extracting(
                        MenuResponse::name,
                        MenuResponse::price,
                        MenuResponse::description
                )
                .containsExactly(
                        menuRegisterRequest1.name(),
                        menuRegisterRequest1.price(),
                        menuRegisterRequest1.description()
                );
        assertThat(response.responses().getFirst().image()).isNotNull();
    }

    @DisplayName("상인이 아닌 회원이 매장 메뉴 목록 조회 시 실패한다.")
    @Test
    void failGetMenusByMerchant() {
        var invalidMerchantId = 99999L;
        assertThatThrownBy(() -> menuQueryService.getMenusByMerchant(invalidMerchantId))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("메뉴를 상세 조회한다")
    @Test
    void getMenu() throws IOException {
        var shopRegisterServiceRequest = getShopRegisterServiceRequest();
        Point point = GeoFixture.createPoint(1.2, 2.3);
        long merchantId = 1L;
        shopCommandService.register(shopRegisterServiceRequest, point, merchantId);
        var shop = shopRepository.findByMerchantId(merchantId);

        var menu = Menu.create(shop, "name", 10000, "description", "image");
        menuRepository.save(menu);

        MenuResponse response = menuQueryService.getMenu(menu.getId());

        assertThat(response)
                .extracting(MenuResponse::name, MenuResponse::price, MenuResponse::description, MenuResponse::image)
                .containsExactly(menu.getName(),    menu.getPrice(),     menu.getDescription(),     menu.getImage());
    }

    @DisplayName("존재하지 않는 메뉴 조회 요청을 할 경우 에러를 반환한다.")
    @Test
    void failGetMenu() {
        var invalidMenuId = 99999L;
        assertThatThrownBy(() -> menuQueryService.getMenu(invalidMenuId))
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
        return ShopRegisterServiceRequest.builder()
                .businessNumber("8962801461")    // 사업자 등록 번호
                .ownerName("김계란")              // 대표자명
                .shopName("역전할머니맥주")         // 매장명
                .shopType(ShopType.FASHION)      // 매장 타입 (enum)
                .shopPhoneNumber("010-8742-1234")// 매장 전화번호
                .address("경기도 광주시 경충대로1461번길 12-4 코오롱 세이브 프라자 202호")
                .mainImage(mainImage)
                .merchantId(1L)
                .imageFiles(List.of(image1, image2))
                .operationTimeRequests(List.of(mondayOperation, tuesdayOperation))
                .build();// 매장 주소
    }

}