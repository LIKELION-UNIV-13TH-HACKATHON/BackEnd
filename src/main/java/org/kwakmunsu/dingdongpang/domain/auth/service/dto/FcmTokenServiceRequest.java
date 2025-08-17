package org.kwakmunsu.dingdongpang.domain.auth.service.dto;

import lombok.Builder;

@Builder
public record FcmTokenServiceRequest(String fcmToken, Long memberId) {

}