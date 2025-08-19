package org.kwakmunsu.dingdongpang.domain.subscribeshop.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.entity.SubscribeShop;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto.DailySubscriptionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubscribeShopJpaRepository extends JpaRepository<SubscribeShop, Long> {

    boolean existsByMemberIdAndShopId(Long memberId, Long shopId);

    void deleteByMemberIdAndShopId(Long memberId, Long shopId);

    @Query("SELECT f.token " +
            "FROM SubscribeShop s " +
            "JOIN FcmToken f ON s.memberId = f.memberId " +
            "WHERE s.shopId = :shopId " +
            "AND f.isEnabled = true"
    )
    List<String> findFcmTokensByShopId(Long shopId);

    List<SubscribeShop> findByShopId(Long shopId);

    // 특정 매장의 오늘 신규 구독자 수
    Long countByShopIdAndCreatedAtBetween(Long shopId, LocalDateTime start, LocalDateTime end);

    // 특정 매장의 전체 구독자 수
    Long countByShopId(Long shopId);

    @Query("SELECT new org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto.DailySubscriptionResponse(" +
            "CAST(s.createdAt AS java.time.LocalDate), COUNT(s)) " +
            "FROM SubscribeShop s " +
            "WHERE s.shopId = :shopId " +
            "AND s.createdAt >= :startDate " +
            "AND s.createdAt < :endDate " +
            "GROUP BY CAST(s.createdAt AS java.time.LocalDate) " +
            "ORDER BY CAST(s.createdAt AS java.time.LocalDate) ASC")
    List<DailySubscriptionResponse> getWeeklySubscriptions(
            @Param("shopId") Long shopId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


}