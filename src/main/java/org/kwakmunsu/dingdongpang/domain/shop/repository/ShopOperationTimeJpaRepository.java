package org.kwakmunsu.dingdongpang.domain.shop.repository;

import java.util.List;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopOperationTime;
import org.kwakmunsu.dingdongpang.domain.shop.repository.dto.ShopOperationTimeResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShopOperationTimeJpaRepository extends JpaRepository<ShopOperationTime, Long> {

    @Query("select new org.kwakmunsu.dingdongpang.domain.shop.repository.dto."
            + "ShopOperationTimeResponse(ot.dayOfWeek,  ot.openTime, ot.closeTime, ot.isClosed)" +
            "from ShopOperationTime ot where ot.shopId = :shopId"
    )
    List<ShopOperationTimeResponse> findByShopId(@Param("shopId") Long shopId);
}