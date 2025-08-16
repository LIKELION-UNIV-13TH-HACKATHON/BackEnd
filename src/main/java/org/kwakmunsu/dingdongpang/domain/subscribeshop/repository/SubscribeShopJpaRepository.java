package org.kwakmunsu.dingdongpang.domain.subscribeshop.repository;

import java.util.List;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.entity.SubscribeShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

}