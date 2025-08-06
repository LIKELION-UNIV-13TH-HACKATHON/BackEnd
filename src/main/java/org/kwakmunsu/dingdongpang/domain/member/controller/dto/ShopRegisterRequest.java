package org.kwakmunsu.dingdongpang.domain.member.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.ShopRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record ShopRegisterRequest(
        @NotBlank(message = "매장명은 필수값입니다.")
        String shopName,

        @NotBlank(message = "매장 타입은 필수값입니다.")
        ShopType shopType,

        @NotBlank(message = "매장 번호는 필수값입니다.")
        String shopPhoneNumber,

        @NotBlank(message = "매장 주소는 필수값입니다.")
        String address,

        @NotEmpty(message = "운영시간 리스트는 필수값입니다.")
        @Size(min = 7, max = 7, message = "운영시간은 7개여야 합니다.")
        List<OperationTimeRequest> operationTimes
) {

    public ShopRegisterServiceRequest toServiceRequest(String businessNumber, String ownerName, MultipartFile mainImage, List<MultipartFile> imageFiles) {
        return ShopRegisterServiceRequest.builder()
                .shopName(shopName)
                .shopType(shopType)
                .shopPhoneNumber(shopPhoneNumber)
                .address(address)
                .businessNumber(businessNumber)
                .ownerName(ownerName)
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