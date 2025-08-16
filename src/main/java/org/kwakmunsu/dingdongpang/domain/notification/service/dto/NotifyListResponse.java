package org.kwakmunsu.dingdongpang.domain.notification.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Schema(description = "알림 목록 응답 DTO")
@Builder
public record NotifyListResponse(List<NotifyPreviewResponse> responses) {

}