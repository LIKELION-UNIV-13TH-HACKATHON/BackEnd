package org.kwakmunsu.dingdongpang.domain.auth.service;

import org.kwakmunsu.dingdongpang.global.oauth.OAuth2UserInfo;

public interface OAuth2Provider {

    OAuth2UserInfo getOAuth2UserInfo(String socialAccessToken);

}