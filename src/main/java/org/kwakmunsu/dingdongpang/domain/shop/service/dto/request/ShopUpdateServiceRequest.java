package org.kwakmunsu.dingdongpang.domain.shop.service.dto.request;

import java.util.List;
import lombok.Builder;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.kwakmunsu.dingdongpang.domain.shop.entity.dto.ShopUpdateDomainRequest;
import org.locationtech.jts.geom.Point;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record ShopUpdateServiceRequest(
        String shopName,
        ShopType shopType,
        String shopPhoneNumber,
        String address,
        String businessNumber,
        String ownerName,
        Long merchantId,
        MultipartFile mainImage,
        List<MultipartFile> imageFiles,
        List<OperationTimeServiceRequest> operationTimeRequests
) {

    public ShopUpdateDomainRequest toDomainRequest(Long merchantId, Point location, String mainImage) {
        return ShopUpdateDomainRequest.builder()
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