package org.kwakmunsu.dingdongpang.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.member.entity.Member;
import org.kwakmunsu.dingdongpang.domain.member.repository.MemberRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
record MemberQueryServiceTest(MemberQueryService memberQueryService, MemberRepository memberRepository) {

    @DisplayName("닉네임 중복 확인")
    @Test
    void nicknameCheck() {
        var response = memberQueryService.isExistsNickname("nickname");
        assertThat(response.existsNickname()).isFalse();

        var member = Member.createMember("email", "nickname", "1234");
        memberRepository.save(member);

        var response2 = memberQueryService.isExistsNickname("nickname");
        assertThat(response2.existsNickname()).isTrue();
    }

}