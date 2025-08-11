package org.kwakmunsu.dingdongpang.domain.inquiry.service;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.dingdongpang.domain.inquiry.repository.InquiryRepository;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.InquiryRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class InquiryCommandService {

    private final InquiryRepository inquiryRepository;
    private final ShopRepository shopRepository;

    public void register(InquiryRegisterServiceRequest request) {
        shopRepository.findById(request.shopId());

        Inquiry inquiry = Inquiry.create(request.shopId(), request.memberId(), request.question());
        inquiryRepository.save(inquiry);
    }

}