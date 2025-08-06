package org.kwakmunsu.dingdongpang.domain.shopimage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.BaseEntity;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ShopImage extends BaseEntity {

    @Column(nullable = false)
    private Long shopId;

    @Column(nullable = false)
    private String image;

    public static ShopImage create(Long shopId, String image) {
        return new ShopImage(shopId, image);
    }

}