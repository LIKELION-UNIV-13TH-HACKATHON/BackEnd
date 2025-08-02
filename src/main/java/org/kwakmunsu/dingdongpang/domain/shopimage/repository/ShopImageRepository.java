package org.kwakmunsu.dingdongpang.domain.shopimage.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ShopImageRepository {

    private final ShopImageJpaRepository inquiryJpaRepository;

}