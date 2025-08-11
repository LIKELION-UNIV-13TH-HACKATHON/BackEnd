package org.kwakmunsu.dingdongpang.domain.inquiry.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.Inquiry;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class InquiryRepository {

    private final InquiryJpaRepository inquiryJpaRepository;

    public void save(Inquiry inquiry) {
        inquiryJpaRepository.save(inquiry);
    }

    public void findById(Long id) {

    }

    public List<Inquiry> findByShopId(Long shopId) {
        return inquiryJpaRepository.findByShopId(shopId);
    }

    public void findByAuthorId(Long authorId) {

    }

}