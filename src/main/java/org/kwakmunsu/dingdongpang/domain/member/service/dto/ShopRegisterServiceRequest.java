package org.kwakmunsu.dingdongpang.domain.member.service.dto;

import java.util.List;
import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.member.repository.dto.ShopRegisterDomainRequest;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.kwakmunsu.dingdongpang.infrastructure.geocoding.GeocodeResponse;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record ShopRegisterServiceRequest(
        String shopName,
        ShopType shopType,
        String shopPhoneNumber,
        String address,
        String businessNumber,
        String ownerName,
        MultipartFile mainImage,
        List<MultipartFile> imageFiles,
        List<OperationTimeServiceRequest> operationTimeRequests
) {

    public ShopRegisterDomainRequest toDomainRequest(Long memberId, GeocodeResponse response, String mainImage) {
        return ShopRegisterDomainRequest.builder()
                .memberId(memberId)
                .shopName(shopName)
                .businessNumber(businessNumber)
                .ownerName(ownerName)
                .shopType(shopType)
                .address(address)
                .shopTellNumber(shopPhoneNumber)
                .latitude(response.latitude())
                .longitude(response.longitude())
                .mainImage(mainImage)
                .build();
    }

}