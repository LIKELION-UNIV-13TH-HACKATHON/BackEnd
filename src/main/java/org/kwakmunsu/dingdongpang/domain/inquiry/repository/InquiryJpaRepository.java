package org.kwakmunsu.dingdongpang.domain.inquiry.repository;

import org.kwakmunsu.dingdongpang.domain.inquiry.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryJpaRepository extends JpaRepository<Inquiry, Long> {

}