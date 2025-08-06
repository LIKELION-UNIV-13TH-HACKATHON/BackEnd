package org.kwakmunsu.dingdongpang.domain.shop.repository;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.global.exception.NotFoundException;
import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ShopRepository {

    private final ShopJpaRepository shopJpaRepository;

    public void save(Shop shop) {
        shopJpaRepository.save(shop);
    }

    public boolean existsByBusinessNumber(String businessNumber) {
        return shopJpaRepository.existsByBusinessNumber(businessNumber);
    }

    public Shop findByMemberId(Long memberId) {
        return shopJpaRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_SHOP));
    }

}