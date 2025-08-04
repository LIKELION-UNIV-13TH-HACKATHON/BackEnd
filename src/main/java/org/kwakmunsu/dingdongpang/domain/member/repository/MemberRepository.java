package org.kwakmunsu.dingdongpang.domain.member.repository;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.member.entity.Member;
import org.kwakmunsu.dingdongpang.domain.member.entity.Role;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    public Optional<Member> findBySocialIdAndRole(String socialId, Role role) {
        return memberJpaRepository.findBySocialIdAndRole(socialId, role);
    }

    public void save(Member guest) {
        memberJpaRepository.save(guest);
    }

}