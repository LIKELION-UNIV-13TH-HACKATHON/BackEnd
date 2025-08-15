package org.kwakmunsu.dingdongpang.domain.notification.repository;

import java.util.List;
import org.kwakmunsu.dingdongpang.domain.notification.entity.NotificationImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationImageJpaRepository extends JpaRepository<NotificationImage, Long> {

    @Query("select n.image from NotificationImage n where n.notificationId = :notificationId")
    List<String> findByNotificationId(@Param("notificationId") Long notificationId);

}