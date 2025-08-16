package org.kwakmunsu.dingdongpang.domain.notification.repository;

import java.util.List;
import org.kwakmunsu.dingdongpang.domain.notification.entity.NotificationReceiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationReceiverJpaRepository extends JpaRepository<NotificationReceiver, Long> {

    @Query("select nr.notificationId from NotificationReceiver nr where nr.receiverId= :receiverId")
    List<Long> findByReceiverId(@Param("receiverId") Long receiverId);

}