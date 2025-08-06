package org.kwakmunsu.dingdongpang.domain.member.controller.dto;

import java.util.List;
import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.MerchantRegisterServiceRequest;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record MerchantRegisterRequest(
        String nickname,
        String businessRegistrationNumber, // 사업자 등록 번호
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