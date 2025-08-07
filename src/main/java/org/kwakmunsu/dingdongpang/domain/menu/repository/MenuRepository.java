package org.kwakmunsu.dingdongpang.domain.menu.repository;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.menu.entity.Menu;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MenuRepository {

    private final MenuJpaRepository menuJpaRepository;

    public boolean existsByShopIdAndName(Long shopId, String name) {
        return menuJpaRepository.existsByShopIdAndName(shopId,name);
    }

    public void save(Menu menu) {
        menuJpaRepository.save(menu);
    }

}