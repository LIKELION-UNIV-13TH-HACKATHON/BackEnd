package org.kwakmunsu.dingdongpang.domain.shop.service;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.shop.repository.ShopRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShopQueryService {

    private final ShopRepository shopRepository;

}