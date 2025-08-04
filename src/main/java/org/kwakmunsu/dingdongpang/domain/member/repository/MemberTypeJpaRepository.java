package org.kwakmunsu.dingdongpang.domain.member.repository;

import org.kwakmunsu.dingdongpang.domain.member.entity.MemberStatus;
import org.kwakmunsu.dingdongpang.domain.member.entity.MemberType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTypeJpaRepository extends JpaRepository<MemberType, Long> {

    boolean existsByMemberIdAndStatus(Long memberId, MemberStatus status);

}