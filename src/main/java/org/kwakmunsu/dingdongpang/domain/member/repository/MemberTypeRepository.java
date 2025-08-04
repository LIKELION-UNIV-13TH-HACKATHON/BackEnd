package org.kwakmunsu.dingdongpang.domain.member.repository;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.member.entity.MemberStatus;
import org.kwakmunsu.dingdongpang.domain.member.entity.MemberType;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberTypeRepository {

    private final MemberTypeJpaRepository memberTypeJpaRepository;

    public void save(MemberType memberType) {
        memberTypeJpaRepository.save(memberType);
    }

    public boolean existsByMemberIdAndStatus(Long memberId, MemberStatus status) {
        return memberTypeJpaRepository.existsByMemberIdAndStatus(memberId, status);
    }

}