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
import org.kwakmunsu.dingdongpang.domain.shop.entity.dto.ShopRegisterDomainRequest;
import org.locationtech.jts.geom.Point;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Shop extends BaseEntity {

    @Column(nullable = false)
    private Long merchantId;

    @Column(nullable = false)
    private String shopName;

    @Column(nullable = false, unique = true)
    private String businessNumber;

    @Column(nullable = false)
    private String ownerName;

    @Enumerated(EnumType.STRING)
    private ShopType shopType;

    @Column(nullable = false, unique = true)
    private String address;

    @Column(nullable = false, unique = true)
    private String shopTellNumber;

    @Column(columnDefinition = "POINT SRID 4326", nullable = false)
    private Point location; // 경도(X), 위도(Y)

    private String mainImage;

    private boolean isDeleted;

    @Column(nullable = false)
    private Long viewCount;

    public static Shop create(ShopRegisterDomainRequest request) {
        return Shop.builder()
                .merchantId(request.merchantId())
                .shopName(request.shopName())
                .businessNumber(request.businessNumber())
                .ownerName(request.ownerName())
                .shopType(request.shopType())
                .address(request.address())
                .shopTellNumber(request.shopTellNumber())
                .location(request.location())
                .mainImage(request.mainImage())
                .isDeleted(false)
                .viewCount(0L)
                .build();
    }

    public void incrementViewCount() {
        this.viewCount++;
    }

    public void delete() {
        this.isDeleted = true;
    }

    public boolean isMerchant(Long merchantId) {
        return this.merchantId.equals(merchantId);
    }

    public boolean isNotEqualToBusinessNumber(String businessNumber) {
        return !this.businessNumber.equals(businessNumber);
    }

    public boolean isNotEqualToAddress(String address) {
        return !this.address.equals(address);
    }

    public void updateInfo(ShopRegisterDomainRequest request) {
        this.shopName =       request.shopName();
        this.businessNumber = request.businessNumber();
        this.ownerName =      request.ownerName();
        this.shopType =       request.shopType();
        this.address =        request.address();
        this.shopTellNumber = request.shopTellNumber();
        this.location =       request.location();
        this.mainImage =      request.mainImage();

    }
}