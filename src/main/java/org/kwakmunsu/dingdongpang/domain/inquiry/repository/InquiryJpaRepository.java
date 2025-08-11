package org.kwakmunsu.dingdongpang.domain.inquiry.repository;

import java.util.List;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InquiryJpaRepository extends JpaRepository<Inquiry, Long> {

    List<Inquiry> findByShopId(Long shopId);

    List<Inquiry> findByShopIdAndAuthorId(Long shopId, Long authorId);

    @Query("select i from Inquiry i join fetch i.author where i.shopId = :shopId")
    List<Inquiry> findByShopIdForMerchant(@Param("shopId") Long shopId);
}