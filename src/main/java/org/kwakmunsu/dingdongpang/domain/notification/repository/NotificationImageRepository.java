package org.kwakmunsu.dingdongpang.domain.notification.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.notification.entity.NotificationImage;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class NotificationImageRepository {

    private final NotificationImageJpaRepository notificationImageJpaRepository;

    public Long save(NotificationImage notificationImage) {
        return notificationImageJpaRepository.save(notificationImage).getId();
    }

    public List<String> findByNotificationId(Long notificationId) {
        return notificationImageJpaRepository.findByNotificationId(notificationId);
    }

}