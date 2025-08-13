package org.kwakmunsu.dingdongpang.domain.member.service.dto;

import java.util.List;
import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.shop.entity.dto.ShopRegisterDomainRequest;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.locationtech.jts.geom.Point;
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

    public ShopRegisterDomainRequest toDomainRequest(Long merchantId, Point location, String mainImage) {
        return ShopRegisterDomainRequest.builder()
                .merchantId(merchantId)
                .shopName(shopName)
                .businessNumber(businessNumber)
                .ownerName(ownerName)
                .shopType(shopType)
                .address(address)
                .shopTellNumber(shopPhoneNumber)
                .location(location)
                .mainImage(mainImage)
                .build();
    }

}