package org.kwakmunsu.dingdongpang.domain.notification.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class NotificationRepository {

    private final NotificationJpaRepository inquiryJpaRepository;

}