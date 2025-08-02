package org.kwakmunsu.dingdongpang.domain.notification.repository;

import org.kwakmunsu.dingdongpang.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {

}