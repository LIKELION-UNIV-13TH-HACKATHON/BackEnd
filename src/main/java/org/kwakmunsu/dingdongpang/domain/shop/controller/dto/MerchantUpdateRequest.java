package org.kwakmunsu.dingdongpang.domain.shop.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.MerchantUpdateServiceRequest;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Schema(description = "상인 회원 등록 ")
public record MerchantUpdateRequest(

        @NotBlank(message = "닉네임은 필수값입니다.")
        @Schema(description = "상인 닉네임", example = "강대훈")
        String nickname,

        @NotBlank(message = "사업자 등록 번호는 필수값입니다.")
        @Schema(description = "사업자 등록 번호", example = "8962801461")
        String businessRegistrationNumber, // 사업자 등록 번호

        @NotBlank(message = "대표자명은 필수값입니다.")
        @Schema(description = "매장 대표자 명", example = "김계란")
        String ownerName,

        ShopUpdateRequest shopUpdateRequest
) {

    public MerchantUpdateServiceRequest toServiceRequest(
            MultipartFile mainImage,
            List<MultipartFile> imageFiles,
            Long memberId
    ) {
        return MerchantUpdateServiceRequest.builder()
                .nickname(nickname)
                .businessRegistrationNumber(businessRegistrationNumber)
                .memberId(memberId)
                .shopUpdateServiceRequest(
                        shopUpdateRequest.toServiceRequest(businessRegistrationNumber, ownerName, mainImage, imageFiles))
                .build();
    }

}