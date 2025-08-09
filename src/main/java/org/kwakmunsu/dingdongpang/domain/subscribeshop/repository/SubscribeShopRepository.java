package org.kwakmunsu.dingdongpang.domain.subscribeshop.repository;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.entity.SubscribeShop;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class SubscribeShopRepository {

    private final SubscribeShopJpaRepository subscribeShopJpaRepository;

    public boolean existsByMemberIdAndShopId(Long memberId, Long shopId) {
        return subscribeShopJpaRepository.existsByMemberIdAndShopId(memberId, shopId);
    }

    public void save(SubscribeShop subscribeShop) {
        subscribeShopJpaRepository.save(subscribeShop);
    }

    public void deleteByMemberIdAndShopId(Long memberId, Long shopId) {
        subscribeShopJpaRepository.deleteByMemberIdAndShopId(memberId, shopId);
    }

}