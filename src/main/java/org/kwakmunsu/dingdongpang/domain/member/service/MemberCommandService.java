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
        checkDuplicateNickname(request);
        Member member = memberRepository.findById(request.memberId());

        member.updateNickname(request.nickname());
        member.upgradeRoleToMember();

        registerMemberTypeToCustomer(member);
    }

    private void checkDuplicateNickname(CustomerRegisterServiceRequest request) {
        if (memberRepository.existsByNickname(request.nickname())) {
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

}