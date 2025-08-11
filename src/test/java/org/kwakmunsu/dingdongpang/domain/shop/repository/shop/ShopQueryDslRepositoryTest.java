package org.kwakmunsu.dingdongpang.domain.shop.repository.shop;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kwakmunsu.dingdongpang.domain.shop.entity.SortBy.MOST_SUBSCRIBED;
import static org.kwakmunsu.dingdongpang.domain.shop.entity.SortBy.NEAREST;
import static org.kwakmunsu.dingdongpang.domain.shop.entity.SortBy.NEWEST;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.OperationTimeServiceRequest;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.ShopRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.dto.ShopListResponse;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.dto.ShopPreviewResponse;
import org.kwakmunsu.dingdongpang.domain.shop.service.ShopCommandService;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.ShopReadServiceRequest;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.entity.SubscribeShop;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.SubscribeShopRepository;
import org.kwakmunsu.dingdongpang.global.GeoFixture;
import org.kwakmunsu.dingdongpang.infrastructure.geocoding.KakaoGeocodingProvider;
import org.locationtech.jts.geom.Point;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@SpringBootTest
record ShopQueryDslRepositoryTest(
        ShopCommandService shopCommandService,
        ShopRepository shopRepository,
        SubscribeShopRepository subscribeShopRepository,
        ShopQueryDslRepository shopQueryDslRepository,
        KakaoGeocodingProvider kakaoGeocodingProvider
) {

    @DisplayName("매장 목록을 조회힌다.")
    @Test
    void getShopList() {
        var address1 = "경기 광주시 경충대로 1411 투썸플레이스";
        var address2 = "경기 광주시 역동로63번길 1";
        var address3 = "경기 광주시 경충대로1481번길 4 1층";

        var registerRequest = getShopRegisterServiceRequest("대훈이네 곱창", "010-1234-5678", "businessNumber1", address1);
        var registerRequest2 = getShopRegisterServiceRequest("문수네 곱창", "010-1234-5679", "businessNumber2", address2);
        var registerRequest3 = getShopRegisterServiceRequest("순재네 막창", "010-1234-5690", "businessNumber3", address3);

        var point1 = kakaoGeocodingProvider.transferToGeocode(address1);
        var point2 = kakaoGeocodingProvider.transferToGeocode(address2);
        var point3 = kakaoGeocodingProvider.transferToGeocode(address3);

        shopCommandService.register(registerRequest, point1, 1L);
        shopCommandService.register(registerRequest2, point2, 2L);
        shopCommandService.register(registerRequest3, point3, 3L);

        long memberId = 99L;
        // 맨처음 매장 구독
        Shop shop = shopRepository.findByMerchantId(1L);
        var subscribeShop = SubscribeShop.create(memberId, shop.getId());
        subscribeShopRepository.save(subscribeShop);

        var request = ShopReadServiceRequest.builder()
                .memberId(memberId)
                .sortBy(NEWEST)
                .longitude(127.271143598307)
                .latitude(37.4013921881645)
                .build();

        ShopListResponse response = shopQueryDslRepository.getShopList(request.toDomainRequest());

        assertThat(response)
                .extracting(ShopListResponse::hasNext, ShopListResponse::nextCursorId)
                .containsExactly(false, null);
        assertThat(response.responses()).hasSize(3);
        assertThat(response.responses().getLast())
                .extracting(
                        ShopPreviewResponse::shopId,
                        ShopPreviewResponse::shopName,
                        ShopPreviewResponse::mainImage,
                        ShopPreviewResponse::address,
                        ShopPreviewResponse::subscribeCount,
                        ShopPreviewResponse::isSubscribe
                )
                .containsExactly(
                        shop.getId(),
                        shop.getShopName(),
                        null,
                        shop.getAddress(),
                        1L,   /*subscribeCount*/
                        true /*isSubscribe*/
                );

        var distance = response.responses().getFirst().distance();
        log.info(distance.toString());
        assertThat(distance).isNotNull();
    }

    @DisplayName("총 3개의 가게 중 한가지만 조회 되도록 검색한다.")
    @Test
    void searchShop() {
        var address1 = "경기 광주시 경충대로 1411 투썸플레이스";
        var address2 = "경기 광주시 역동로63번길 1";
        var address3 = "경기 광주시 경충대로1481번길 4 1층";

        var registerRequest = getShopRegisterServiceRequest("대훈이네 곱창", "010-1234-5678", "businessNumber1", address1);
        var registerRequest2 = getShopRegisterServiceRequest("문수네 곱창", "010-1234-5679", "businessNumber2", address2);
        var registerRequest3 = getShopRegisterServiceRequest("순재네 막창", "010-1234-5690", "businessNumber3", address3);

        var point1 = kakaoGeocodingProvider.transferToGeocode(address1);
        var point2 = kakaoGeocodingProvider.transferToGeocode(address2);
        var point3 = kakaoGeocodingProvider.transferToGeocode(address3);

        shopCommandService.register(registerRequest, point1, 1L);
        shopCommandService.register(registerRequest2, point2, 2L);
        shopCommandService.register(registerRequest3, point3, 3L);

        long memberId = 99L;
        // 구독
        Shop shop = shopRepository.findByMerchantId(1L);
        var subscribeShop = SubscribeShop.create(memberId, shop.getId());
        subscribeShopRepository.save(subscribeShop);

        var request = ShopReadServiceRequest.builder()
                .memberId(memberId)
                .q("대훈이네")
                .sortBy(NEWEST)
                .longitude(127.271143598307)
                .latitude(37.4013921881645)
                .build();

        ShopListResponse response = shopQueryDslRepository.getShopList(request.toDomainRequest());

        assertThat(response)
                .extracting(ShopListResponse::hasNext, ShopListResponse::nextCursorId)
                .containsExactly(false, null);
        assertThat(response.responses()).hasSize(1);
        assertThat(response.responses().getFirst())
                .extracting(
                        ShopPreviewResponse::shopId,
                        ShopPreviewResponse::shopName,
                        ShopPreviewResponse::mainImage,
                        ShopPreviewResponse::address,
                        ShopPreviewResponse::subscribeCount,
                        ShopPreviewResponse::isSubscribe
                )
                .containsExactly(
                        shop.getId(),
                        shop.getShopName(),
                        null,
                        shop.getAddress(),
                        1L,   /*subscribeCount*/
                        true /*isSubscribe*/
                );

        var distance = response.responses().getFirst().distance();
        log.info(distance.toString());
        assertThat(distance).isNotNull();
    }

    @DisplayName("거리순으로 정렬한다.")
    @Test
    void sortByDistance() {
        var address1 = "경기 광주시 경충대로 1411 투썸플레이스";
        var address2 = "경기 광주시 역동로63번길 1";
        var address3 = "경기 광주시 경충대로1481번길 4 1층";

        var registerRequest = getShopRegisterServiceRequest("대훈이네 곱창", "010-1234-5678", "businessNumber1", address1);
        var registerRequest2 = getShopRegisterServiceRequest("문수네 곱창", "010-1234-5679", "businessNumber2", address2);
        var registerRequest3 = getShopRegisterServiceRequest("순재네 막창", "010-1234-5690", "businessNumber3", address3);

        var point1 = kakaoGeocodingProvider.transferToGeocode(address1);
        var point2 = kakaoGeocodingProvider.transferToGeocode(address2);
        var point3 = kakaoGeocodingProvider.transferToGeocode(address3);

        shopCommandService.register(registerRequest, point1, 1L);
        shopCommandService.register(registerRequest2, point2, 2L);
        shopCommandService.register(registerRequest3, point3, 3L); // 얘가 가장 가까움.

        long memberId = 99L;
        var request = ShopReadServiceRequest.builder()
                .memberId(memberId)
                .sortBy(NEAREST)
                .longitude(127.271143598307)
                .latitude(37.4013921881645)
                .build();

        ShopListResponse response = shopQueryDslRepository.getShopList(request.toDomainRequest());

        List<Double> distances = response.responses().stream()
                .map(ShopPreviewResponse::distance)
                .toList();
        for (int i = 1; i < distances.size(); i++) {
            assertThat(distances.get(i)).isGreaterThanOrEqualTo(distances.get(i - 1));
        }

        // 로그로 실제 거리 값 확인
        distances.forEach(distance -> log.info("Distance: {}m", distance));
    }

    @DisplayName("구독순으로 정렬한다.")
    @Test
    void sortBySubscribe() {
        var address1 = "경기 광주시 경충대로 1411 투썸플레이스";
        var address2 = "경기 광주시 역동로63번길 1";
        var address3 = "경기 광주시 경충대로1481번길 4 1층";

        var registerRequest = getShopRegisterServiceRequest("대훈이네 곱창", "010-1234-5678", "businessNumber1", address1);
        var registerRequest2 = getShopRegisterServiceRequest("문수네 곱창", "010-1234-5679", "businessNumber2", address2);
        var registerRequest3 = getShopRegisterServiceRequest("순재네 막창", "010-1234-5690", "businessNumber3", address3);

        var point1 = kakaoGeocodingProvider.transferToGeocode(address1);
        var point2 = kakaoGeocodingProvider.transferToGeocode(address2);
        var point3 = kakaoGeocodingProvider.transferToGeocode(address3);

        shopCommandService.register(registerRequest, point1, 1L);
        shopCommandService.register(registerRequest2, point2, 2L);
        shopCommandService.register(registerRequest3, point3, 3L);

        long memberId = 99L;
        // 구독
        Shop shop = shopRepository.findByMerchantId(1L);
        Shop shop2 = shopRepository.findByMerchantId(3L);
        var subscribeShop = SubscribeShop.create(memberId, shop.getId());
        var subscribeShop2 = SubscribeShop.create(200L, shop.getId());
        var subscribeShop3 = SubscribeShop.create(200L, shop2.getId());
        subscribeShopRepository.save(subscribeShop);
        subscribeShopRepository.save(subscribeShop2);
        subscribeShopRepository.save(subscribeShop3);

        var request = ShopReadServiceRequest.builder()
                .memberId(memberId)
                .sortBy(MOST_SUBSCRIBED)
                .longitude(127.271143598307)
                .latitude(37.4013921881645)
                .build();

        ShopListResponse response = shopQueryDslRepository.getShopList(request.toDomainRequest());

        assertThat(response.responses()).hasSize(3);

        assertThat(response.responses().get(0))
                .extracting(ShopPreviewResponse::shopName, ShopPreviewResponse::subscribeCount, ShopPreviewResponse::isSubscribe)
                .containsExactly("대훈이네 곱창", 2L, true);  // memberId=99L이 구독중

        assertThat(response.responses().get(1))
                .extracting(ShopPreviewResponse::shopName, ShopPreviewResponse::subscribeCount, ShopPreviewResponse::isSubscribe)
                .containsExactly("순재네 막창", 1L, false);  // memberId=99L이 구독안함

        assertThat(response.responses().get(2))
                .extracting(ShopPreviewResponse::shopName, ShopPreviewResponse::subscribeCount, ShopPreviewResponse::isSubscribe)
                .containsExactly("문수네 곱창", 0L, false); // memberId=99L이 구독안함

    }

    @DisplayName("최신순 커서 페이징을 조회한다.")
    @Test
    void getListSecPage() {
        // 1. 21개 이상의 테스트 데이터 생성 (hasNext 확인용)
        createTestShops(25); // 25개 매장 생성

        long memberId = 99L;

        // 2. 첫 번째 페이지 조회
        var firstRequest = ShopReadServiceRequest.builder()
                .memberId(memberId)
                .sortBy(NEWEST) // ID 내림차순
                .longitude(127.271143598307)
                .latitude(37.4013921881645)
                .build();

        ShopListResponse firstPage = shopQueryDslRepository.getShopList(firstRequest.toDomainRequest());

        // 첫 페이지 검증
        assertThat(firstPage.responses()).hasSize(20); // PAGE_SIZE만큼
        assertThat(firstPage.hasNext()).isTrue();      // 다음 페이지 있음
        assertThat(firstPage.nextCursorId()).isNotNull(); // 커서 ID 존재

        // 3. 두 번째 페이지 조회 (커서 사용)
        var secondRequest = ShopReadServiceRequest.builder()
                .memberId(memberId)
                .sortBy(NEWEST)
                .lastShopId(firstPage.nextCursorId()) // 첫 페이지의 커서 사용
                .longitude(127.271143598307)
                .latitude(37.4013921881645)
                .build();

        ShopListResponse secondPage = shopQueryDslRepository.getShopList(secondRequest.toDomainRequest());

        // 두 번째 페이지 검증
        assertThat(secondPage.responses()).hasSize(5);  // 나머지 5개
        assertThat(secondPage.hasNext()).isFalse();     // 마지막 페이지
        assertThat(secondPage.nextCursorId()).isNull(); // 커서 없음

        // 4. 페이지 간 중복 확인
        Set<Long> firstPageIds = firstPage.responses().stream()
                .map(ShopPreviewResponse::shopId)
                .collect(Collectors.toSet());

        Set<Long> secondPageIds = secondPage.responses().stream()
                .map(ShopPreviewResponse::shopId)
                .collect(Collectors.toSet());

        // 중복 없어야 함
        assertThat(Collections.disjoint(firstPageIds, secondPageIds)).isTrue();

        // 5. 정렬 순서 검증 (NEWEST = ID 내림차순)
        Long lastIdFromFirstPage = firstPage.responses().get(19).shopId();
        Long firstIdFromSecondPage = secondPage.responses().getFirst().shopId();

        assertThat(firstIdFromSecondPage).isLessThan(lastIdFromFirstPage);

        log.info("=== 첫 번째 페이지 ===");
        firstPage.responses().forEach(shop ->
                log.info("ID: {}, 이름: {}", shop.shopId(), shop.shopName()));

        log.info("=== 두 번째 페이지 ===");
        secondPage.responses().forEach(shop ->
                log.info("ID: {}, 이름: {}", shop.shopId(), shop.shopName()));
    }

    @DisplayName("거리순 복합 커서 페이징을 조회한다.")
    @Test
    void getListSecPageByDistanceWithCompositeCursor() {
        // 1. 25개 매장 생성 (일부는 같은 거리에 위치)
        createTestShopsWithSameDistance(25);

        long memberId = 99L;

        // 2. 첫 번째 페이지 조회 (거리순)
        var firstRequest = ShopReadServiceRequest.builder()
                .memberId(memberId)
                .sortBy(NEAREST)
                .longitude(127.271143598307)
                .latitude(37.4013921881645)
                .build();

        ShopListResponse firstPage = shopQueryDslRepository.getShopList(firstRequest.toDomainRequest());

        // 첫 페이지 검증
        assertThat(firstPage.responses()).hasSize(20);
        assertThat(firstPage.hasNext()).isTrue();
        assertThat(firstPage.nextCursorId()).isNotNull();

        // 마지막 항목의 복합 커서 추출
        ShopPreviewResponse lastShop = firstPage.responses().get(19);
        Double lastDistance = lastShop.distance();
        Long lastShopId = lastShop.shopId();

        log.info("첫 페이지 마지막 항목 - 거리: {}m, ID: {}", lastDistance, lastShopId);

        // 3. 두 번째 페이지 조회 (복합 커서 사용)
        var secondRequest = ShopReadServiceRequest.builder()
                .memberId(memberId)
                .sortBy(NEAREST)
                .lastShopId(lastShopId)       // ID 커서
                .lastDistance(lastDistance)   // 거리 커서 (복합)
                .longitude(127.271143598307)
                .latitude(37.4013921881645)
                .build();

        ShopListResponse secondPage = shopQueryDslRepository.getShopList(secondRequest.toDomainRequest());

        // 두 번째 페이지 검증
        assertThat(secondPage.responses()).hasSize(5);
        assertThat(secondPage.hasNext()).isFalse();
        assertThat(secondPage.nextCursorId()).isNull();

        // 4. 복합 커서 정렬 순서 검증
        ShopPreviewResponse firstShopFromSecondPage = secondPage.responses().getFirst();
        Double firstDistance = firstShopFromSecondPage.distance();
        Long firstId = firstShopFromSecondPage.shopId();

        // 거리가 더 멀거나, 같은 거리면 ID가 더 큰 것
        if (firstDistance.equals(lastDistance)) {
            assertThat(firstId).isGreaterThan(lastShopId);
            log.info("같은 거리({})에서 ID 비교: {} > {}", firstDistance, firstId, lastShopId);
        } else {
            assertThat(firstDistance).isGreaterThan(lastDistance);
            log.info("거리 비교: {}m > {}m", firstDistance, lastDistance);
        }

        // 5. 전체 정렬 순서 연속성 확인
        List<ShopPreviewResponse> allShops = new ArrayList<>();
        allShops.addAll(firstPage.responses());
        allShops.addAll(secondPage.responses());

        // 거리 오름차순, 같은 거리면 ID 오름차순 확인
        for (int i = 1; i < allShops.size(); i++) {
            ShopPreviewResponse current = allShops.get(i);
            ShopPreviewResponse previous = allShops.get(i - 1);

            if (current.distance().equals(previous.distance())) {
                // 같은 거리면 최신순
                assertThat(current.shopId()).isLessThan(previous.shopId());
            } else {
                // 다른 거리면 거리 가까운순
                assertThat(current.distance()).isGreaterThan(previous.distance());
            }
        }
    }

    private ShopRegisterServiceRequest getShopRegisterServiceRequest(
            String title,
            String shopPhoneNumber,
            String businessNumber,
            String address
    ) {
        List<OperationTimeServiceRequest> timeServiceRequests = List.of(
                getOperationTimeServiceRequest(DayOfWeek.MONDAY, false),
                getOperationTimeServiceRequest(DayOfWeek.TUESDAY, false),
                getOperationTimeServiceRequest(DayOfWeek.WEDNESDAY, false),
                getOperationTimeServiceRequest(DayOfWeek.THURSDAY, false),
                getOperationTimeServiceRequest(DayOfWeek.FRIDAY, false),
                getOperationTimeServiceRequest(DayOfWeek.SATURDAY, false),
                getOperationTimeServiceRequest(DayOfWeek.SUNDAY, true)
        );

        return new ShopRegisterServiceRequest(
                title,
                ShopType.FOOD,
                shopPhoneNumber,
                address,
                businessNumber,
                "홍길동",
                null,      // mainImage
                List.of(), // imageFiles
                timeServiceRequests
        );
    }

    private OperationTimeServiceRequest getOperationTimeServiceRequest(DayOfWeek dayOfWeek, boolean isClosed) {
        return new OperationTimeServiceRequest(
                dayOfWeek,
                "09:00",
                "22:00",
                isClosed
        );
    }

    private void createTestShops(int count) {
        for (int i = 1; i <= count; i++) {
            var registerRequest = getShopRegisterServiceRequest(
                    "테스트매장" + i,
                    "010-1234-" + String.format("%04d", i),  // 4자리로 포맷,
                    "businessNumber" + i,
                    "경기 광주시 테스트주소 " + i
            );

            // 다양한 위치 생성 (약간씩 다른 좌표)
            Point point = GeoFixture.createPoint(
                    127.271000 + (i * 0.001),  // 경도 약간씩 변경
                    37.401000 + (i * 0.001)    // 위도 약간씩 변경
            );

            shopCommandService.register(registerRequest, point, (long) i);
        }
    }

    private void createTestShopsWithSameDistance(int count) {
        for (int i = 1; i <= count; i++) {
            var registerRequest = getShopRegisterServiceRequest(
                    "테스트매장" + i,
                    "010-1234-" + String.format("%04d", i),
                    "businessNumber" + i,
                    "경기 광주시 테스트주소 " + i
            );

            // 일부 매장들을 같은 거리에 위치시킴
            double lonOffset = (i % 5 == 0) ? 0.001 : (i * 0.001);  // 5의 배수는 같은 경도
            double latOffset = (i % 5 == 0) ? 0.001 : (i * 0.001);  // 5의 배수는 같은 위도

            Point point = GeoFixture.createPoint(
                    127.271000 + lonOffset,
                    37.401000 + latOffset
            );

            shopCommandService.register(registerRequest, point, (long) i);
        }
    }
}