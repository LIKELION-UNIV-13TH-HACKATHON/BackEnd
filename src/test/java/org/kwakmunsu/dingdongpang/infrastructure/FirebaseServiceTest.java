package org.kwakmunsu.dingdongpang.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.infrastructure.firebase.entity.FcmToken;
import org.kwakmunsu.dingdongpang.infrastructure.firebase.repository.FcmTokenRepository;
import org.kwakmunsu.dingdongpang.infrastructure.firebase.service.FirebaseService;
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

}
