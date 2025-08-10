package org.kwakmunsu.dingdongpang.domain.subscribeshop.service;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.entity.SubscribeShop;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.SubscribeShopRepository;
import org.kwakmunsu.dingdongpang.global.exception.DuplicationException;
import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SubscribeShopCommandService {

    private final ShopRepository shopRepository;
    private final SubscribeShopRepository subscribeShopRepository;

    public void subscribe(Long shopId, Long memberId) {
        shopRepository.findById(shopId);

        boolean isSubscribed = subscribeShopRepository.existsByMemberIdAndShopId(memberId, shopId);
        if (isSubscribed) {
            throw new DuplicationException(ErrorStatus.ALREADY_SUBSCRIBE_SHOP);
        }

        SubscribeShop subscribeShop  = SubscribeShop.create(memberId, shopId);
        subscribeShopRepository.save(subscribeShop);
    }

    public void unsubscribe(Long shopId, Long memberId) {
        shopRepository.findById(shopId);

        boolean isSubscribed = subscribeShopRepository.existsByMemberIdAndShopId(memberId, shopId);
        if (isSubscribed) {
            subscribeShopRepository.deleteByMemberIdAndShopId(memberId, shopId);
        }
    }

}