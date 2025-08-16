package org.kwakmunsu.dingdongpang.infrastructure.firebase.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.BaseEntity;

@Builder
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

    private boolean isEnabled;

    public static FcmToken create(Long memberId, String token) {
        return FcmToken.builder()
                .memberId(memberId)
                .token(token)
                .lastUsedAt(LocalDateTime.now())
                .isEnabled(true)
                .build();
    }

    public void updateLastUsed() {
        this.lastUsedAt = LocalDateTime.now();
    }

    public void updateEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

}