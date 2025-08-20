package org.kwakmunsu.dingdongpang.domain.auth.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.ControllerTestSupport;
import org.kwakmunsu.dingdongpang.domain.auth.controller.dto.ReissueTokenRequest;
import org.kwakmunsu.dingdongpang.domain.auth.controller.dto.SignInRequest;
import org.kwakmunsu.dingdongpang.domain.auth.service.dto.SignInResponse;
import org.kwakmunsu.dingdongpang.global.TestMember;
import org.kwakmunsu.dingdongpang.global.exception.UnAuthenticationException;
import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;
import org.kwakmunsu.dingdongpang.global.jwt.dto.TokenResponse;
import org.springframework.http.MediaType;

class AuthControllerTest extends ControllerTestSupport {

    @DisplayName("카카오 서버에서 발급 받은 accessToken과 함께 로그인 요청 시 JWT를 발급한다.")
    @Test
    void signIn() throws JsonProcessingException {
        var signInRequest = new SignInRequest("kakao-accessToken");
        var jsonToString = objectMapper.writeValueAsString(signInRequest);
        var tokenResponse = new TokenResponse("accessToken", "refreshToken");
        var signInResponse = new SignInResponse(true /*isNewMember*/, 1L, tokenResponse);

        given(authCommandService.signIn(any())).willReturn(signInResponse);

        var result = mvcTester.post().uri("/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .apply(print())
                .bodyJson()
                .hasPathSatisfying("$.isNewMember", v -> v.assertThat().isEqualTo(true))
                .hasPathSatisfying("$.memberId", v -> v.assertThat().isEqualTo(signInResponse.memberId().intValue()))
                .hasPathSatisfying("$.response.accessToken", v -> v.assertThat().isEqualTo(tokenResponse.accessToken()))
                .hasPathSatisfying("$.response.refreshToken", v -> v.assertThat().isEqualTo(tokenResponse.refreshToken()));
    }

    @DisplayName("로그인 시 요청 body에 카카오 AccessToken 값이 없다면 에러를 던진다.")
    @Test
    void failSignIn() throws JsonProcessingException {
        var signInRequest = new SignInRequest("");
        var jsonToString = objectMapper.writeValueAsString(signInRequest);

        var tokenResponse = new TokenResponse("accessToken", "refreshToken");
        var signInResponse = new SignInResponse(true /*isNewMember*/, 1L, tokenResponse);
        given(authCommandService.signIn(any())).willReturn(signInResponse);
        var result = mvcTester.post().uri("/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString)
                .exchange();

        assertThat(result).hasStatus(400);
    }

    @DisplayName("로그인 시 유효하지 않은 카카오 AccessToken 값을 보낸다면 에러를 던진다.")
    @Test
    void failSignInToUnAuthentication() throws JsonProcessingException {
        var signInRequest = new SignInRequest("invalid-token");
        var jsonToString = objectMapper.writeValueAsString(signInRequest);

        given(authCommandService.signIn(any())).willThrow(new UnAuthenticationException(ErrorStatus.INVALID_TOKEN));

        var result = mvcTester.post().uri("/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString)
                .exchange();

        assertThat(result).hasStatus(401);
    }

    @TestMember
    @DisplayName("로그아웃을 한다.")
    @Test
    void singOut() {
        assertThat(mvcTester.post().uri("/auth/sign-out"))
                .hasStatusOk()
                .apply(print())
                .bodyJson();
    }

    @DisplayName("토큰을 재발급한다.")
    @Test
    void reissue() throws JsonProcessingException {
        var reissueTokenRequest = new ReissueTokenRequest("refreshToken");
        var jsonToString = objectMapper.writeValueAsString(reissueTokenRequest);
        var tokenResponse = new TokenResponse("accessToken", "refreshToken");
        given(authCommandService.reissue(any())).willReturn(tokenResponse);

        var result = mvcTester.post().uri("/auth/reissue")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .apply(print())
                .bodyJson()
                .hasPathSatisfying("$.accessToken", v -> v.assertThat().isEqualTo(tokenResponse.accessToken()))
                .hasPathSatisfying("$.refreshToken", v -> v.assertThat().isEqualTo(tokenResponse.refreshToken()));
    }

}