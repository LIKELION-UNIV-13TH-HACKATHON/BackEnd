package org.kwakmunsu.dingdongpang.infrastructure.firebase.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.BaseEntity;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FcmToken extends BaseEntity {

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String token;

    private LocalDateTime lastUsedAt;

    public static FcmToken create(Long memberId, String token) {
        return new FcmToken(memberId, token, LocalDateTime.now());
    }

    public void updateLastUsed() {
        this.lastUsedAt = LocalDateTime.now();
    }

}