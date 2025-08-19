package org.kwakmunsu.dingdongpang.domain.subscribeshop.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

    private final SubscribeShopRepository subscribeShopRepository;
    private final ShopRepository shopRepository;

    public SubscribeShopListResponse getSubscribedShop(Long memberId) {
        return subscribeShopRepository.getSubscribeShop(memberId);
    }

    public DailySubscriptionListResponse getWeeklySubscriptions(Long shopId, Long merchantId) {
        validateReadWeeklySubscriptions(shopId, merchantId);

        // 오늘부터 6일 전까지 (총 7일간)
        LocalDateTime endDate = LocalDate.now().plusDays(1).atStartOfDay(); // 내일 00:00:00 (오늘 포함하기 위해)
        LocalDateTime startDate = LocalDate.now().minusDays(6).atStartOfDay(); // 6일 전 00:00:00
        List<DailySubscriptionResponse> weeklySubscriptions = subscribeShopRepository.getWeeklySubscriptions(shopId, startDate,
                endDate);
        // GROUP BY 에서 데이터가 없는 날짜는 결과에 포함되지 않는 SQL의 특성 때문에 각 날짜 결과 생성을 해줍니다.
        List<DailySubscriptionResponse> responses = getDailySubscriptionListResponse(weeklySubscriptions);

        return new DailySubscriptionListResponse(responses);
    }

    private void validateReadWeeklySubscriptions(Long shopId, Long merchantId) {
        if (shopRepository.existsByIdAndMerchantId(shopId, merchantId)) {
            return;
        }
        throw new ForbiddenException(ErrorStatus.FORBIDDEN_ERROR);
    }

    private List<DailySubscriptionResponse> getDailySubscriptionListResponse(List<DailySubscriptionResponse> weeklySubscriptions) {
        // 7일 전체 날짜 생성
        List<LocalDate> allDates = IntStream.range(0, 7)
                .mapToObj(i -> LocalDate.now().minusDays(6).plusDays(i))
                .toList();

        // DB 결과를 Map 으로 변환 (날짜 -> 카운트)
        Map<LocalDate, Long> dataMap = weeklySubscriptions.stream()
                .collect(Collectors.toMap(
                        DailySubscriptionResponse::date,
                        DailySubscriptionResponse::subscriptionCount
                ));

        // 모든 날짜에 대해 결과 생성 (없으면 0)
        return allDates.stream()
                .map(date -> new DailySubscriptionResponse(
                        date,
                        dataMap.getOrDefault(date, 0L)
                )).toList();
    }

}