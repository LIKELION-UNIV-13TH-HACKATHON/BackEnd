package org.kwakmunsu.dingdongpang.domain.menu.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "메뉴 목록 조회 DTO")
public record MenuListResponse(List<MenuResponse> responses) {

}