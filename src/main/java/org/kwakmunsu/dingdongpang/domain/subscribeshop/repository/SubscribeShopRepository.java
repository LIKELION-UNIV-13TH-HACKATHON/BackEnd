package org.kwakmunsu.dingdongpang.domain.subscribeshop.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class SubscribeShopRepository {

    private final SubscribeShopJpaRepository subscribeShopJpaRepository;

    public boolean existsByMemberIdAndShopId(Long memberId, Long shopId) {
        return subscribeShopJpaRepository.existsByMemberIdAndShopId(memberId, shopId);
    }

}