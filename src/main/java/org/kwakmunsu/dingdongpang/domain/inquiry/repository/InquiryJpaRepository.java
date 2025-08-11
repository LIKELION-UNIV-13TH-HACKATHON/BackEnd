package org.kwakmunsu.dingdongpang.domain.inquiry.repository;

import java.util.List;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryJpaRepository extends JpaRepository<Inquiry, Long> {

    List<Inquiry> findByShopId(Long shopId);

    List<Inquiry> findByShopIdAndAuthorId(Long shopId, Long authorId);
}