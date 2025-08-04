package org.kwakmunsu.dingdongpang.domain.auth.service.kakao;
import java.util.Map;
import org.kwakmunsu.dingdongpang.domain.auth.service.OAuth2UserInfo;

public class KakaoOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> kakaoProfile;
    private final String email;
    private final String socialId;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        this.socialId = attributes.get("id").toString();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        this.email = kakaoAccount.get("email").toString();
        this.kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");
    }

    @Override
    public String getSocialId() {
        return socialId;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return kakaoProfile.get("nickname").toString();
    }

}