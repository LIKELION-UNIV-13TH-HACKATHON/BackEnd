package org.kwakmunsu.dingdongpang.domain.subscribeshop.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

    public List<SubscribeShop> findByShopId(Long shopId) {
        return subscribeShopJpaRepository.findByShopId(shopId);
    }

    public List<String> findFcmTokensByShopId(Long shopId) {
        return subscribeShopJpaRepository.findFcmTokensByShopId(shopId);
    }

    public Long countByShopIdAndCreatedAtBetween(Long shopId, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        return subscribeShopJpaRepository.countByShopIdAndCreatedAtBetween(shopId, startOfDay, endOfDay);
    }

    public Long countByShopId(Long shopId) {
        return subscribeShopJpaRepository.countByShopId(shopId);
    }

}