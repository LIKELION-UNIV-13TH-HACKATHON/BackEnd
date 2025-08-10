package org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Schema(description = "매장 목록 조회 응답 DTO")
@Builder
public record SubscribeShopListResponse(
        @Schema(description = "매장 목록 조회 시 보여지는 정보")
        List<SubscribeShopPreviewResponse> responses,

        @Schema(description = "다음 커서 ID, Null 이면 더 이상 데이터가 없음")
        Long nextCursorId,

        @Schema(description = "다음 페이지가 있는지 여부")
        boolean hasNext
) {

}