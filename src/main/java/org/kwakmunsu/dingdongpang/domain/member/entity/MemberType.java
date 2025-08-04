package org.kwakmunsu.dingdongpang.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class MemberType extends BaseEntity {

    @Column(nullable = false)
    Long memberId;

    @Enumerated(EnumType.STRING)
    MemberStatus status;

    public static MemberType create(Long memberId, MemberStatus status) {
        return MemberType.builder()
                .memberId(memberId)
                .status(status)
                .build();
    }

}