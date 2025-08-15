package org.kwakmunsu.dingdongpang.domain.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
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
public class Notification extends BaseEntity {

    @Column(nullable = false)
    private Long shopId;

    // 알림 타입 추가
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private String message;

    private LocalDateTime scheduledAt;  // 예약 발송시만 사용

    @Column(nullable = false)
    private Boolean isSent;

    public static Notification createImmediate(Long shopId, String message) {
        return Notification.builder()
                .shopId(shopId)
                .message(message)
                .type(NotificationType.IMMEDIATE)
                .isSent(true)
                .build();
    }

    public static Notification createScheduled(Long shopId, String message, LocalDateTime scheduledAt) {
        return Notification.builder()
                .shopId(shopId)
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