package org.kwakmunsu.dingdongpang.domain.auth.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.ControllerTestSupport;
import org.kwakmunsu.dingdongpang.domain.auth.service.dto.SignInResponse;
import org.kwakmunsu.dingdongpang.global.exception.UnAuthenticationException;
import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;
import org.kwakmunsu.dingdongpang.global.jwt.dto.TokenResponse;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

@WebMvcTest(AuthController.class)
class AuthControllerTest extends ControllerTestSupport {

    @DisplayName("카카오 서버에서 발급 받은 accessToken과 함께 로그인 요청 시 JWT를 발급한다.")
    @Test
    void signIn() {
        var tokenResponse = new TokenResponse("accessToken", "refreshToken");
        var signInResponse = new SignInResponse(true /*isNewMember*/, tokenResponse);
        given(authCommandService.signIn(anyString())).willReturn(signInResponse);
        var result = mvcTester.post().uri("/auth/sign-in")
                .header("Authorization", "Bearer accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .apply(print())
                .bodyJson()
                .hasPathSatisfying("$.isNewMember", v -> v.assertThat().isEqualTo(true))
                .hasPathSatisfying("$.response.accessToken", v -> v.assertThat().isEqualTo(tokenResponse.accessToken()))
                .hasPathSatisfying("$.response.refreshToken", v -> v.assertThat().isEqualTo(tokenResponse.refreshToken()));
    }

    @DisplayName("로그인 시 요청 헤더에 카카오 AccessToken 값이 없다면 에러를 던진다.")
    @Test
    void failSignIn() {
        var tokenResponse = new TokenResponse("accessToken", "refreshToken");
        var signInResponse = new SignInResponse(true /*isNewMember*/, tokenResponse);
        given(authCommandService.signIn(anyString())).willReturn(signInResponse);
        var result = mvcTester.post().uri("/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .exchange();

        assertThat(result).hasStatus(400);
    }

    @DisplayName("로그인 시 유효하지 않은 카카오 AccessToken 값을 보낸다면 에러를 던진다.")
    @Test
    void failSignInToUnAuthentication() {
        given(authCommandService.signIn(anyString())).willThrow(
                new UnAuthenticationException(ErrorStatus.INVALID_TOKEN));
        var result = mvcTester.post().uri("/auth/sign-in")
                .header("Authorization", "Bearer accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .exchange();

        assertThat(result).hasStatus(401);
    }

}