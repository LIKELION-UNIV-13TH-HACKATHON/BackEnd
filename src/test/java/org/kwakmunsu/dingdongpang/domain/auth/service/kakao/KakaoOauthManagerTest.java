package org.kwakmunsu.dingdongpang.domain.auth.service.kakao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kwakmunsu.dingdongpang.domain.auth.service.OAuth2UserInfo;
import org.kwakmunsu.dingdongpang.global.exception.UnAuthenticationException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class KakaoOauthManagerTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    KakaoOauthManager kakaoOauthManager;

    @DisplayName("카카오에서 발급된 accessToken 으로 카카오 서버에 사용자 소셜 정보를 요청한다.")
    @Test
    void getOAuth2UserInfo() {

        Map<String, Object> kakaoResponse = new HashMap<>();
        kakaoResponse.put("id", 123456789L);
        kakaoResponse.put("kakao_account", Map.of("email", "test@kakao.com"));

        given(restTemplate.exchange(anyString(), any(), any(), eq(Map.class)))
                .willReturn(new ResponseEntity<>(kakaoResponse, HttpStatus.OK));

        OAuth2UserInfo userInfo = kakaoOauthManager.getOAuth2UserInfo("valid_token");
        assertThat(userInfo.getSocialId()).isEqualTo("123456789");
        assertThat(userInfo.getEmail()).isEqualTo("test@kakao.com");

    }

    @DisplayName("유효하지 않은 accessToken 으로 카카오 서버에 사용자 소셜 정보를 요청 시 에러를 반환한다.")
    @Test
    void failGetOAuth2UserInfo() {
        var invalidToken = "invalid-token";

        given(restTemplate.exchange(anyString(), any(), any(), eq(Map.class)))
                .willThrow(UnAuthenticationException.class);

        assertThatThrownBy(() -> kakaoOauthManager.getOAuth2UserInfo(invalidToken))
            .isInstanceOf(UnAuthenticationException.class);
    }

}