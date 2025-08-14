package org.kwakmunsu.dingdongpang.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.member.entity.Member;
import org.kwakmunsu.dingdongpang.domain.member.repository.MemberRepository;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.CheckNicknameResponse;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.CustomerProfileResponse;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.MerchantProfileResponse;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberQueryService {

    private final MemberRepository memberRepository;
    private final ShopRepository shopRepository;

    public CheckNicknameResponse isExistsNickname(String nickname) {
        boolean exists = memberRepository.existsByNickname(nickname);

        return new CheckNicknameResponse(exists);
    }

    public MerchantProfileResponse getMerchantProfile(Long merchantId) {
        Shop shop = shopRepository.findByMerchantId(merchantId);

        return new MerchantProfileResponse(merchantId, shop.getShopName(), shop.getAddress(), shop.getMainImage());
    }

    public CustomerProfileResponse getCustomerProfile(Long customerId) {
        Member customer = memberRepository.findById(customerId);

        return new CustomerProfileResponse(customer.getId(), customer.getNickname(), customer.getProfileUrl());
    }

}