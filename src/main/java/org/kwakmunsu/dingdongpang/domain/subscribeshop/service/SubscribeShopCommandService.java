package org.kwakmunsu.dingdongpang.domain.subscribeshop.service;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.SubscribeShopRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SubscribeShopCommandService {

    private final SubscribeShopRepository subscribeShopRepository;

}