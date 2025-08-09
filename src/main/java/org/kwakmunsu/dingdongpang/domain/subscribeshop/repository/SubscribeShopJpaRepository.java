package org.kwakmunsu.dingdongpang.domain.subscribeshop.repository;

import org.kwakmunsu.dingdongpang.domain.subscribeshop.entity.SubscribeShop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribeShopJpaRepository extends JpaRepository<SubscribeShop, Long> {

    boolean existsByMemberIdAndShopId(Long memberId, Long shopId);

    void deleteByMemberIdAndShopId(Long memberId, Long shopId);

}