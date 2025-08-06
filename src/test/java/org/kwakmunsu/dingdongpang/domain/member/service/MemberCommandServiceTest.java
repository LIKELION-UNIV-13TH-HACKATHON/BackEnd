package org.kwakmunsu.dingdongpang.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.member.entity.Member;
import org.kwakmunsu.dingdongpang.domain.member.entity.MemberStatus;
import org.kwakmunsu.dingdongpang.domain.member.entity.Role;
import org.kwakmunsu.dingdongpang.domain.member.repository.MemberRepository;
import org.kwakmunsu.dingdongpang.domain.member.repository.MemberTypeRepository;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.CustomerRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.global.exception.DuplicationException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
record MemberCommandServiceTest(
        MemberCommandService memberCommandService,
        MemberRepository memberRepository,
        MemberTypeRepository memberTypeRepository,
        EntityManager entityManager
) {

    @DisplayName("고객 상태로 회원을 등록한다.")
    @Test
    void registerCustomer() {
        var guest = Member.createGuest("test-email@gmail.com", "nickname", "123456");
        memberRepository.save(guest);

        assertThat(guest.getRole()).isEqualTo(Role.ROLE_GUEST);

        var request = new CustomerRegisterServiceRequest("new-nickname", guest.getId());

        memberCommandService.registerCustomer(request);
        entityManager.flush();
        entityManager.clear();

        var member = memberRepository.findById(request.memberId());
        assertThat(member.getRole()).isEqualTo(Role.ROLE_MEMBER);

        // True 라면 테이블에 정상 등록
        boolean isExistsCustomer = memberTypeRepository.existsByMemberIdAndStatus(member.getId(), MemberStatus.CUSTOMER);
        assertThat(isExistsCustomer).isTrue();
    }

    @DisplayName("중복된 닉네임으로 고객 등록에 실패한다.")
    @Test
    void failRegisterCustomerWhenNicknameIsDuplicated() {
        var guest = Member.createGuest("test-email@gmail.com", "nickname", "123456");
        var duplicateGuest = Member.createGuest("test2-email@gmail.com", "testname2", "12345sada6");
        memberRepository.save(guest);
        memberRepository.save(duplicateGuest);
        var request = new CustomerRegisterServiceRequest(guest.getNickname(), duplicateGuest.getId());

        assertThatThrownBy(() -> memberCommandService.registerCustomer(request))
            .isInstanceOf(DuplicationException.class);
    }

    @DisplayName("이미 고객 상태로 등록이 되어 있을 경우 고객 등록에 실패한다.")
    @Test
    void failRegisterCustomerWhenAlreadyCustomer() {
        var guest = Member.createGuest("test-email@gmail.com", "nickname", "123456");
        memberRepository.save(guest);
        var request = new CustomerRegisterServiceRequest("new-nickname", guest.getId());

        assertThat(guest.getRole()).isEqualTo(Role.ROLE_GUEST);
        // 중복을 위한 사전 등록
        memberCommandService.registerCustomer(request);
        entityManager.flush();

        var request2 = new CustomerRegisterServiceRequest("new-nickname2", guest.getId());

        assertThatThrownBy(() ->memberCommandService.registerCustomer(request2) )
            .isInstanceOf(DuplicationException.class);
    }

    @DisplayName("상인 역할의 회원을 생성한다.")
    @Test
    void registerMerchant () {
        Member guest = Member.createGuest("email@naver.com", "nickname", "12345");
        memberRepository.save(guest);

        assertThat(guest.getRole()).isEqualTo(Role.ROLE_GUEST);

        memberCommandService.registerMerchant("new-nickname", guest.getId());
        entityManager.flush();
        entityManager.clear();

        var member = memberRepository.findById(guest.getId());
        assertThat(member.getRole()).isEqualTo(Role.ROLE_MEMBER);

        // True 라면 테이블에 정상 등록
        boolean isExistsCustomer = memberTypeRepository.existsByMemberIdAndStatus(member.getId(), MemberStatus.MERCHANT);
        assertThat(isExistsCustomer).isTrue();
    }
}