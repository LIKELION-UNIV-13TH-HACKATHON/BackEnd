package org.kwakmunsu.dingdongpang.domain.inquiry.service;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.dingdongpang.domain.inquiry.repository.InquiryRepository;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.request.InquiryAnswerServiceRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.request.InquiryDeleteServiceRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.request.InquiryModifyServiceRequest;
import org.kwakmunsu.dingdongpang.domain.inquiry.service.dto.request.InquiryRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.member.entity.Member;
import org.kwakmunsu.dingdongpang.domain.member.repository.MemberRepository;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.kwakmunsu.dingdongpang.global.exception.ForbiddenException;
import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class InquiryCommandService {

    private final InquiryRepository inquiryRepository;
    private final ShopRepository shopRepository;
    private final MemberRepository memberRepository;

    public void register(InquiryRegisterServiceRequest request) {
        shopRepository.findById(request.shopId());
        Member author = memberRepository.findById(request.memberId());

        Inquiry inquiry = Inquiry.create(request.shopId(), author, request.title(), request.question());
        inquiryRepository.save(inquiry);
    }

    @Transactional
    public void registerAnswer(InquiryAnswerServiceRequest request) {
        Inquiry inquiry = inquiryRepository.findById(request.inquiryId());
        Shop shop = shopRepository.findById(request.shopId());

        if (shop.isMerchant(request.merchantId())) {
            inquiry.registerAnswer(request.answer());
            inquiry.updateStatusToCompleted();
            return;
        }
        throw new ForbiddenException(ErrorStatus.FORBIDDEN_ANSWER);
    }

    @Transactional
    public void modifyInquiry(InquiryModifyServiceRequest request) {
        Inquiry inquiry = inquiryRepository.findByIdAndAuthorId(request.inquiryId(), request.authorId());
        inquiry.updateQuestion(request.question());
    }

    @Transactional
    public void modifyAnswer(InquiryAnswerServiceRequest request) {
        Inquiry inquiry = inquiryRepository.findById(request.inquiryId());
        Shop shop = shopRepository.findById(request.shopId());

        if (shop.isMerchant(request.merchantId())) {
            inquiry.updateAnswer(request.answer());
            return;
        }
        throw new ForbiddenException(ErrorStatus.FORBIDDEN_MODIFY);
    }

    @Transactional
    public void delete(InquiryDeleteServiceRequest request) {
        boolean isAuthor = inquiryRepository.existsByIdAndAuthorId(request.inquiryId(), request.memberId());
        Shop shop = shopRepository.findById(request.shopId());

        // 관리자 || 문의 작성자 사람만이 삭제할 수 있다.
        if (shop.isMerchant(request.memberId()) || isAuthor) {
            inquiryRepository.deleteById(request.inquiryId());
            return;
        }
        throw new ForbiddenException(ErrorStatus.FORBIDDEN_DELETE);
    }

}