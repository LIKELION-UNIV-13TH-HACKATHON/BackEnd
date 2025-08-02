package org.kwakmunsu.dingdongpang.domain.notification.service;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationCommandService {

    private final NotificationRepository notificationRepository;

}