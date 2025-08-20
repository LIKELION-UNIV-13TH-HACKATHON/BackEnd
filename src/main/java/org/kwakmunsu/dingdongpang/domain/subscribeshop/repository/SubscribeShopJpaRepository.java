package org.kwakmunsu.dingdongpang.domain.subscribeshop.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.entity.SubscribeShop;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto.SubscribeShopPreviewResponse;
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

    Long countByShopIdAndCreatedAtBetween(Long shopId, LocalDateTime start, LocalDateTime end);

    Long countByShopId(Long shopId);

    @Query("SELECT new org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto.SubscribeShopPreviewResponse(" +
            "ss.id, ss.shopId, s.shopName, s.mainImage) " +
            "FROM SubscribeShop ss " +
            "JOIN Shop s ON ss.shopId = s.id " +
            "WHERE ss.memberId = :memberId " +
            "ORDER BY ss.id DESC"
    )
    List<SubscribeShopPreviewResponse> getSubscribedShopList(@Param("memberId") Long memberId);

}