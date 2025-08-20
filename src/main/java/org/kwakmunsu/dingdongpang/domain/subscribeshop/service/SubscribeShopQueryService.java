package org.kwakmunsu.dingdongpang.domain.subscribeshop.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.SubscribeShopRepository;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto.DailySubscriptionResponse;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto.SubscribeShopListResponse;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.service.dto.DailySubscriptionListResponse;
import org.kwakmunsu.dingdongpang.global.exception.ForbiddenException;
import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SubscribeShopQueryService {

    private final SubscriptionCacheService subscriptionCacheService;
    private final SubscribeShopRepository subscribeShopRepository;
    private final ShopRepository shopRepository;

    public SubscribeShopListResponse getSubscribedShop(Long memberId) {
        return subscribeShopRepository.getSubscribeShop(memberId);
    }

    public DailySubscriptionListResponse getWeeklySubscriptions(Long shopId, Long merchantId) {
        validateReadWeeklySubscriptions(shopId, merchantId);

        LocalDate today = LocalDate.now();
        List<DailySubscriptionResponse> responses = IntStream.range(0, 7)
                .mapToObj(i -> today.minusDays(6 - i))  // 6일 전부터 오늘까지
                .map(date -> {
                    Long count = subscriptionCacheService.getDailySubscriptionCount(shopId, date);  // 조건부 캐싱

                    return new DailySubscriptionResponse(date, count);
                })
                .toList();

        return new DailySubscriptionListResponse(responses);
    }

    private void validateReadWeeklySubscriptions(Long shopId, Long merchantId) {
        if (shopRepository.existsByIdAndMerchantId(shopId, merchantId)) {
            return;
        }
        throw new ForbiddenException(ErrorStatus.FORBIDDEN_ERROR);
    }

}