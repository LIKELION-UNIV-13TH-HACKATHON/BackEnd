package org.kwakmunsu.dingdongpang.domain.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.BaseEntity;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class NotificationImage extends BaseEntity {

    @Column(nullable = false)
    private Long notificationId;

    @Column(nullable = false)
    private String image;

    public static NotificationImage create(Long notificationId, String image) {
        return new NotificationImage(notificationId, image);
    }

}