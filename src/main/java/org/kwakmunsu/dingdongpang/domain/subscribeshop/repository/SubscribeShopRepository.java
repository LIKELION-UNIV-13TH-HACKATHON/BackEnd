package org.kwakmunsu.dingdongpang.domain.subscribeshop.repository;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.entity.SubscribeShop;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto.SubscribeShopListResponse;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto.SubscribeShopReadDomainRequest;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class SubscribeShopRepository {

    private final SubscribeShopJpaRepository subscribeShopJpaRepository;
    private final SubscribeShopQueryDslRepository subscribeShopQueryDslRepository;

    public boolean existsByMemberIdAndShopId(Long memberId, Long shopId) {
        return subscribeShopJpaRepository.existsByMemberIdAndShopId(memberId, shopId);
    }

    public void save(SubscribeShop subscribeShop) {
        subscribeShopJpaRepository.save(subscribeShop);
    }

    public void deleteByMemberIdAndShopId(Long memberId, Long shopId) {
        subscribeShopJpaRepository.deleteByMemberIdAndShopId(memberId, shopId);
    }

    public SubscribeShopListResponse getSubscribeShop(SubscribeShopReadDomainRequest request) {
        return subscribeShopQueryDslRepository.getSubscribedShopList(request);
    }

}