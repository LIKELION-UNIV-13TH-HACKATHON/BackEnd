package org.kwakmunsu.dingdongpang.domain.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ShopType {

    AGRI_FISH_LIVESTOCK("농수축산물"), // 농수축산물
    FOOD("식료품"),                // 식료품
    DAILY_SUPPLIES("생활잡화"),      // 생활잡화
    FASHION("의류/패션"),             // 의류/패션
    HEALTH_BEAUTY("건강/미용"),       // 건강/미용
    ETC("기타"),                 // 기타
    ;

    private final String description;

}