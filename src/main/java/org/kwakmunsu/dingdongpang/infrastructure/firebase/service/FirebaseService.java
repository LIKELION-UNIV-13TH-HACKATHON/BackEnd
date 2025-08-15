package org.kwakmunsu.dingdongpang.infrastructure.firebase.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.infrastructure.firebase.entity.FcmToken;
import org.kwakmunsu.dingdongpang.infrastructure.firebase.repository.FcmTokenRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FirebaseService {

    private final FcmTokenRepository fcmTokenRepository;

    public void save(Long memberId, String token) {
        Optional<FcmToken> existingToken = fcmTokenRepository.findByMemberIdAndToken(memberId, token);

        if (existingToken.isPresent()) {
            // 기존 토큰의 마지막 사용 시간만 업데이트
            existingToken.get().updateLastUsed();
            return;
        }

        FcmToken fcmToken = FcmToken.create(memberId, token);
        fcmTokenRepository.save(fcmToken);
    }

}