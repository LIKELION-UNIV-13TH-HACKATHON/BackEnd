package org.kwakmunsu.dingdongpang.domain.member.controller.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.MerchantRegisterServiceRequest;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record MerchantRegisterRequest(

        @NotBlank(message = "닉네임은 필수값입니다.")
        String nickname,

        @NotBlank(message = "사업자 등록 번호는 필수값입니다.")
        String businessRegistrationNumber, // 사업자 등록 번호

        @NotBlank(message = "대표자명은 필수값입니다.")
        String ownerName,

        ShopRegisterRequest shopRegisterRequest
) {

    public MerchantRegisterServiceRequest toServiceRequest(
            MultipartFile mainImage,
            List<MultipartFile> imageFiles,
            Long memberId
    ) {
        return MerchantRegisterServiceRequest.builder()
                .nickname(nickname)
                .businessRegistrationNumber(businessRegistrationNumber)
                .memberId(memberId)
                .shopRegisterServiceRequest(shopRegisterRequest.toServiceRequest(businessRegistrationNumber, ownerName, mainImage, imageFiles))
                .build();
    }

}