package org.kwakmunsu.dingdongpang.domain.menu.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.kwakmunsu.dingdongpang.domain.menu.service.dto.MenuRegisterServiceRequest;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "메뉴 등록 요청 DTO")
public record MenuRegisterRequest(
        @Schema(description = "메뉴 이름", example = "핵불닭돈가스")
        @NotBlank(message = "메뉴 이름은 필수 값입니다.")
        String name,

        @Schema(description = "메뉴 가격", example = "20000")
        @Positive(message = "메뉴 가격은 0원 이상이여야 합니다.")
        int price,

        @Schema(description = "메뉴 설명", example = "먹으면 응급실 갑니다 ㅜㅜㅜ")
        @NotBlank(message = "메뉴 설명은 필수 값입니다.")
        String description
) {

    public MenuRegisterServiceRequest toServiceRequest(MultipartFile image, Long memberId) {
        return MenuRegisterServiceRequest.builder()
                .name(name)
                .price(price)
                .description(description)
                .image(image)
                .merchantId(memberId)
                .build();
    }

}