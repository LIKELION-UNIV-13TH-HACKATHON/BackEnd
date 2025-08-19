package org.kwakmunsu.dingdongpang.domain.shop.repository.shop;

import java.util.List;
import java.util.Optional;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShopJpaRepository extends JpaRepository<Shop, Long> {

    boolean existsByBusinessNumber(String businessNumber);

    Optional<Shop> findByMerchantId(Long merchantId);

    @Query(value = "SELECT * FROM shop " +
            "WHERE ST_Distance_Sphere(" +
            " ST_GeomFromText(CONCAT('POINT(', :lat, ' ', :lon, ')'), 4326), " +  // SRID 4326 명시
            " location" +
            ") <= :radius",
            nativeQuery = true)
    List<Shop> findShopsWithinRadius(@Param("lon") Double longitude, @Param("lat") Double latitude, @Param("radius") int radius);

    boolean existsByIdAndMerchantId(Long shopId, Long merchantId);
}