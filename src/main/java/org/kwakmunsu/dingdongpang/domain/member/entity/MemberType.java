package org.kwakmunsu.dingdongpang.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.BaseEntity;

@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"member_id", "status"})
        }
)
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MemberType extends BaseEntity {

    @Column(nullable = false)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    public static MemberType createCustomer(Long memberId) {
        return MemberType.builder()
                .memberId(memberId)
                .status(MemberStatus.CUSTOMER)
                .build();
    }

    public static MemberType createMerchant(Long memberId) {
        return MemberType.builder()
                .memberId(memberId)
                .status(MemberStatus.MERCHANT)
                .build();
    }

}