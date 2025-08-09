package org.kwakmunsu.dingdongpang.domain.message.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageType {

    KIND   ("정중하고 배려 있는 스타일"),        // 친절
    HUMOR  ("개그맨 같이 유머있고 재밌는 스타일"), // 유머
    NEAT   ("불필요한 수식 없이 정돈된 스타일"),  // 깔끔
    CONCISE("핵심만 빠르게 전달하는 스타일");    // 간결

    private final String description;

}