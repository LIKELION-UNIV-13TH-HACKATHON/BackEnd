package org.kwakmunsu.dingdongpang.domain.subscribeshop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.shop.ShopFixture;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.request.ShopRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.entity.SubscribeShop;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.SubscribeShopRepository;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto.DailySubscriptionResponse;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.service.dto.DailySubscriptionListResponse;
import org.kwakmunsu.dingdongpang.global.GeoFixture;
import org.kwakmunsu.dingdongpang.global.exception.ForbiddenException;
import org.locationtech.jts.geom.Point;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
record SubscribeShopQueryServiceTest(
        SubscribeShopQueryService subscribeShopQueryService,
        SubscribeShopRepository subscribeShopRepository,
        ShopRepository shopRepository
) {

    @DisplayName("최근 일주일 간 구독자 수를 조회한다.")
    @Test
    void getWeeklySubscriptions() {
        ShopRegisterServiceRequest request = ShopFixture.getShopRegisterServiceRequest();
        Point point = GeoFixture.createPoint(1.2, 2.3);
        Shop shop = Shop.create(request.toDomainRequest(1L, point, null));
        shopRepository.save(shop);

        SubscribeShop subscribeShop = SubscribeShop.create(1L, shop.getId());
        SubscribeShop subscribeShop1 = SubscribeShop.create(2L, shop.getId());
        subscribeShopRepository.save(subscribeShop);
        subscribeShopRepository.save(subscribeShop1);

        DailySubscriptionListResponse response = subscribeShopQueryService.getWeeklySubscriptions(shop.getId(),
                shop.getMerchantId());
        List<DailySubscriptionResponse> responses = response.responses();

        assertThat(responses).hasSize(7)
                // 날짜 순서가 오름차순(오래된 → 최신)인지 확인
                .extracting(DailySubscriptionResponse::date)
                .isSortedAccordingTo(LocalDate::compareTo);

        LocalDate today = LocalDate.now();
        assertThat(responses).hasSize(7)
                .extracting("date", "subscriptionCount")
                .containsExactly(
                        tuple(today.minusDays(6), 0L),
                        tuple(today.minusDays(5), 0L),
                        tuple(today.minusDays(4), 0L),
                        tuple(today.minusDays(3), 0L),
                        tuple(today.minusDays(2), 0L),
                        tuple(today.minusDays(1), 0L),
                        tuple(today,              2L)
                );
    }

    @DisplayName("매장 관리자가 아닐 경우  조회할 수 없다.")
    @Test
    void failGetWeeklySubscriptions() {
        Assertions.assertThatThrownBy(() -> subscribeShopQueryService.getWeeklySubscriptions(-999L, -999L))
                .isInstanceOf(ForbiddenException.class);
    }

}