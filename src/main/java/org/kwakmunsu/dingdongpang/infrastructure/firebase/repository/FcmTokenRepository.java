package org.kwakmunsu.dingdongpang.infrastructure.firebase.repository;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.infrastructure.firebase.entity.FcmToken;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class FcmTokenRepository {

    private final FcmTokenJpaRepository fcmTokenJpaRepository;

    public void save(FcmToken fcmToken) {
        fcmTokenJpaRepository.save(fcmToken);
    }

    public Optional<FcmToken> findByMemberIdAndToken(Long memberId, String token) {
        return fcmTokenJpaRepository.findByMemberIdAndToken(memberId, token);
    }

}