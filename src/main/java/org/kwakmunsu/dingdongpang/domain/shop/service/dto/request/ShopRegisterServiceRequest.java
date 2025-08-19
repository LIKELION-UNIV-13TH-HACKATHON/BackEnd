package org.kwakmunsu.dingdongpang.domain.shop.service.dto.request;

import java.util.List;
import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.kwakmunsu.dingdongpang.domain.shop.entity.dto.ShopRegisterDomainRequest;
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
        boolean isTermAgreed,
        Long merchantId,
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