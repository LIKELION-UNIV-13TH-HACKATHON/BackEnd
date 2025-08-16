package org.kwakmunsu.dingdongpang.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.infrastructure.firebase.entity.FcmToken;
import org.kwakmunsu.dingdongpang.infrastructure.firebase.repository.FcmTokenRepository;
import org.kwakmunsu.dingdongpang.infrastructure.firebase.service.FirebaseService;
import org.kwakmunsu.dingdongpang.infrastructure.firebase.service.dto.PushMessage;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
record FirebaseServiceTest(
        FirebaseService firebaseService,
        FcmTokenRepository fcmTokenRepository
) {

    @DisplayName("Fcm 저장")
    @Test
    void save() {
        var memberId = 1L;
        var fcmToken = "test-fcm-token";
        firebaseService.save(memberId, fcmToken);
        Optional<FcmToken> existingToken = fcmTokenRepository.findByMemberIdAndToken(memberId, fcmToken);

        assertThat(existingToken.isPresent()).isTrue();
        assertThat(existingToken.get())
                .extracting(FcmToken::getToken, FcmToken:: getMemberId)
                .containsExactly(fcmToken, memberId);
    }
    
    @DisplayName("fcm이 존재할 경우 최근 사용일자만 Update")
    @Test
    void updateLastUsed() {
        var memberId = 1L;
        var fcmToken = "test-fcm-token";
        firebaseService.save(memberId, fcmToken);
        Optional<FcmToken> updatedBeforeFcm = fcmTokenRepository.findByMemberIdAndToken(memberId, fcmToken);
        var updatedBeforeAt = updatedBeforeFcm.get().getLastUsedAt();

        firebaseService.save(memberId, fcmToken);
        Optional<FcmToken> existingToken = fcmTokenRepository.findByMemberIdAndToken(memberId, fcmToken);

        var token = existingToken.get();
        assertThat(token.getLastUsedAt()).isAfter(updatedBeforeAt);
    }
    
    @DisplayName("fcm 전송 테스트")
    @Test
    void sendMessage() throws InterruptedException {
        var pushMessage = PushMessage.builder()
                .fcmToken("5tWJZKLEJxDHq_cZM_mhtj")
                .message("test-message")
                .shopId(1L)
                .shopName("shop-name")
                .shopMainImage("https:dkadd")
                .url("/shops/1")
                .notificationId(2L)
                .images(List.of("1234", "1234"))
                .build();
        var pushMessages = List.of(pushMessage);
        // 비동기 완료 대기용
        CountDownLatch latch = new CountDownLatch(1);

        // when
        firebaseService.sendMessage(pushMessages);

        // then
        // 최대 5초 대기
        boolean completed = latch.await(5, TimeUnit.SECONDS);
        assertThat(completed).isTrue();
    }


}