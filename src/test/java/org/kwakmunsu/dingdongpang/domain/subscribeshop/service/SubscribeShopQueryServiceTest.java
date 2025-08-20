package org.kwakmunsu.dingdongpang.domain.subscribeshop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.shop.ShopFixture;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.request.ShopRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.entity.SubscribeShop;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.SubscribeShopRepository;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto.DailySubscriptionResponse;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto.SubscribeShopListResponse;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.service.dto.DailySubscriptionListResponse;
import org.kwakmunsu.dingdongpang.global.GeoFixture;
import org.kwakmunsu.dingdongpang.global.exception.ForbiddenException;
import org.locationtech.jts.geom.Point;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
record SubscribeShopQueryServiceTest(
        SubscribeShopQueryService subscribeShopQueryService,
        SubscribeShopRepository subscribeShopRepository,
        ShopRepository shopRepository,
        CacheManager cacheManager
) {

    @DisplayName("구독한 매장 목록을 조회힌다.")
    @Test
    void getSubscribedShop() {
        ShopRegisterServiceRequest request = ShopFixture.getShopRegisterServiceRequest();
        ShopRegisterServiceRequest request2 = getShopRegisterServiceRequest(3L);

        Point point = GeoFixture.createPoint(1.2, 2.3);
        Point point2 = GeoFixture.createPoint(1.5, 2.3);
        Shop shop = Shop.create(request.toDomainRequest(1L, point, null));
        Shop shop2 = Shop.create(request2.toDomainRequest(1L, point2, null));
        shopRepository.save(shop);
        shopRepository.save(shop2);

        SubscribeShop subscribeShop = SubscribeShop.create(1L, shop.getId());
        SubscribeShop subscribeShop2 = SubscribeShop.create(1L, shop2.getId());
        subscribeShopRepository.save(subscribeShop);
        subscribeShopRepository.save(subscribeShop2);

        SubscribeShopListResponse response = subscribeShopQueryService.getSubscribedShop(1L);

        assertThat(response.totalSubscribeCount()).isEqualTo(2);
        assertThat(response.responses()).hasSize(2)
                .extracting("shopId", "shopName", "mainImage")
                .containsExactly(
                        tuple(shop2.getId(), shop2.getShopName(), shop2.getMainImage()),
                        tuple(shop.getId(), shop.getShopName(), shop.getMainImage())
                );
    }

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
                        tuple(today, 2L)
                );
    }

    @DisplayName("최근 일주일 간 구독자 수를 조회한다.")
    @Test
    void getWeeklySubscriptionsByCache() {
        ShopRegisterServiceRequest request = ShopFixture.getShopRegisterServiceRequest();
        Point point = GeoFixture.createPoint(1.2, 2.3);
        Shop shop = Shop.create(request.toDomainRequest(1L, point, null));
        shopRepository.save(shop);

        SubscribeShop subscribeShop = SubscribeShop.create(1L, shop.getId());
        SubscribeShop subscribeShop1 = SubscribeShop.create(2L, shop.getId());
        subscribeShopRepository.save(subscribeShop);
        subscribeShopRepository.save(subscribeShop1);

        var response = subscribeShopQueryService.getWeeklySubscriptions(shop.getId(), shop.getMerchantId());
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
                        tuple(today, 2L)
                );
    }
    @DisplayName("최근 일주일 간 구독자 수 조회 시 처음에는 7번의 쿼리가 나가고 2번째부터는 6번의 hit로 가 터진다.")
    @Test
    void testCacheStatistics() {
        ShopRegisterServiceRequest request = ShopFixture.getShopRegisterServiceRequest();
        Point point = GeoFixture.createPoint(1.2, 2.3);
        Shop shop = Shop.create(request.toDomainRequest(1L, point, null));
        shopRepository.save(shop);

        SubscribeShop subscribeShop = SubscribeShop.create(1L, shop.getId());
        SubscribeShop subscribeShop1 = SubscribeShop.create(2L, shop.getId());
        subscribeShopRepository.save(subscribeShop);
        subscribeShopRepository.save(subscribeShop1);
        // 첫 번째 호출
        subscribeShopQueryService.getWeeklySubscriptions(shop.getId(), shop.getMerchantId());

        // 캐시 통계 확인
        CaffeineCache cache = (CaffeineCache) cacheManager.getCache("weekly-subscriptions");
        CacheStats stats = cache.getNativeCache().stats();

        System.out.println("Cache hits: " + stats.hitCount());
        System.out.println("Cache misses: " + stats.missCount());

        // 두 번째 호출
        subscribeShopQueryService.getWeeklySubscriptions(shop.getId(), shop.getMerchantId());

        stats = cache.getNativeCache().stats();
        System.out.println("Cache hits after 2nd call: " + stats.hitCount());

        // 캐시 히트 증가 확인
        assertThat(stats.hitCount()).isGreaterThan(0);
    }

    @DisplayName("매장 관리자가 아닐 경우  조회할 수 없다.")
    @Test
    void failGetWeeklySubscriptions() {
        assertThatThrownBy(() -> subscribeShopQueryService.getWeeklySubscriptions(-999L, -999L))
                .isInstanceOf(ForbiddenException.class);
    }

    private ShopRegisterServiceRequest getShopRegisterServiceRequest(Long merchantId) {
        return ShopRegisterServiceRequest.builder()
                .businessNumber("8962803261")    // 사업자 등록 번호
                .ownerName("김계란")              // 대표자명
                .shopName("역전할머니맥주")         // 매장명
                .shopType(ShopType.FASHION)      // 매장 타입 (enum)
                .shopPhoneNumber("010-8742-1554")// 매장 전화번호
                .address("testAdress")
                .mainImage(null)
                .merchantId(merchantId)
                .imageFiles(List.of())
                .operationTimeRequests(List.of())
                .build();// 매장 주소
    }
}