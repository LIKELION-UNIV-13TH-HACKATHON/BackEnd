package org.kwakmunsu.dingdongpang.domain.shop.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ShopRepository {

    private final ShopJpaRepository inquiryJpaRepository;

}