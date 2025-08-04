package org.kwakmunsu.dingdongpang.domain.member.repository;

import java.util.Optional;
import org.kwakmunsu.dingdongpang.domain.member.entity.Member;
import org.kwakmunsu.dingdongpang.domain.member.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    Optional<Member> findBySocialIdAndRole(String socialId, Role role);

    boolean existsByNickname(String nickname);

}