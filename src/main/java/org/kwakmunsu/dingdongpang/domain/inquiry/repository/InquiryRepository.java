package org.kwakmunsu.dingdongpang.domain.inquiry.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class InquiryRepository {

    private final InquiryJpaRepository inquiryJpaRepository;

}