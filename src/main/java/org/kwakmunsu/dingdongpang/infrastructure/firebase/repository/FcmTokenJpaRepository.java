package org.kwakmunsu.dingdongpang.infrastructure.firebase.repository;

import java.util.Optional;
import org.kwakmunsu.dingdongpang.infrastructure.firebase.entity.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmTokenJpaRepository extends JpaRepository<FcmToken, Long> {

    Optional<FcmToken> findByMemberIdAndToken(Long memberId, String token);

}