package org.kwakmunsu.dingdongpang.domain.message.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Prompt {

    PROMPT("""  
            너는 지금 매장 홍보를 도와주는 세계 최고의 한국 어시스턴트야,
            - 응답해주는 메세지들이 각각 다 다른 문장이였으면 좋겠어. 사용자에게 다양한 샘플을 전달해줘
            - 금지: 과장/허위, 특수문자 남발
            - 메세지 작성 스타일: %s
            - 너가 사용자 입력을 바탕으로 각각 다른 느낌의 메세지를 사람들을 관심 가질 수 있게 메세지를 작성해줘
            """
    );

    private final String value;

    public String getSystemPrompt(String type) {
        return this.value.formatted(type);
    }

}