package org.kwakmunsu.dingdongpang.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.member.entity.Member;
import org.kwakmunsu.dingdongpang.domain.member.entity.MemberStatus;
import org.kwakmunsu.dingdongpang.domain.member.entity.MemberType;
import org.kwakmunsu.dingdongpang.domain.member.repository.MemberRepository;
import org.kwakmunsu.dingdongpang.domain.member.repository.MemberTypeRepository;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.CustomerRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.global.exception.DuplicationException;
import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberCommandService {

    private final MemberRepository memberRepository;
    private final MemberTypeRepository memberTypeRepository;

    @Transactional
    public void registerCustomer(CustomerRegisterServiceRequest request) {
        Member customer = memberRepository.findById(request.memberId());

        if (!customer.isEqualNickname(request.nickname())) {
            checkDuplicateNickname(request.nickname());
            customer.updateNickname(request.nickname());
        }

        customer.upgradeRoleToMember();

        registerMemberTypeToCustomer(customer);
    }

    @Transactional
    public Member registerMerchant(String nickname, Long memberId) {
        Member merchant = memberRepository.findById(memberId);

        if (!merchant.isEqualNickname(nickname)) {
            checkDuplicateNickname(nickname);
            merchant.updateNickname(nickname);
        }
        merchant.upgradeRoleToMember();

        registerMemberTypeToMerchant(merchant);

        return merchant;
    }

    private void checkDuplicateNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new DuplicationException(ErrorStatus.DUPLICATE_NICKNAME);
        }
    }

    private void registerMemberTypeToCustomer(Member member) {
        if (memberTypeRepository.existsByMemberIdAndStatus(member.getId(), MemberStatus.CUSTOMER)) {
            throw new DuplicationException(ErrorStatus.DUPLICATE_CUSTOMER);
        }
        MemberType customer = MemberType.createCustomer(member.getId());

        memberTypeRepository.save(customer);
    }

    private void registerMemberTypeToMerchant(Member member) {
        if (memberTypeRepository.existsByMemberIdAndStatus(member.getId(), MemberStatus.MERCHANT)) {
            throw new DuplicationException(ErrorStatus.DUPLICATE_MERCHANT);
        }
        MemberType merchant = MemberType.createMerchant(member.getId());

        memberTypeRepository.save(merchant);
    }

}