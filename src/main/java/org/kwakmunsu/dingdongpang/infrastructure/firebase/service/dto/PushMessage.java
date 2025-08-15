package org.kwakmunsu.dingdongpang.infrastructure.firebase.service.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record PushMessage(
        String fcmToken,
        Long notificationId,
        Long shopId,
        String shopName,
        String shopMainImage,
        String message,
        List<String> images,
        String url
) {

}