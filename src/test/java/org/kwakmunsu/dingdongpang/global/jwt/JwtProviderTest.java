package org.kwakmunsu.dingdongpang.global.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.member.entity.Role;
import org.kwakmunsu.dingdongpang.global.jwt.dto.TokenResponse;
import org.springframework.security.core.Authentication;

class JwtProviderTest {

    JwtProvider jwtProvider;
    Long memberId;
    Role role;

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider("1233asdasdqweqw3123123asdffsdfasdfasggdd");
        this.memberId = 1L;
        this.role = ROLE_MEMBER;
    }

    @DisplayName("AT와 RT를 생성한다.")
    @Test
    void createTokens() {
        TokenResponse response = jwtProvider.createTokens(memberId, role);

        assertThat(response.accessToken()).isNotNull();
        assertThat(response.refreshToken()).isNotNull();
    }

    @DisplayName("Authentication 객체를 가져온다")
    @Test
    void getAuthentication() {
        TokenResponse response = jwtProvider.createTokens(memberId, role);

        Authentication authentication = jwtProvider.getAuthentication(response.accessToken());

        assertThat(authentication.getPrincipal()).isEqualTo(String.valueOf(memberId));
    }

    @DisplayName("Authentication 객체를 가져오는데 실패한다")
    @Test
    void failGetAuthentication() {
        TokenResponse response = jwtProvider.createTokens(memberId, role);

        assertThatThrownBy(() -> jwtProvider.getAuthentication(response.accessToken() + "invalid-signature-token"))
                .isInstanceOf(io.jsonwebtoken.security.SignatureException.class);

        assertThatThrownBy(() -> jwtProvider.getAuthentication("invalid-malformed-token"))
                .isInstanceOf(MalformedJwtException.class);

        assertThatThrownBy(() -> jwtProvider.getAuthentication(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("토큰이 유효한지 검증한다.")
    @Test
    void isValidateToken() {
        TokenResponse response = jwtProvider.createTokens(memberId, role);

        assertThat(jwtProvider.isValidateToken(response.accessToken())).isTrue();
        assertThat(jwtProvider.isValidateToken("invalid-token")).isFalse();
    }

}