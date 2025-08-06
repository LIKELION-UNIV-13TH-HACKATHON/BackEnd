package org.kwakmunsu.dingdongpang.domain.member.controller.dto;

import java.util.List;
import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.ShopRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record ShopRegisterRequest(
        String shopName,
        ShopType shopType,
        String shopPhoneNumber,
        String address,
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