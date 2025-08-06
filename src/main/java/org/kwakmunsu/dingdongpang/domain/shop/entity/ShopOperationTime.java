package org.kwakmunsu.dingdongpang.domain.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.DayOfWeek;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.BaseEntity;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ShopOperationTime extends BaseEntity {

    @Column(nullable = false)
    private Long shopId;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @Column(nullable = false)
    private boolean isClosed;

    public static ShopOperationTime create(
            Long shopId,
            DayOfWeek dayOfWeek,
            LocalTime openTime,
            LocalTime closeTime,
            boolean isClosed
    ) {
        return ShopOperationTime.builder()
                .shopId(shopId)
                .dayOfWeek(dayOfWeek)
                .openTime(openTime)
                .closeTime(closeTime)
                .isClosed(isClosed)
                .build();
    }

}