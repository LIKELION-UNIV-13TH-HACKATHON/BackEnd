package org.kwakmunsu.dingdongpang.infrastructure.firebase;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.SubscribeShopRepository;
import org.kwakmunsu.dingdongpang.infrastructure.firebase.service.FirebaseService;
import org.kwakmunsu.dingdongpang.infrastructure.firebase.service.dto.FcmSendEvent;
import org.kwakmunsu.dingdongpang.infrastructure.firebase.service.dto.PushMessage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class FcmEventHandler {

    private final FirebaseService firebaseService;
    private final SubscribeShopRepository subscribeShopRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleFcmSend(FcmSendEvent event) {
        List<String> fcmTokens = subscribeShopRepository.findFcmTokensByShopId(event.shop().getId());
        if (fcmTokens.isEmpty()) {
            return;
        }
        // 500개씩 나누어 처리 - fcm은 최대 500개까지 처리 가능
        final int BATCH_SIZE = 500;

        for (int i = 0; i < fcmTokens.size(); i += BATCH_SIZE) {
            int endIndex = Math.min(i + BATCH_SIZE, fcmTokens.size());
            List<String> tokenBatch = fcmTokens.subList(i, endIndex);

            List<PushMessage> pushMessages = tokenBatch.stream()
                    .map(token -> createPushMessage(
                            event.message(),
                            token,
                            event.notificationId(),
                            event.shop(),
                            event.notificationImages())
                    )
                    .toList();
            firebaseService.sendMessage(pushMessages);

            log.info("FCM 배치 전송: {}/{} 완료", endIndex, fcmTokens.size());
        }
    }

    private PushMessage createPushMessage(String message, String fcmToken, Long notificationId, Shop shop,
            List<String> uploadImages) {
        return PushMessage.builder()
                .fcmToken(fcmToken)
                .notificationId(notificationId)
                .shopId(shop.getId())
                .shopName(shop.getShopName())
                .shopMainImage(shop.getMainImage())
                .message(message)
                .images(uploadImages)
                .url("/shops/" + shop.getId())
                .build();
    }

}
