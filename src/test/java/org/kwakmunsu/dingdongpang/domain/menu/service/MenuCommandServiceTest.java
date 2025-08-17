package org.kwakmunsu.dingdongpang.domain.menu.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.menu.entity.Menu;
import org.kwakmunsu.dingdongpang.domain.menu.repository.MenuRepository;
import org.kwakmunsu.dingdongpang.domain.menu.service.dto.MenuRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.menu.service.dto.MenuUpdateServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shop.ShopFixture;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.kwakmunsu.dingdongpang.global.GeoFixture;
import org.kwakmunsu.dingdongpang.global.exception.DuplicationException;
import org.kwakmunsu.dingdongpang.global.exception.ForbiddenException;
import org.kwakmunsu.dingdongpang.infrastructure.s3.S3Provider;
import org.locationtech.jts.geom.Point;
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
        var shopRegisterServiceRequest = ShopFixture.getShopRegisterServiceRequest();
        var memberId = 1L;
        Point point = GeoFixture.createPoint();
        var shop = Shop.create(shopRegisterServiceRequest.toDomainRequest(memberId, point, null));
        shopRepository.save(shop);

        var menuRegisterServiceRequest = new MenuRegisterServiceRequest("불닭", 20000, "아주 매워", null, memberId);
        menuCommandService.register(menuRegisterServiceRequest);

        boolean exists = menuRepository.existsByShopIdAndName(shop.getId(), menuRegisterServiceRequest.name());
        assertThat(exists).isTrue();
    }

    @DisplayName("이미 등록된 메뉴라면 등록을 실패한다.")
    @Test
    void failRegister() {
        var shopRegisterServiceRequest = ShopFixture.getShopRegisterServiceRequest();
        var memberId = 1L;
        Point point = GeoFixture.createPoint();
        var shop = Shop.create(shopRegisterServiceRequest.toDomainRequest(memberId, point, null));
        shopRepository.save(shop);

        var menuRegisterServiceRequest = new MenuRegisterServiceRequest("불닭", 20000, "아주 매워", null, memberId);
        menuCommandService.register(menuRegisterServiceRequest);

        assertThatThrownBy(() ->  menuCommandService.register(menuRegisterServiceRequest))
            .isInstanceOf(DuplicationException.class);
    }
    
    @DisplayName("메뉴 정보를 수정한다.")
    @Test
    void updateImage() {
        var shopRegisterServiceRequest = ShopFixture.getShopRegisterServiceRequest();
        var memberId = 1L;
        Point point = GeoFixture.createPoint();
        var shop = Shop.create(shopRegisterServiceRequest.toDomainRequest(memberId, point, null));
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
        var shopRegisterServiceRequest = ShopFixture.getShopRegisterServiceRequest();
        var memberId = 1L;
        Point point = GeoFixture.createPoint();
        var shop = Shop.create(shopRegisterServiceRequest.toDomainRequest(memberId, point, null));
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
        var shopRegisterServiceRequest = ShopFixture.getShopRegisterServiceRequest();
        var memberId = 1L;
        Point point = GeoFixture.createPoint();
        var shop = Shop.create(shopRegisterServiceRequest.toDomainRequest(memberId, point, null));
        shopRepository.save(shop);

        var menuRegisterServiceRequest = new MenuRegisterServiceRequest("불닭", 20000, "아주 매워", null, memberId);
        menuCommandService.register(menuRegisterServiceRequest);

        List<Menu> menus = menuRepository.findByShopId(shop.getId());
        menuCommandService.delete(menus.getFirst().getId(), memberId);

        boolean exists = menuRepository.existsByShopIdAndName(shop.getId(), menuRegisterServiceRequest.name());

        assertThat(exists).isFalse();
    }

}