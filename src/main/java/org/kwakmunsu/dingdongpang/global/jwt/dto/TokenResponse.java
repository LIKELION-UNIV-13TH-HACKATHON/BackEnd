package org.kwakmunsu.dingdongpang.global.jwt.dto;

import lombok.Builder;

@Builder
public record TokenResponse(String accessToken, String refreshToken) {

}