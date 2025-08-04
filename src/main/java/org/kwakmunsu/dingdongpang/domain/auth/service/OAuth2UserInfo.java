package org.kwakmunsu.dingdongpang.domain.auth.service;

public interface OAuth2UserInfo {

    String getSocialId();  //제공자에서 발급해주는 아이디(번호)
    String getEmail();     //이메일
    String getName();      //사용자 실명 (설정한 이름)

}