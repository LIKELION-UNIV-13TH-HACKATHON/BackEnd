package org.kwakmunsu.dingdongpang.domain.shop.repository.shop;

import java.util.Optional;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopJpaRepository extends JpaRepository<Shop, Long> {

    boolean existsByBusinessNumber(String businessNumber);

    Optional<Shop> findByMemberId(Long memberId);
}