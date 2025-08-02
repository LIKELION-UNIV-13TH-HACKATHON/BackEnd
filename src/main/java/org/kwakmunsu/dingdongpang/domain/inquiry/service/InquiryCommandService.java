package org.kwakmunsu.dingdongpang.domain.inquiry.service;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.inquiry.repository.InquiryRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class InquiryCommandService {

    private final InquiryRepository inquiryRepository;

}