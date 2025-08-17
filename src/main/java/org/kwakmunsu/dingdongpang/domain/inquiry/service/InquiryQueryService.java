package org.kwakmunsu.dingdongpang.domain.inquiry.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.InquiryFilter;
import org.kwakmunsu.dingdongpang.domain.inquiry.repository.InquiryRepository;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.InquiryReadServiceRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.response.InquiryByMerchantResponse;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.response.InquiryListByMerchantResponse;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.response.InquiryListResponse;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.response.InquiryResponse;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class InquiryQueryService {

    private final InquiryRepository inquiryRepository;
    private final ShopRepository shopRepository;

    public InquiryListResponse getInquiryList(InquiryReadServiceRequest request) {
        List<Inquiry> inquiries = getInquiresByFilter(request);

        List<InquiryResponse> responses = inquiries.stream()
                .map(InquiryResponse::of)
                .toList();
        return new InquiryListResponse(responses);
    }

    public InquiryListByMerchantResponse getInquiryList(Long merchantId) {
        Shop shop = shopRepository.findByMerchantId(merchantId);
        List<Inquiry> inquiries = inquiryRepository.findByShopIdForMerchant(shop.getId());

        List<InquiryByMerchantResponse> responses = inquiries.stream()
                .map(InquiryByMerchantResponse::of)
                .toList();
        return new InquiryListByMerchantResponse(responses);
    }

    private List<Inquiry> getInquiresByFilter(InquiryReadServiceRequest request) {
        InquiryFilter filter = request.filter();

        return switch (filter) {
            case GENERAL -> inquiryRepository.findByShopId(request.shopId());
            case MY -> inquiryRepository.findByShopIdAndAuthorId(request.shopId(), request.memberId());
        };
    }
}