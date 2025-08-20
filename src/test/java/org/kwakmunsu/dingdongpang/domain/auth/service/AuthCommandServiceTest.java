package org.kwakmunsu.dingdongpang.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kwakmunsu.dingdongpang.domain.auth.service.dto.SignInServiceRequest;
import org.kwakmunsu.dingdongpang.domain.auth.service.kakao.KakaoOauthManager;
import org.kwakmunsu.dingdongpang.domain.member.entity.Member;
import org.kwakmunsu.dingdongpang.domain.member.repository.MemberRepository;
import org.kwakmunsu.dingdongpang.global.exception.NotFoundException;
import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;
import org.kwakmunsu.dingdongpang.global.jwt.JwtProvider;
import org.kwakmunsu.dingdongpang.global.jwt.dto.TokenResponse;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

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

        var tokenResponse = new TokenResponse("accessToken", "refreshToken");
        given(jwtProvider.createTokens(any(), any())).willReturn(tokenResponse);

        var signInServiceRequest = new SignInServiceRequest("social-access-token");
        var result = authCommandService.signIn(signInServiceRequest);
        assertThat(result.isNewMember()).isTrue();
        assertThat(result.response())
                .extracting(TokenResponse::accessToken, TokenResponse::refreshToken)
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

        var tokenResponse = new TokenResponse("accessToken", "refreshToken");
        given(jwtProvider.createTokens(any(), any())).willReturn(tokenResponse);

        var signInServiceRequest = new SignInServiceRequest("social-access-token");
        var result = authCommandService.signIn(signInServiceRequest);

        assertThat(optionalMember.get().getRefreshToken()).isEqualTo(tokenResponse.refreshToken());
        assertThat(result.isNewMember()).isFalse();
        assertThat(result.response())
                .extracting(TokenResponse::accessToken, TokenResponse::refreshToken)
                .containsExactly(tokenResponse.accessToken(), tokenResponse.refreshToken());
    }

    @DisplayName("토큰을 재발급한다.")
    @Test
    void reissue() {
        var member = Member.createMember("email", "nickname", "1234");
        var tokenResponse = new TokenResponse("new-accessToken", "new-refreshToken");
        given(memberRepository.findByRefreshToken(any())).willReturn(member);
        given(jwtProvider.createTokens(any(), any())).willReturn(tokenResponse);

        TokenResponse result = authCommandService.reissue("refreshToken");

        assertThat(result)
                .extracting(TokenResponse::accessToken, TokenResponse::refreshToken)
                .containsExactly(tokenResponse.accessToken(), tokenResponse.refreshToken());

        assertThat(member.getRefreshToken()).isEqualTo(tokenResponse.refreshToken());
    }

    @DisplayName("유효하지 않은 RT 요청으로 토큰 재발급에 실패한다.")
    @Test
    void failReissue() {
        given(memberRepository.findByRefreshToken(any())).willThrow(new NotFoundException(ErrorStatus.NOT_FOUND_TOKEN));

        assertThatThrownBy(() -> authCommandService.reissue("refreshToken"))
                .isInstanceOf(NotFoundException.class);
    }

    // TODO: FCM 토큰도 확인하기
    @DisplayName("로그아웃을 한다.")
    @Test
    void logout() {
        var member = Member.createMember("email", "nickname", "1234");
        ReflectionTestUtils.setField(member, "id", 1L);

        given(memberRepository.findById(any())).willReturn(member);

        authCommandService.signOut(member.getId());

        assertThat(member.getRefreshToken()).isNull();
    }

}