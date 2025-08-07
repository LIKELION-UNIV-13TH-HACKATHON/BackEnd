package org.kwakmunsu.dingdongpang.domain.menu.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.menu.entity.Menu;

@Schema(description = "메뉴 정보 응답 DTO")
@Builder
public record MenuResponse(
        @Schema(description = "메뉴 id", example = "1")
        Long id,

        @Schema(description = "메뉴 이름", example = "문수표 김치볶음밥")
        String name,

        @Schema(description = "메뉴 가격", example = "2000000")
        int price,

        @Schema(description = "메뉴 설명", example = "둘이 먹다가 혼자 먹어도 돼?")
        String description,

        @Schema(description = "메뉴 이미지", example = "http:s12312313")
        String image
) {

    public static MenuResponse of(Menu menu) {
        return MenuResponse.builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(menu.getPrice())
                .description(menu.getDescription())
                .image(menu.getImage())
                .build();
    }
}