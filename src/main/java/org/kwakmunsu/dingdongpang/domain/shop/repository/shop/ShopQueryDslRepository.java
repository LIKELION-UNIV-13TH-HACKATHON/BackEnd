package org.kwakmunsu.dingdongpang.domain.shop.repository.shop;

import static com.querydsl.core.types.Projections.constructor;
import static com.querydsl.jpa.JPAExpressions.select;
import static org.kwakmunsu.dingdongpang.domain.shop.entity.QShop.shop;
import static org.kwakmunsu.dingdongpang.domain.subscribeshop.entity.QSubscribeShop.subscribeShop;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.dingdongpang.domain.shop.entity.SortBy;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.dto.ShopListResponse;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.dto.ShopPreviewResponse;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.dto.ShopReadDomainRequest;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ShopQueryDslRepository {

    private static final int PAGE_SIZE = 20;
    private final JPAQueryFactory queryFactory;

    public ShopListResponse getShopList(ShopReadDomainRequest request) {
        SortBy sort = request.sortBy() == null ? SortBy.NEWEST : request.sortBy();

        NumberTemplate<Double> distanceExpr = distanceM(request.longitude(), request.latitude());
        JPAQuery<ShopPreviewResponse> query = queryFactory.select(
                        constructor(ShopPreviewResponse.class,
                                shop.id.as("shopId"),
                                shop.shopName,
                                shop.mainImage,
                                shop.address,
                                select(subscribeShop.count()).from(subscribeShop)
                                        .where(subscribeShop.shopId.eq(shop.id)),
                                select(subscribeShop.id).from(subscribeShop)
                                        .where(
                                                subscribeShop.shopId.eq(shop.id),
                                                subscribeShop.memberId.eq(request.memberId())
                                        )
                                        .limit(1)
                                        .isNotNull(),
                                distanceExpr
                        )
                )
                .from(shop)
                .where(
                        cursorIdCondition(sort, request, distanceExpr),
                        queryContains(request.q())
                );
        List<ShopPreviewResponse> responses = query
                .orderBy(getOrderSpecifiers(sort, distanceExpr))
                .limit(PAGE_SIZE + 1)
                .fetch();

        boolean hasNext = responses.size() > PAGE_SIZE;
        List<ShopPreviewResponse> limitedPage = getLimitedPage(responses, hasNext);
        Long nextCursorOrNull = getNextCursorOrNull(limitedPage, hasNext);

        return ShopListResponse.builder()
                .responses(limitedPage)
                .nextCursorId(nextCursorOrNull)
                .hasNext(hasNext)
                .build();
    }

    /**
     * 표준: SRID 4326에서는 POINT(longitude latitude) 순서
     * MySQL 구현: 내부적으로 다른 순서로 해석하는 경우 발생
     * 따라서 index {2} , {1} 순서로 변경
     * **/
    private NumberTemplate<Double> distanceM(Double userLon, Double userLat) {
        log.info(userLon.toString());
        log.info(userLat.toString());
        return Expressions.numberTemplate(Double.class,
                "ST_Distance_Sphere({0}, ST_GeomFromText(CONCAT('POINT(', {2}, ' ', {1}, ')'), 4326))",
                shop.location, userLon, userLat);
    }

    private BooleanExpression cursorIdCondition(SortBy sortBy, ShopReadDomainRequest request, NumberTemplate<Double> distanceExpr) {
        Long lastSubscribeCount = request.lastSubscribeCount();
        Long lastShopId = request.lastShopId();
        Double lastDistance = request.lastDistance();

        if (lastShopId == null) {
            return null;
        }

        return switch (sortBy) {
            case NEWEST:
                yield shop.id.lt(lastShopId);
            case MOST_SUBSCRIBED:
                if (lastSubscribeCount == null) {
                    yield null;
                }
                JPQLQuery<Long> subscribeCount = select(subscribeShop.count())
                        .from(subscribeShop)
                        .where(subscribeShop.shopId.eq(shop.id));
                yield subscribeCount.lt(lastSubscribeCount).or(subscribeCount.eq(lastSubscribeCount)
                        .and(shop.id.lt(lastShopId)));
            case NEAREST:
                if (lastDistance == null) {
                    yield null;
                }
                yield distanceExpr.gt(lastDistance)
                        .or(distanceExpr.eq(lastDistance).and(shop.id.gt(lastShopId)));
        };
    }

    private BooleanExpression queryContains(String query) {
        if (query == null || query.isBlank()) {
            return null;
        }
        return shop.shopName.contains(query);
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(SortBy option, NumberTemplate<Double> distanceExpr) {
        return switch (option) {
            case MOST_SUBSCRIBED -> {
                NumberTemplate<Long> subscribeCount = Expressions.numberTemplate(
                        Long.class,
                        "({0})",
                        select(subscribeShop.count()).from(subscribeShop)
                                .where(subscribeShop.shopId.eq(shop.id))
                );
                yield new OrderSpecifier[]{
                        subscribeCount.desc(), // 1순위: 구독 많은 순
                        shop.id.desc()         // 2순위: 최신순
                };
            }
            case NEWEST -> new OrderSpecifier[]{shop.id.desc()};
            case NEAREST -> new OrderSpecifier[]{
                    distanceExpr.asc(),    // 1순위: 거리 가까운 순
                    shop.id.desc()         // 2순위: 최신순 (ID 내림차순) ← 추가!
            };
        };
    }

    private List<ShopPreviewResponse> getLimitedPage(List<ShopPreviewResponse> responses, boolean hasNext) {
        if (hasNext) {
            return responses.subList(0, PAGE_SIZE); // 실제로는 limit 만큼만 반환
        }
        return responses;
    }

    private Long getNextCursorOrNull(List<ShopPreviewResponse> responses, boolean hasNext) {
        if (hasNext) {
            return responses.getLast().shopId();
        }
        return null;
    }

}