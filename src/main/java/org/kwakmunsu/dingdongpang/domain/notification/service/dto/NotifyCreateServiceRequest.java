package org.kwakmunsu.dingdongpang.domain.notification.service.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.notification.entity.SendType;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record NotifyCreateServiceRequest(
        String message,
        SendType sendType,
        LocalDateTime scheduledAt, // 예약 발송 시간
        List<MultipartFile> images,
        Long shopId,
        Long memberId
) {

}