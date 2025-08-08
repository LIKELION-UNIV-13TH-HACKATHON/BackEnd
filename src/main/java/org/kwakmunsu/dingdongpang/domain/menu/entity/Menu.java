package org.kwakmunsu.dingdongpang.domain.menu.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.BaseEntity;
import org.kwakmunsu.dingdongpang.domain.menu.entity.dto.MenuUpdateDomainRequest;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Menu extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Column(nullable = false)
    private String name;

    private int price;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    private String image;

    public static Menu create(Shop shop, String name, int price, String description, String image) {
        if (price < 0)  throw new IllegalArgumentException("가격은 0원 이상이어야 합니다.");

        return Menu.builder()
                .shop(shop)
                .name(name)
                .price(price)
                .description(description)
                .image(image)
                .build();
    }

    public void updateMenu(MenuUpdateDomainRequest request) {
        this.name = request.name();
        this.price = request.price();
        this.description = request.description();
        this.image = request.image();
    }

}