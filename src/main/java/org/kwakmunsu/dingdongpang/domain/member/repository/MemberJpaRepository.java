package org.kwakmunsu.dingdongpang.domain.member.repository;

import org.kwakmunsu.dingdongpang.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

}