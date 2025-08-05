package org.kwakmunsu.dingdongpang.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kwakmunsu.dingdongpang.domain.auth.service.kakao.KakaoOauthManager;
import org.kwakmunsu.dingdongpang.domain.member.entity.Member;
import org.kwakmunsu.dingdongpang.domain.member.repository.MemberRepository;
import org.kwakmunsu.dingdongpang.global.jwt.JwtProvider;
import org.kwakmunsu.dingdongpang.global.jwt.dto.TokenResponse;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthCommandServiceTest {

    @Mock
    KakaoOauthManager kakaoOauthManager;

    @Mock
    MemberRepository memberRepository;

    @Mock
    JwtProvider jwtProvider;

    @InjectMocks
    AuthCommandService authCommandService;

    @DisplayName("가입 하지 않은 사용자가 로그인 한 경우 소셜 로그인 정보로 guest 권한의 멤버를 생성한다.")
    @Test
    void signInWithGuest() {
        var mockUserInfo = mock(OAuth2UserInfo.class);
        when(mockUserInfo.getSocialId()).thenReturn("123456789");
        when(mockUserInfo.getEmail()).thenReturn("test@email.com");
        when(mockUserInfo.getName()).thenReturn("홍길동");
        given(kakaoOauthManager.getOAuth2UserInfo(anyString())).willReturn(mockUserInfo);

        given(memberRepository.findBySocialIdAndRole(anyString(), any())).willReturn(Optional.empty());

        var tokenResponse = new TokenResponse("accessToken","refreshToken");
        given(jwtProvider.createTokens(any(),any())).willReturn(tokenResponse);

        var result = authCommandService.signIn("accessToken");
        assertThat(result.isNewMember()).isTrue();
        assertThat(result.response())
                .extracting(    TokenResponse::accessToken, TokenResponse::refreshToken)
                .containsExactly(tokenResponse.accessToken(), tokenResponse.refreshToken());
    }

    @DisplayName("기존 이용자가 로그인 한 경우 jwt만 발급한다.")
    @Test
    void signInWithMember() {
        var mockUserInfo = mock(OAuth2UserInfo.class);
        when(mockUserInfo.getSocialId()).thenReturn("123456789");
        when(mockUserInfo.getEmail()).thenReturn("test@email.com");
        given(kakaoOauthManager.getOAuth2UserInfo(anyString())).willReturn(mockUserInfo);

        Optional<Member> optionalMember = Optional.ofNullable(Member.createMember("email", "nickname", "1234"));
        given(memberRepository.findBySocialIdAndRole(anyString(), any())).willReturn(optionalMember);

        var tokenResponse = new TokenResponse("accessToken","refreshToken");
        given(jwtProvider.createTokens(any(),any())).willReturn(tokenResponse);

        var result = authCommandService.signIn("accessToken");

        assertThat(optionalMember.get().getRefreshToken()).isEqualTo(tokenResponse.refreshToken());
        assertThat(result.isNewMember()).isFalse();
        assertThat(result.response())
                .extracting(    TokenResponse::accessToken, TokenResponse::refreshToken)
                .containsExactly(tokenResponse.accessToken(), tokenResponse.refreshToken());
    }

}