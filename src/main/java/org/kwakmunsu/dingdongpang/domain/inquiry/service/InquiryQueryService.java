package org.kwakmunsu.dingdongpang.domain.inquiry.service;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.inquiry.repository.InquiryRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class InquiryQueryService {

    private final InquiryRepository inquiryRepository;

}