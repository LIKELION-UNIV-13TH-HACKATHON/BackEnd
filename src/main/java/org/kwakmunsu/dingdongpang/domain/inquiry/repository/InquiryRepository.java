package org.kwakmunsu.dingdongpang.domain.inquiry.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.dingdongpang.global.exception.NotFoundException;
import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class InquiryRepository {

    private final InquiryJpaRepository inquiryJpaRepository;

    public void save(Inquiry inquiry) {
        inquiryJpaRepository.save(inquiry);
    }

    public Inquiry findById(Long id) {
        return inquiryJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_INQUIRY));
    }

    public List<Inquiry> findByShopId(Long shopId) {
        return inquiryJpaRepository.findByShopId(shopId);
    }

    public List<Inquiry> findByShopIdAndAuthorId(Long shopId, Long authorId) {
        return inquiryJpaRepository.findByShopIdAndAuthorId(shopId, authorId);
    }

    public List<Inquiry> findByShopIdForMerchant(Long shopId) {
        return inquiryJpaRepository.findByShopIdForMerchant(shopId);
    }

}