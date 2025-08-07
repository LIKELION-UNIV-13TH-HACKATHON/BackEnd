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

    public MenuListResponse getMenus(Long memberId) {
        Shop shop = shopRepository.findByMemberId(memberId);
        List<Menu> menus = menuRepository.findByShopId(shop.getId());

        List<MenuResponse> responses = menus.stream()
                .map(MenuResponse::of)
                .toList();

        return new MenuListResponse(responses);
    }

}