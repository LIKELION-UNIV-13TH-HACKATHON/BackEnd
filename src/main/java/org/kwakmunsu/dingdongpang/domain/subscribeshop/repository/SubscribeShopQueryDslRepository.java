package org.kwakmunsu.dingdongpang.domain.subscribeshop.repository;

import static com.querydsl.core.types.Projections.constructor;
import static org.kwakmunsu.dingdongpang.domain.shop.entity.QShop.shop;
import static org.kwakmunsu.dingdongpang.domain.subscribeshop.entity.QSubscribeShop.subscribeShop;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto.SubscribeShopListResponse;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto.SubscribeShopPreviewResponse;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto.SubscribeShopReadDomainRequest;
import org.springframework.stereotype.Repository;

@Slf4j
@RequiredArgsConstructor
@Repository
public class SubscribeShopQueryDslRepository {

    private static final int PAGE_SIZE = 20;
    private final JPAQueryFactory queryFactory;

    public SubscribeShopListResponse getSubscribedShopList(SubscribeShopReadDomainRequest request) {
        NumberTemplate<Double> distanceExpr = distanceM(request.longitude(), request.latitude());

        JPAQuery<SubscribeShopPreviewResponse> query = queryFactory.select(
                        constructor(SubscribeShopPreviewResponse.class,
                                shop.id.as("shopId"),
                                shop.shopName,
                                shop.mainImage,
                                shop.address,
                                subscribeShop.shopId.countDistinct(),
                                distanceExpr
                        )
                )
                .from(shop)
                .innerJoin(subscribeShop).on(subscribeShop.shopId.eq(shop.id)
                        .and(subscribeShop.memberId.eq(request.memberId()))
                )
                .where(
                        cursorIdCondition(request.lastShopId())
                )
                .groupBy(shop.id, shop.shopName, shop.mainImage, shop.address);

        List<SubscribeShopPreviewResponse> responses = query
                .orderBy(shop.id.desc())
                .limit(PAGE_SIZE + 1)
                .fetch();

        boolean hasNext = responses.size() > PAGE_SIZE;
        List<SubscribeShopPreviewResponse> limitedPage = getLimitedPage(responses, hasNext);
        Long nextCursorOrNull = getNextCursorOrNull(limitedPage, hasNext);

        return SubscribeShopListResponse.builder()
                .responses(limitedPage)
                .nextCursorId(nextCursorOrNull)
                .hasNext(hasNext)
                .build();
    }

    /**
     * 표준: SRID 4326에서는 POINT(longitude latitude) 순서 MySQL 구현: 내부적으로 다른 순서로 해석하는 경우 발생 따라서 index {2} , {1} 순서로 변경
     **/
    private NumberTemplate<Double> distanceM(Double userLon, Double userLat) {
        log.info(userLon.toString());
        log.info(userLat.toString());
        return Expressions.numberTemplate(Double.class,
                "ST_Distance_Sphere({0}, ST_GeomFromText(CONCAT('POINT(', {2}, ' ', {1}, ')'), 4326))",
                shop.location, userLon, userLat);
    }

    private BooleanExpression cursorIdCondition(Long lastShopId) {
        if (lastShopId == null) {
            return null;
        }
        return shop.id.lt(lastShopId);
    }

    private List<SubscribeShopPreviewResponse> getLimitedPage(List<SubscribeShopPreviewResponse> responses, boolean hasNext) {
        if (hasNext) {
            return responses.subList(0, PAGE_SIZE); // 실제로는 limit 만큼만 반환
        }
        return responses;
    }

    private Long getNextCursorOrNull(List<SubscribeShopPreviewResponse> responses, boolean hasNext) {
        if (hasNext) {
            return responses.getLast().shopId();
        }
        return null;
    }

}