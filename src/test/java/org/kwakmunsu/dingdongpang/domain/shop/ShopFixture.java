package org.kwakmunsu.dingdongpang.domain.shop;

import java.util.List;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.request.ShopRegisterServiceRequest;

public final class ShopFixture {

    public static ShopRegisterServiceRequest getShopRegisterServiceRequest() {
        return getShopRegisterServiceRequest(1L);
    }

    public static ShopRegisterServiceRequest getShopRegisterServiceRequest(Long merchantId) {
        return ShopRegisterServiceRequest.builder()
                .businessNumber("8962801461")    // 사업자 등록 번호
                .ownerName("김계란")              // 대표자명
                .shopName("역전할머니맥주")         // 매장명
                .shopType(ShopType.FASHION)      // 매장 타입 (enum)
                .shopPhoneNumber("010-8742-1234")// 매장 전화번호
                .address("경기도 광주시 경충대로1461번길 12-4 코오롱 세이브 프라자 202호")
                .mainImage(null)
                .merchantId(merchantId)
                .imageFiles(List.of())
                .operationTimeRequests(List.of())
                .build();// 매장 주소
    }

    public static ShopRegisterServiceRequest getShopRegisterServiceRequest(Long merchantId, String businessNumber) {
        return ShopRegisterServiceRequest.builder()
                .businessNumber(businessNumber)    // 사업자 등록 번호
                .ownerName("김계란")              // 대표자명
                .shopName("역전할머니맥주")         // 매장명
                .shopType(ShopType.FASHION)      // 매장 타입 (enum)
                .shopPhoneNumber("010-8742-1234")// 매장 전화번호
                .address("경기도 광주시 경충대로1461번길 12-4 코오롱 세이브 프라자 202호")
                .mainImage(null)
                .merchantId(merchantId)
                .imageFiles(List.of())
                .operationTimeRequests(List.of())
                .build();// 매장 주소
    }
}