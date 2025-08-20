package org.kwakmunsu.dingdongpang.domain.subscribeshop.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.SubscribeShopRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SubscriptionCacheService {

    private final SubscribeShopRepository subscribeShopRepository;

    /**
     * 캐시된 일별 신규 구독자 수 정보를 가져옵니다.
     *
     * @param shopId 상점 ID
     * @param date   타겟 날짜
     * @return 일별 신규 구독자 수 정보
     */
    @Cacheable(
            value = "weekly-subscriptions",
            key = "#shopId + '-' + #date",
            condition = "#date.isBefore(T(java.time.LocalDate).now())"
    )
    public Long getDailySubscriptionCount(Long shopId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return subscribeShopRepository.getSubscriptionCount(shopId, startOfDay, endOfDay);
    }

}