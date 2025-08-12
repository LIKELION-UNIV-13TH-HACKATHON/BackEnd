package org.kwakmunsu.dingdongpang.domain.shop.repository.shop;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.dto.ShopListResponse;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.dto.ShopNearbySearchDomainRequest;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.dto.ShopReadDomainRequest;
import org.kwakmunsu.dingdongpang.global.exception.NotFoundException;
import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ShopRepository {

    private final ShopJpaRepository shopJpaRepository;
    private final ShopQueryDslRepository shopQueryDslRepository;

    public void save(Shop shop) {
        shopJpaRepository.save(shop);
    }

    public boolean existsByBusinessNumber(String businessNumber) {
        return shopJpaRepository.existsByBusinessNumber(businessNumber);
    }

    public Shop findByMerchantId(Long memberId) {
        return shopJpaRepository.findByMerchantId(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_SHOP));
    }

    public Shop findById(Long id) {
        return shopJpaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.NOT_FOUND_SHOP));
    }

    public ShopListResponse getShopList(ShopReadDomainRequest request) {
        return shopQueryDslRepository.getShopList(request);
    }

    public List<Shop> findNearbyShops(ShopNearbySearchDomainRequest request) {
        return shopJpaRepository.findShopsWithinRadius(request.longitude(), request.latitude(), request.radiusMeters());
    }

}