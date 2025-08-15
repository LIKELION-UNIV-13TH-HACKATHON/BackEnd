package org.kwakmunsu.dingdongpang.domain.notification.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import org.kwakmunsu.dingdongpang.domain.notification.entity.SendType;
import org.kwakmunsu.dingdongpang.domain.notification.service.dto.NotifyCreateServiceRequest;
import org.springframework.web.multipart.MultipartFile;

public record NotifyCreateRequest(
        @NotBlank(message = "알림 메세지는 필수값입니다.")
        String message,

        @NotNull(message = "발송 타입은 필수값입니다.")
        SendType sendType,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime scheduledAt // 예약 발송 시간
) {

    @AssertTrue(message = "예약 발송 시 scheduledAt은 필수입니다")
    public boolean isValidScheduledAt() {
        if (sendType == SendType.SCHEDULED) {
            return scheduledAt != null && scheduledAt.isAfter(LocalDateTime.now());
        }
        return true;
    }

    public NotifyCreateServiceRequest toServiceRequest(List<MultipartFile> images, Long shopId, Long memberId) {
        return NotifyCreateServiceRequest.builder()
                .message(message)
                .sendType(sendType)
                .scheduledAt(scheduledAt)
                .images(images)
                .shopId(shopId)
                .memberId(memberId)
                .build();
    }

}