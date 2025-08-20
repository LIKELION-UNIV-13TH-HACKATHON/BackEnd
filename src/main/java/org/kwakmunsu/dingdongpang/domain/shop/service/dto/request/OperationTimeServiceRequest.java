package org.kwakmunsu.dingdongpang.domain.shop.service.dto.request;

import java.time.DayOfWeek;
import lombok.Builder;

@Builder
public record OperationTimeServiceRequest(
        DayOfWeek dayOfWeek,
        String openTime,
        String closeTime,
        boolean isClosed
) {

}