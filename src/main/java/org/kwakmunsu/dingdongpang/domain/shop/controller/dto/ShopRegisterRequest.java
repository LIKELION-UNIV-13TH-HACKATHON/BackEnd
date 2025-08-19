package org.kwakmunsu.dingdongpang.domain.shop.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.request.ShopRegisterServiceRequest;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Schema(description = "매장 정보 등록 DTO")
public record ShopRegisterRequest(

        @NotBlank(message = "사업자 등록 번호는 필수값입니다.")
        @Schema(description = "사업자 등록 번호", example = "8962801461")
        String businessNumber, // 사업자 등록 번호

        @NotBlank(message = "대표자명은 필수값입니다.")
        @Schema(description = "매장 대표자 명", example = "김계란")
        String ownerName,

        @NotBlank(message = "매장명은 필수값입니다.")
        @Schema(description = "매장 이름", example = "역전할머니맥주")
        String shopName,

        @Schema(description = "업종 ", example = "FASHION")
        ShopType shopType,

        @NotBlank(message = "매장 번호는 필수값입니다.")
        @Schema(description = "매장 번호 ", example = "010-8742-1234")
        String shopPhoneNumber,

        @NotBlank(message = "매장 주소는 필수값입니다.")
        @Schema(description = "매장 주소", example = "경기도 광주시 경충대로1461번길 12-4 코오롱 세이브 프라자 202호")
        String address,

        @Schema(description = "약관 동의 여부", example = "true")
        boolean isTermAgreed,

        @NotEmpty(message = "운영시간 리스트는 필수값입니다.")
        @Size(min = 7, max = 7, message = "운영시간은 7개여야 합니다.")
        @Schema(description = "매장 운영시간 - 월~일 모두 입력하셔야합니다.")
        List<OperationTimeRequest> operationTimes
) {

    public ShopRegisterServiceRequest toServiceRequest(
            MultipartFile mainImage,
            List<MultipartFile> imageFiles,
            Long merchantId
    ) {
        return ShopRegisterServiceRequest.builder()
                .shopName(shopName)
                .shopType(shopType)
                .shopPhoneNumber(shopPhoneNumber)
                .address(address)
                .businessNumber(businessNumber)
                .ownerName(ownerName)
                .isTermAgreed(isTermAgreed)
                .merchantId(merchantId)
                .mainImage(mainImage)
                .imageFiles(imageFiles)
                .operationTimeRequests(
                        operationTimes.stream()
                                .map(OperationTimeRequest::toServiceRequest)
                                .toList()
                )
                .build();
    }
}