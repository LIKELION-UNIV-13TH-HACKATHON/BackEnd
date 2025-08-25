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

    @Query("SELECT n FROM Notification n JOIN FETCH n.shop WHERE n.type = :type AND n.isSent = false AND n.scheduledAt <= :currentTime")
    List<Notification> findReadyToSendScheduledNotifications(
            @Param("type") NotificationType type,
            @Param("currentTime") LocalDateTime currentTime
    );

    @Query("SELECT n FROM Notification n JOIN FETCH n.shop WHERE n.id IN :notificationIds AND n.isSent = true")
    List<Notification> findSentNotificationsWithShopByIds(@Param("notificationIds") List<Long> notificationIds);

    @Query("SELECT n FROM Notification n JOIN FETCH  n.shop WHERE n.shop.id = :shopId AND n.isSent = true")
    List<Notification> findByShopIdAndIsSentTrue(@Param("shopId") Long shopId);


    @Query("SELECT n FROM Notification n " +
            "WHERE n.shop.id = :shopId " +
            "AND n.createdAt >= :startOfDay " +
            "AND n.createdAt < :endOfDay " +
            "ORDER BY n.createdAt DESC")
    Optional<Notification> findTodayLatestByShopId(
            @Param("shopId") Long shopId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT COUNT(n) FROM Notification n " +
            "WHERE n.shop.id = :shopId " +
            "AND n.isSent = true " +
            "AND n.createdAt >= :startOfDay " +
            "AND n.createdAt < :endOfDay")
    Long getTodayNotificationSentCountByShop(
            @Param("shopId") Long shopId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);

}