package org.kwakmunsu.dingdongpang.global.jwt.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenExpiration {

    ACCESS_TOKEN  (2 * 60 * 60 * 1000L),      // 2시간
    REFRESH_TOKEN (7 * 24 * 60 * 60 * 1000L), // 1주일
  ;

    private final long expirationTime;

}