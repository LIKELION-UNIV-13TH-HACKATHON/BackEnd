package org.kwakmunsu.dingdongpang.domain.shop.entity;

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
import org.kwakmunsu.dingdongpang.domain.member.repository.dto.ShopRegisterDomainRequest;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Shop extends BaseEntity {

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String shopName;

    @Column(nullable = false)
    private String businessNumber;

    @Column(nullable = false)
    private String ownerName;

    @Enumerated(EnumType.STRING)
    private ShopType shopType;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String shopTellNumber;

    @Column(nullable = false)
    private String latitude;  // 위도

    @Column(nullable = false)
    private String longitude; // 경도

    private String mainImage;

    private boolean isDeleted;

    public static Shop create(ShopRegisterDomainRequest request) {
        return Shop.builder()
                .memberId(request.memberId())
                .shopName(request.shopName())
                .businessNumber(request.businessNumber())
                .ownerName(request.ownerName())
                .shopType(request.shopType())
                .address(request.address())
                .shopTellNumber(request.shopTellNumber())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .mainImage(request.mainImage())
                .isDeleted(false)
                .build();
    }

    public void delete() {
        this.isDeleted = true;
    }

}