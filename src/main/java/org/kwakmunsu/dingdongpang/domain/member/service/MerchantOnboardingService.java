package org.kwakmunsu.dingdongpang.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.member.entity.Member;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.MerchantRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.ShopRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.kwakmunsu.dingdongpang.domain.shop.service.ShopCommandService;
import org.kwakmunsu.dingdongpang.global.exception.DuplicationException;
import org.kwakmunsu.dingdongpang.global.exception.NotFoundException;
import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;
import org.kwakmunsu.dingdongpang.infrastructure.geocoding.KakaoGeocodingProvider;
import org.kwakmunsu.dingdongpang.infrastructure.openapi.BusinessRegisterProvider;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// TODO: 이 서비스를 어디에 둘 것인지 고민... 파사드 느낌인데
/**
 * 상인 등록 시 회원 등록 + 매장 등록이 함꼐 이루어지는 서비스
 **/
@RequiredArgsConstructor
@Service
public class MerchantOnboardingService {

    private final MemberCommandService memberCommandService;
    private final ShopCommandService shopCommandService;
    private final ShopRepository shopRepository;
    private final BusinessRegisterProvider businessRegisterProvider;
    private final KakaoGeocodingProvider kakaoGeocodingProvider;

    @Transactional
    public void register(MerchantRegisterServiceRequest request) {
        ShopRegisterServiceRequest shopRegisterRequest = request.shopRegisterServiceRequest();

        checkDuplicateRegisterShop(request);
        validateBusinessNumber(request);

        Member merchant = memberCommandService.registerMerchant(request.nickname(), request.memberId());

        Point point = kakaoGeocodingProvider.transferToGeocode(shopRegisterRequest.address());

        shopCommandService.register(shopRegisterRequest, point, merchant.getId());
    }

    private void checkDuplicateRegisterShop(MerchantRegisterServiceRequest request) {
        if (shopRepository.existsByBusinessNumber(request.businessRegistrationNumber())) {
            throw new DuplicationException(ErrorStatus.DUPLICATE_SHOP);
        }
    }

    private void validateBusinessNumber(MerchantRegisterServiceRequest request) {
        if (businessRegisterProvider.isRegister(request.businessRegistrationNumber())) {
            return;
        }
        throw new NotFoundException(ErrorStatus.NOT_FOUND_BUSINESS_NUMBER);
    }

}