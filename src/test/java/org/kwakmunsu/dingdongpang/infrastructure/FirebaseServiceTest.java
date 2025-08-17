package org.kwakmunsu.dingdongpang.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.kwakmunsu.dingdongpang.domain.auth.service.dto.FcmTokenServiceRequest;
import org.kwakmunsu.dingdongpang.global.exception.BadRequestException;
import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;
import org.kwakmunsu.dingdongpang.infrastructure.firebase.entity.FcmToken;
import org.kwakmunsu.dingdongpang.infrastructure.firebase.repository.FcmTokenRepository;
import org.kwakmunsu.dingdongpang.infrastructure.firebase.service.FirebaseService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FirebaseServiceTest {

    @Mock
    private FcmTokenRepository fcmTokenRepository;

    @Mock
    private FirebaseMessaging firebaseMessaging;

    @InjectMocks
    private FirebaseService firebaseService;

    @DisplayName("FcmToken을 저장한다.")
    @Test
    void updateFcmToken() throws FirebaseMessagingException {
        var request = new FcmTokenServiceRequest("test-fcm-token", 1L);
        var fcmToken = FcmToken.create(request.memberId(), request.fcmToken());
        given(fcmTokenRepository.findByMemberIdAndToken(any(), any())).willReturn(Optional.of(fcmToken));
        given(firebaseMessaging.send(any(), any(Boolean.class))).willReturn("mock_message_id");

        firebaseService.updateFcmToken(request);
        Optional<FcmToken> existingToken = fcmTokenRepository.findByMemberIdAndToken(request.memberId(), request.fcmToken());

        assertThat(existingToken.get())
                .extracting(FcmToken::getToken, FcmToken::getMemberId)
                .containsExactly(request.fcmToken(), request.memberId());
    }

    @DisplayName("유효하지 않은 fcmToken일 경우 예외를 반환한다.")
    @Test
    void failUpdateFcmToken() throws FirebaseMessagingException {
        var request = new FcmTokenServiceRequest("test-fcm-token", 1L);
        given(firebaseMessaging.send(any(), any(Boolean.class)))
                .willThrow(new BadRequestException(ErrorStatus.INVALID_FCM_TOKEN));

        assertThatThrownBy(() -> firebaseService.updateFcmToken(request))
                .isInstanceOf(BadRequestException.class);
    }

    @DisplayName("fcm이 존재할 경우 최근 사용일자만 Update")
    @Test
    void updateLastUsed() {
        var request = new FcmTokenServiceRequest("test-fcm-token", 1L);
        var fcmToken = FcmToken.create(request.memberId(), request.fcmToken());
        given(fcmTokenRepository.findByMemberIdAndToken(any(), any())).willReturn(Optional.of(fcmToken));

        firebaseService.updateFcmToken(request);

        verify(fcmTokenRepository, never()).save(any(FcmToken.class));
    }

}