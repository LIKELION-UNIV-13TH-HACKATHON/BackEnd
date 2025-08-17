package org.kwakmunsu.dingdongpang.infrastructure.firebase.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.SendResponse;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kwakmunsu.dingdongpang.domain.auth.service.dto.FcmTokenServiceRequest;
import org.kwakmunsu.dingdongpang.global.exception.BadRequestException;
import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;
import org.kwakmunsu.dingdongpang.infrastructure.firebase.entity.FcmToken;
import org.kwakmunsu.dingdongpang.infrastructure.firebase.repository.FcmTokenRepository;
import org.kwakmunsu.dingdongpang.infrastructure.firebase.service.dto.PushMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class FirebaseService {

    private final FcmTokenRepository fcmTokenRepository;
    private final FirebaseMessaging firebaseMessaging;

    @Transactional
    public void updateFcmToken(FcmTokenServiceRequest request) {
        Long memberId = request.memberId();
        String token = request.fcmToken();
        // dry_run = true로 실제 메시지를 보내지 않고 토큰만 검증
        validateFcmToken(token);

        Optional<FcmToken> existingToken = fcmTokenRepository.findByMemberIdAndToken(memberId, token);
        if (existingToken.isPresent()) {
            // 기존 토큰의 마지막 사용 시간만 업데이트
            existingToken.get().updateLastUsed();
            return;
        }

        FcmToken fcmToken = FcmToken.create(memberId, token);
        fcmTokenRepository.save(fcmToken);
    }

    public void sendMessage(List<PushMessage> pushMessages) {
        List<Message> messages = pushMessages.stream()
                .map(this::createMessage).toList();
        ApiFuture<BatchResponse> future = FirebaseMessaging.getInstance().sendEachAsync(messages);

        // 콜백으로 결과 처리
        future.addListener(() -> {
            try {
                BatchResponse response = future.get();
                log.info("FCM 전송 완료: 성공={}, 실패={}", response.getSuccessCount(), response.getFailureCount());

                handleFailedTokens(response, messages);

            } catch (ExecutionException | InterruptedException e) {
                log.error("FCM 전송 실패", e);
            }
        }, Runnable::run);
    }

    private void handleFailedTokens(BatchResponse response, List<Message> messages) {
        if (response.getFailureCount() > 0) {
            List<SendResponse> responses = response.getResponses();
            for (int i = 0; i < responses.size(); i++) {
                if (!responses.get(i).isSuccessful()) {
                    log.error(responses.get(i).getException().getMessage());
                    log.error(responses.get(i).getException().getErrorCode().toString());
                    log.warn("FCM 전송 실패 토큰: {}", messages.get(i).toString());
                }
            }
        }
    }

    private Message createMessage(PushMessage pushMessage) {
        Notification.Builder notificationBuilder = Notification.builder()
                .setTitle(pushMessage.shopName() + " 새로운 공지")
                .setBody(pushMessage.message());

        // Message 빌더
        Message.Builder messageBuilder = Message.builder()
                .setToken(pushMessage.fcmToken())
                .setNotification(notificationBuilder.build())
                .putData("notificationId", pushMessage.notificationId().toString())
                .putData("shopId", pushMessage.shopId().toString())
                .putData("shopName", pushMessage.shopName())
                .putData("message", pushMessage.message())
                .putData("url", pushMessage.url());

        // 메인 이미지 URL 추가
        if (pushMessage.shopMainImage() != null && !pushMessage.shopMainImage().isEmpty()) {
            messageBuilder.putData("shopMainImage", pushMessage.shopMainImage());
        }

        // 추가 이미지들을 JSON으로 변환해서 추가
        if (pushMessage.images() != null && !pushMessage.images().isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String imagesJson = objectMapper.writeValueAsString(pushMessage.images());
                messageBuilder.putData("images", imagesJson);
            } catch (Exception e) {
                log.error("이미지 목록 JSON 변환 실패", e);
            }
        }

        return messageBuilder.build();
    }

    private void validateFcmToken(String fcmToken) {
        try {
            Message message = Message.builder()
                    .setToken(fcmToken)
                    .build();

            firebaseMessaging.send(message, true /*dry_run*/);
        } catch (FirebaseMessagingException e) {
            // InvalidRegistration, NotRegistered 등의 에러 발생 시 무효한 토큰
            throw new BadRequestException(ErrorStatus.INVALID_FCM_TOKEN);
        }
    }

}