package org.kwakmunsu.dingdongpang.domain.shop.service;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.member.entity.Member;
import org.kwakmunsu.dingdongpang.domain.member.service.MemberCommandService;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.request.ShopRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.request.ShopUpdateServiceRequest;
import org.kwakmunsu.dingdongpang.global.exception.DuplicationException;
import org.kwakmunsu.dingdongpang.global.exception.NotFoundException;
import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;
import org.kwakmunsu.dingdongpang.infrastructure.geocoding.KakaoGeocodingProvider;
import org.kwakmunsu.dingdongpang.infrastructure.openapi.BusinessRegisterProvider;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 상인 등록 시 회원 등록 + 매장 등록이 함꼐 이루어지는 서비스
 **/
@RequiredArgsConstructor
@Service
public class ShopOnboardingService {

    private final MemberCommandService memberCommandService;
    private final ShopCommandService shopCommandService;
    private final ShopRepository shopRepository;
    private final BusinessRegisterProvider businessRegisterProvider;
    private final KakaoGeocodingProvider kakaoGeocodingProvider;

    @Transactional
    public void register(ShopRegisterServiceRequest request) {

        checkDuplicateRegisterShop(request.businessNumber());
        validateBusinessNumber(request.businessNumber());

        String nickname = request.ownerName() + request.shopName();
        Member merchant = memberCommandService.registerMerchant(nickname, request.merchantId());

        Point point = kakaoGeocodingProvider.transferToGeocode(request.address());

        shopCommandService.register(request, point, merchant.getId());
    }

    @Transactional
    public void update(ShopUpdateServiceRequest request) {
        Shop shop = shopRepository.findByMerchantId(request.merchantId());

        if (shop.isNotEqualToBusinessNumber(request.businessNumber())) {
            validateBusinessNumber(request.businessNumber());
        }

        String nickname = request.ownerName() + request.shopName();
        Member merchant = memberCommandService.updateMerchant(nickname, request.merchantId());
        Point point = shop.getLocation();
        if (shop.isNotEqualToAddress(request.address())) {
            point = kakaoGeocodingProvider.transferToGeocode(request.address());
        }

        shopCommandService.update(request, shop, point, merchant.getId());
    }

    private void checkDuplicateRegisterShop(String businessRegistrationNumber) {
        if (shopRepository.existsByBusinessNumber(businessRegistrationNumber)) {
            throw new DuplicationException(ErrorStatus.DUPLICATE_SHOP);
        }
    }

    private void validateBusinessNumber(String businessRegistrationNumber) {
        if (businessRegisterProvider.isRegister(businessRegistrationNumber)) {
            return;
        }
        throw new NotFoundException(ErrorStatus.NOT_FOUND_BUSINESS_NUMBER);
    }

}