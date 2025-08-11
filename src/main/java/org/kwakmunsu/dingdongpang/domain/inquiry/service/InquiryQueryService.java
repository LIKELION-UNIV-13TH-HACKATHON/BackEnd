package org.kwakmunsu.dingdongpang.domain.inquiry.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.InquiryFilter;
import org.kwakmunsu.dingdongpang.domain.inquiry.repository.InquiryRepository;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.InquiryListResponse;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.InquiryPreviewResponse;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.InquiryReadServiceRequest;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class InquiryQueryService {

    private final InquiryRepository inquiryRepository;

    public InquiryListResponse getInquiryList(InquiryReadServiceRequest request) {
        List<Inquiry> inquires = getInquiresByFilter(request);

        List<InquiryPreviewResponse> responses = inquires.stream()
                .map(InquiryPreviewResponse::of)
                .toList();
        return new InquiryListResponse(responses);
    }


    private List<Inquiry> getInquiresByFilter(InquiryReadServiceRequest request) {
        InquiryFilter filter = request.filter();

        return switch (filter) {
            case GENERAL -> inquiryRepository.findByShopId(request.shopId());
            case MY -> inquiryRepository.findByShopIdAndAuthorId(request.shopId(), request.memberId());
        };
    }

}