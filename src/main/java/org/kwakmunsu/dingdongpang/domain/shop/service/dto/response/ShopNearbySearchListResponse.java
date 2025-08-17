package org.kwakmunsu.dingdongpang.domain.shop.service.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "주변 매장 목록 응답 DTO")
public record ShopNearbySearchListResponse(List<ShopNearbySearchResponse> responses) {

}