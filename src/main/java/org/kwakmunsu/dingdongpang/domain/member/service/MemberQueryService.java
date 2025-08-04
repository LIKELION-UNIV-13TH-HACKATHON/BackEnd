package org.kwakmunsu.dingdongpang.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.member.repository.MemberRepository;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.CheckNicknameResponse;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberQueryService {

    private final MemberRepository memberRepository;

    public CheckNicknameResponse isExistsNickname(String nickname) {
        boolean exists = memberRepository.existsByNickname(nickname);

        return new CheckNicknameResponse(exists);
    }

}