package org.kwakmunsu.dingdongpang.domain.shopimage.repository;

import java.util.List;
import org.kwakmunsu.dingdongpang.domain.shopimage.entity.ShopImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShopImageJpaRepository extends JpaRepository<ShopImage, Long> {

    @Query("select image from ShopImage where shopId = :shopId")
    List<String> findByShopId(@Param("shopId") Long shopId);

    void deleteByShopId(Long shopId);

}