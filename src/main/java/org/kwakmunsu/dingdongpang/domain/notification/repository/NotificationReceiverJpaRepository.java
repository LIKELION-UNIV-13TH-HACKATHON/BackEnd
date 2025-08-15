package org.kwakmunsu.dingdongpang.domain.notification.repository;

import org.kwakmunsu.dingdongpang.domain.notification.entity.NotificationReceiver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationReceiverJpaRepository extends JpaRepository<NotificationReceiver, Long> {

}