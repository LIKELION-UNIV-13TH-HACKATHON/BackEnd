package org.kwakmunsu.dingdongpang.domain.auth.service;

import static org.kwakmunsu.dingdongpang.domain.member.entity.Role.ROLE_MEMBER;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.auth.service.dto.SignInResponse;
import org.kwakmunsu.dingdongpang.domain.auth.service.kakao.KakaoOauthManager;
import org.kwakmunsu.dingdongpang.domain.member.entity.Member;
import org.kwakmunsu.dingdongpang.domain.member.repository.MemberRepository;
import org.kwakmunsu.dingdongpang.global.jwt.JwtProvider;
import org.kwakmunsu.dingdongpang.global.jwt.dto.TokenResponse;
import org.kwakmunsu.dingdongpang.infrastructure.firebase.service.FirebaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthCommandService {

    private final KakaoOauthManager kakaoOauthManager;
    private final FirebaseService firebaseService;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public SignInResponse signIn(String socialAccessToken, String fcmToken) {
        OAuth2UserInfo oAuth2UserInfo = kakaoOauthManager.getOAuth2UserInfo(socialAccessToken);
        Optional<Member> optionalMember = memberRepository.findBySocialIdAndRole(oAuth2UserInfo.getSocialId(), ROLE_MEMBER);

        Member member;
        boolean isNewMember;

        // 기존 사용자가 로그인 할 경우 정보 업데이트 후 jwt 발급, 첫 사용자 일 경우 Guest 권한의 회원 생성 후 jwt 발급.
        if (optionalMember.isPresent()) {
            member = optionalMember.get();
            member.updateEmail(oAuth2UserInfo.getEmail());
            isNewMember = false;
        } else {
            member = registerNewGuestMember(oAuth2UserInfo);
            isNewMember = true;
        }

        firebaseService.save(member.getId(), fcmToken);

        return createSignInResponse(member, isNewMember);
    }

    @Transactional
    public TokenResponse reissue(String refreshToken) {
        Member member = memberRepository.findByRefreshToken(refreshToken);

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

    private SignInResponse createSignInResponse(Member member, boolean isNewMember) {
        TokenResponse tokenResponse = jwtProvider.createTokens(member.getId(), member.getRole());
        member.updateRefreshToken(tokenResponse.refreshToken());

        return new SignInResponse(isNewMember, tokenResponse);
    }

    private Member registerNewGuestMember(OAuth2UserInfo oAuth2UserInfo) {
        Member guest = Member.createGuest(oAuth2UserInfo.getEmail(), oAuth2UserInfo.getName(), oAuth2UserInfo.getSocialId());
        memberRepository.save(guest);

        return guest;
    }

}