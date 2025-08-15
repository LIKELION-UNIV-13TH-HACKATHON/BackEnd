package org.kwakmunsu.dingdongpang.domain.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class NotificationReceiver extends BaseEntity {

    @Column(nullable = false)
    private Long notificationId;

    @Column(nullable = false)
    private Long receiverId;

    public static NotificationReceiver create(Long notificationId, Long receiverId) {
        return NotificationReceiver.builder()
                .notificationId(notificationId)
                .receiverId(receiverId)
                .build();
    }

}