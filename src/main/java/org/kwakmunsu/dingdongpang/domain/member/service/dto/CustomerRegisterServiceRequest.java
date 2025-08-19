package org.kwakmunsu.dingdongpang.domain.member.service.dto;

import lombok.Builder;

@Builder
public record CustomerRegisterServiceRequest(
        String nickname,
        boolean isTermAgreed,
        Long memberId
) {

}