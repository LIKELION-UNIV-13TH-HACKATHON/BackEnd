package org.kwakmunsu.dingdongpang.infrastructure.firebase.service.dto;

import java.util.List;
import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;

@Builder
public record FcmSendEvent(
        String message,
        Long notificationId,
        Shop shop,
        List<String> notificationImages
) {

    public static FcmSendEvent of(String message, Long notificationId, Shop shop, List<String> notificationImages) {
        return FcmSendEvent.builder()
                .message(message)
                .notificationId(notificationId)
                .shop(shop)
                .notificationImages(notificationImages)
                .build();
    }

}