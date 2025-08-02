package org.kwakmunsu.dingdongpang.domain.shop.repository;

import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopJpaRepository extends JpaRepository<Shop, Long> {

}