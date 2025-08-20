package org.kwakmunsu.dingdongpang.domain.auth.service.kakao;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.dingdongpang.domain.auth.service.OAuth2UserInfo;
import org.kwakmunsu.dingdongpang.global.exception.UnAuthenticationException;
import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoOauthManager {

    private final RestClient restClient;

    public OAuth2UserInfo getOAuth2UserInfo(String socialAccessToken) {
        try {
            ResponseEntity<?> response = getMemberInfoFromKakaoServer(socialAccessToken);
            Map<String, Object> attributes = (Map<String, Object>) response.getBody();

            if (attributes == null) {
                throw new UnAuthenticationException(ErrorStatus.INVALID_TOKEN);
            }

            return new KakaoOAuth2UserInfo(attributes);
        } catch (HttpClientErrorException e) {
            log.error("kakao 소셜 로그인 실패: {}", e.getResponseBodyAsString());
            throw new UnAuthenticationException(ErrorStatus.INVALID_TOKEN, e);
        }
    }

    private ResponseEntity<?> getMemberInfoFromKakaoServer(String socialAccessToken) {
        return restClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", "Bearer " + socialAccessToken)
                .retrieve()
                .toEntity(Map.class);
    }

}