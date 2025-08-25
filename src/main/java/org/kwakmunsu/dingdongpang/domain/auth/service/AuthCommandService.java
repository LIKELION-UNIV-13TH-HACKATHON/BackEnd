package org.kwakmunsu.dingdongpang.domain.auth.service;

import static org.kwakmunsu.dingdongpang.domain.member.entity.Role.ROLE_GUEST;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.auth.service.dto.SignInResponse;
import org.kwakmunsu.dingdongpang.domain.auth.service.dto.SignInServiceRequest;
import org.kwakmunsu.dingdongpang.domain.member.entity.Member;
import org.kwakmunsu.dingdongpang.domain.member.repository.MemberRepository;
import org.kwakmunsu.dingdongpang.global.jwt.JwtProvider;
import org.kwakmunsu.dingdongpang.global.jwt.dto.TokenResponse;
import org.kwakmunsu.dingdongpang.global.oauth.OAuth2UserInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthCommandService {

    private final OAuth2Provider oauth2Provider;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public SignInResponse signIn(SignInServiceRequest request) {
        OAuth2UserInfo oAuth2UserInfo = oauth2Provider.getOAuth2UserInfo(request.socialAccessToken());

        MemberSignInResult memberResult = findOrCreateMember(oAuth2UserInfo);
        TokenResponse tokenResponse = createTokens(memberResult.member());

        return new SignInResponse(
                memberResult.isNewMember(),
                memberResult.member().getId(),
                tokenResponse
        );
    }

    @Transactional
    public TokenResponse reissue(String refreshToken) {
        Member member = memberRepository.findByRefreshToken(refreshToken);

        TokenResponse response = jwtProvider.createTokens(member.getId(), member.getRole());
        member.updateRefreshToken(response.refreshToken());

        return response;
    }

    @Transactional
    public TokenResponse testReissue(Long memberId) {
        Member member = memberRepository.findById(memberId);

        TokenResponse response = jwtProvider.createTokens(member.getId(), member.getRole());
        member.updateRefreshToken(response.refreshToken());

        return response;
    }

    @Transactional
    public void signOut(Long memberId) {
        Member member = memberRepository.findById(memberId);
        member.updateRefreshToken(null);
        //TODO: 추후 FCM 토큰도 초기화, blacklist 추가
    }

    private MemberSignInResult findOrCreateMember(OAuth2UserInfo oAuth2UserInfo) {
        Optional<Member> optionalMember = memberRepository.findBySocialId(oAuth2UserInfo.getSocialId());

        if (optionalMember.isPresent()) {
            return handleExistingMember(optionalMember.get(), oAuth2UserInfo);
        }

        Member newMember = registerNewGuestMember(oAuth2UserInfo);
        return new MemberSignInResult(newMember, true);
    }

    private MemberSignInResult handleExistingMember(Member member, OAuth2UserInfo oAuth2UserInfo) {
        member.updateEmail(oAuth2UserInfo.getEmail());
        boolean isNewMember = (member.getRole() == ROLE_GUEST);

        return new MemberSignInResult(member, isNewMember);
    }

    private TokenResponse createTokens(Member member) {
        TokenResponse tokenResponse = jwtProvider.createTokens(member.getId(), member.getRole());
        member.updateRefreshToken(tokenResponse.refreshToken());

        return tokenResponse;
    }

    private Member registerNewGuestMember(OAuth2UserInfo oAuth2UserInfo) {
        Member guest = Member.createGuest(oAuth2UserInfo.getEmail(), oAuth2UserInfo.getName(), oAuth2UserInfo.getSocialId());
        memberRepository.save(guest);

        return guest;
    }

    private record MemberSignInResult(Member member, boolean isNewMember) {

    }

}