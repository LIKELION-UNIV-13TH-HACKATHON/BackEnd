package org.kwakmunsu.dingdongpang.domain.shop.repository.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import lombok.Builder;

@Builder
public record ShopOperationTimeResponse(
        DayOfWeek dayOfWeek,
        LocalTime openTime,
        LocalTime closeTime,
        boolean isClosed
) {

}