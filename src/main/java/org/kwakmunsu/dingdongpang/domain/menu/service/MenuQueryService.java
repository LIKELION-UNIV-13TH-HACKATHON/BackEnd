package org.kwakmunsu.dingdongpang.domain.menu.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.menu.entity.Menu;
import org.kwakmunsu.dingdongpang.domain.menu.repository.MenuRepository;
import org.kwakmunsu.dingdongpang.domain.menu.service.dto.MenuListResponse;
import org.kwakmunsu.dingdongpang.domain.menu.service.dto.MenuResponse;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.repository.ShopRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MenuQueryService {

    private final MenuRepository menuRepository;
    private final ShopRepository shopRepository;

    public MenuListResponse getMenusByMerchant(Long memberId) {
        Shop shop = shopRepository.findByMemberId(memberId);
        return getMenuListResponse(shop.getId());
    }

    public MenuListResponse getMenusByCustomer(Long shopId) {
        return getMenuListResponse(shopId);
    }

    private MenuListResponse getMenuListResponse(Long shopId) {
        List<Menu> menus = menuRepository.findByShopId(shopId);

        List<MenuResponse> responses = menus.stream()
                .map(MenuResponse::of)
                .toList();

        return new MenuListResponse(responses);
    }

    public MenuResponse getMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId);

        return MenuResponse.of(menu);
    }

}