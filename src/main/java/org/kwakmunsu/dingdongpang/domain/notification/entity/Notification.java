package org.kwakmunsu.dingdongpang.domain.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.BaseEntity;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Notification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    // 알림 타입 추가
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private String message;

    private LocalDateTime scheduledAt;  // 예약 발송시만 사용

    @Column(nullable = false)
    private Boolean isSent;

    public static Notification createImmediate(Shop shop, String message) {
        return Notification.builder()
                .shop(shop)
                .message(message)
                .type(NotificationType.IMMEDIATE)
                .isSent(true)
                .build();
    }

    public static Notification createScheduled(Shop shop, String message, LocalDateTime scheduledAt) {
        return Notification.builder()
                .shop(shop)
                .message(message)
                .type(NotificationType.SCHEDULED)
                .scheduledAt(scheduledAt)
                .isSent(false)
                .build();
    }

    public void markAsSent() {
        this.isSent = true;
    }

}