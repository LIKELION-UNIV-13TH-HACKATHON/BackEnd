package org.kwakmunsu.dingdongpang.domain.menu.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.DayOfWeek;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.OperationTimeServiceRequest;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.ShopRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.menu.entity.Menu;
import org.kwakmunsu.dingdongpang.domain.menu.repository.MenuRepository;
import org.kwakmunsu.dingdongpang.domain.menu.service.dto.MenuRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.menu.service.dto.MenuUpdateServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.kwakmunsu.dingdongpang.domain.shop.repository.ShopRepository;
import org.kwakmunsu.dingdongpang.global.exception.DuplicationException;
import org.kwakmunsu.dingdongpang.global.exception.ForbiddenException;
import org.kwakmunsu.dingdongpang.infrastructure.geocoding.GeocodeResponse;
import org.kwakmunsu.dingdongpang.infrastructure.s3.S3Provider;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
    void register() {
        var shopRegisterServiceRequest = getShopRegisterServiceRequest();
        var memberId = 1L;
        var geocodeResponse = new GeocodeResponse("1","10");
        var shop = Shop.create(shopRegisterServiceRequest.toDomainRequest(memberId, geocodeResponse, null));
        shopRepository.save(shop);

        var menuRegisterServiceRequest = new MenuRegisterServiceRequest("불닭", 20000, "아주 매워", null, memberId);
        menuCommandService.register(menuRegisterServiceRequest);

        boolean exists = menuRepository.existsByShopIdAndName(shop.getId(), menuRegisterServiceRequest.name());
        assertThat(exists).isTrue();
    }

    @DisplayName("이미 등록된 메뉴라면 등록을 실패한다.")
    @Test
    void failRegister() {
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
    
    @DisplayName("메뉴 정보를 수정한다.")
    @Test
    void updateImage() {
        var shopRegisterServiceRequest = getShopRegisterServiceRequest();
        var memberId = 1L;
        var geocodeResponse = new GeocodeResponse("1","10");
        var shop = Shop.create(shopRegisterServiceRequest.toDomainRequest(memberId, geocodeResponse, null));
        shopRepository.save(shop);

        var menuRegisterServiceRequest = new MenuRegisterServiceRequest("불닭", 20000, "아주 매워", null, memberId);
        menuCommandService.register(menuRegisterServiceRequest);

        List<Menu> menus = menuRepository.findByShopId(shop.getId());
        var menuUpdateServiceRequest = new MenuUpdateServiceRequest(menus.getFirst().getId(), "핵불닭", 25000, "아주아주 매워", null, memberId);
        menuCommandService.update(menuUpdateServiceRequest);

        var menu = menuRepository.findById(menus.getFirst().getId());
        assertThat(menu)
                .extracting(Menu::getName, Menu:: getPrice, Menu:: getDescription, Menu:: getImage)
                .containsExactly(
                        menuUpdateServiceRequest.name(),
                        menuUpdateServiceRequest.price(),
                        menuUpdateServiceRequest.description(),
                        menuUpdateServiceRequest.image()
                );
    }

    @DisplayName("매장 관리자가 아니면 매장 메뉴 정보를 수정할 수 없다.")
    @Test
    void failUpdate() {
        var shopRegisterServiceRequest = getShopRegisterServiceRequest();
        var memberId = 1L;
        var geocodeResponse = new GeocodeResponse("1","10");
        var shop = Shop.create(shopRegisterServiceRequest.toDomainRequest(memberId, geocodeResponse, null));
        shopRepository.save(shop);

        var menuRegisterServiceRequest = new MenuRegisterServiceRequest("불닭", 20000, "아주 매워", null, memberId);
        menuCommandService.register(menuRegisterServiceRequest);

        var invalidOwnerId = 12313L;
        List<Menu> menus = menuRepository.findByShopId(shop.getId());
        var menuUpdateServiceRequest = new MenuUpdateServiceRequest(menus.getFirst().getId(), "핵불닭", 25000, "아주아주 매워", null, invalidOwnerId);

        assertThatThrownBy(() -> menuCommandService.update(menuUpdateServiceRequest))
            .isInstanceOf(ForbiddenException.class);
    }

    @DisplayName("메뉴를 삭제한다.")
    @Test
    void delete() {
        var shopRegisterServiceRequest = getShopRegisterServiceRequest();
        var memberId = 1L;
        var geocodeResponse = new GeocodeResponse("1","10");
        var shop = Shop.create(shopRegisterServiceRequest.toDomainRequest(memberId, geocodeResponse, null));
        shopRepository.save(shop);

        var menuRegisterServiceRequest = new MenuRegisterServiceRequest("불닭", 20000, "아주 매워", null, memberId);
        menuCommandService.register(menuRegisterServiceRequest);

        List<Menu> menus = menuRepository.findByShopId(shop.getId());
        menuCommandService.delete(menus.getFirst().getId(), memberId);

        boolean exists = menuRepository.existsByShopIdAndName(shop.getId(), menuRegisterServiceRequest.name());

        assertThat(exists).isFalse();
    }

    private ShopRegisterServiceRequest getShopRegisterServiceRequest() {

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
                null,                                       // mainImage
                List.of(),                                  // imageFiles
                List.of(mondayOperation, tuesdayOperation)  // operationTimeRequests
        );
    }

}