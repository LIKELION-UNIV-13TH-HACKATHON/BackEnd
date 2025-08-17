package org.kwakmunsu.dingdongpang.domain.auth.service.dto;

import lombok.Builder;

@Builder
public record SignInServiceRequest(String socialAccessToken) {

}