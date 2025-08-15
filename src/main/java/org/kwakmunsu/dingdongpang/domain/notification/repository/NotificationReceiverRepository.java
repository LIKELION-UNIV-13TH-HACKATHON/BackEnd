package org.kwakmunsu.dingdongpang.domain.notification.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.notification.entity.NotificationReceiver;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class NotificationReceiverRepository {

    private final NotificationReceiverJpaRepository notificationReceiverJpaRepository;

    public Long save(NotificationReceiver notificationReceiver) {
        return notificationReceiverJpaRepository.save(notificationReceiver).getId();
    }

    public void saveAll(List<NotificationReceiver> receivers) {
        notificationReceiverJpaRepository.saveAll(receivers);
    }

}