package org.kwakmunsu.dingdongpang.domain.notification.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.kwakmunsu.dingdongpang.domain.notification.entity.Notification;
import org.kwakmunsu.dingdongpang.domain.notification.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE n.type = :type AND n.isSent = false AND n.scheduledAt <= :currentTime")
    List<Notification> findReadyToSendScheduledNotifications(
            @Param("type") NotificationType type,
            @Param("currentTime") LocalDateTime currentTime
    );

    List<Notification> findByIdInAndIsSentTrue(List<Long> notificationIds);

    List<Notification> findByShopIdAndIsSentTrue(Long shopId);


    @Query("SELECT n FROM Notification n " +
            "WHERE n.shopId = :shopId " +
            "AND DATE(n.createdAt) = CURRENT_DATE " +
            "ORDER BY n.createdAt DESC " +
            "LIMIT 1")
    Optional<Notification> findTodayLatestByShopId(@Param("shopId") Long shopId);

    @Query("SELECT COUNT(n) FROM Notification n " +
            "WHERE n.shopId = :shopId " +
            "AND n.isSent = true " +
            "AND DATE(n.createdAt) = CURRENT_DATE")
    Long getTodayNotificationSentCountByShop(@Param("shopId") Long shopId);

}