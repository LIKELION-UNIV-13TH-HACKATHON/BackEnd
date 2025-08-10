package org.kwakmunsu.dingdongpang.domain.subscribeshop.service;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.SubscribeShopRepository;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto.SubscribeShopListResponse;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.service.dto.SubscribeShopReadServiceRequest;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SubscribeShopQueryService {

    private final SubscribeShopRepository subscribeShopRepository;

    public SubscribeShopListResponse getSubscribedShop(SubscribeShopReadServiceRequest request) {
       return subscribeShopRepository.getSubscribeShop(request.toDomainRequest());
    }

}