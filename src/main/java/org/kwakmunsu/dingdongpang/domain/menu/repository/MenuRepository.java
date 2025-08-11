package org.kwakmunsu.dingdongpang.domain.menu.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.menu.entity.Menu;
import org.kwakmunsu.dingdongpang.global.exception.ForbiddenException;
import org.kwakmunsu.dingdongpang.global.exception.NotFoundException;
import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;
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

    public List<Menu> findByShopId(Long shopId) {
         return menuJpaRepository.findByShopId(shopId);
    }

    public Menu findById(Long menuId) {
        return menuJpaRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_MENU));
    }

    public Menu findByIdAndShopMerchantId(Long menuId, Long merchantId) {
        return menuJpaRepository.findByIdAndShopMerchantId(menuId, merchantId)
                .orElseThrow(() -> new ForbiddenException(ErrorStatus.FORBIDDEN_MODIFY));
    }

    public void deleteById(Long id) {
        menuJpaRepository.deleteById(id);
    }

}