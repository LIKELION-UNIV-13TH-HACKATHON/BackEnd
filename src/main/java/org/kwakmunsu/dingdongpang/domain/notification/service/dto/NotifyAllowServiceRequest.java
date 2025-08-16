package org.kwakmunsu.dingdongpang.domain.notification.service.dto;

import lombok.Builder;

@Builder
public record NotifyAllowServiceRequest(boolean isAllow, Long memberId) {

}