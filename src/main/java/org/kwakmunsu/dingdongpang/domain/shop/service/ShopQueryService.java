package org.kwakmunsu.dingdongpang.domain.shop.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.dto.ShopListResponse;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shopoperation.ShopOperationTimeRepository;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shopoperation.dto.ShopOperationTimeResponse;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.ShopNearbySearchListResponse;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.ShopNearbySearchResponse;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.ShopNearbySearchServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.ShopReadServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.ShopResponse;
import org.kwakmunsu.dingdongpang.domain.shopimage.repository.ShopImageRepository;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.SubscribeShopRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShopQueryService {

    private final ShopRepository shopRepository;
    private final ShopOperationTimeRepository shopOperationTimeRepository;
    private final ShopImageRepository shopImageRepository;
    private final SubscribeShopRepository subscribeShopRepository;

    // TODO: 그냥 한방 쿼리로 할까....
    public ShopResponse getShop(Long shopId, Long memberId) {
        Shop shop = shopRepository.findById(shopId);
        boolean isSubscribe = subscribeShopRepository.existsByMemberIdAndShopId(memberId, shopId);
        List<ShopOperationTimeResponse> operationTimeResponses = shopOperationTimeRepository.findByShopId(shopId);
        List<String> shopImages = shopImageRepository.findByShopId(shopId);

        // 조회수 증가
        shop.incrementViewCount();
        shopRepository.save(shop);

        return ShopResponse.from(shop, isSubscribe, operationTimeResponses, shopImages);
    }

    public ShopListResponse getShopList(ShopReadServiceRequest request) {
        return shopRepository.getShopList(request.toDomainRequest());
    }

    public ShopNearbySearchListResponse getNearbyShops(ShopNearbySearchServiceRequest request) {
        List<Shop> nearbyShops = shopRepository.findNearbyShops(request.toDomainRequest());
        List<ShopNearbySearchResponse> searchResponseList = nearbyShops.stream()
                .map(ShopNearbySearchResponse::of)
                .toList();

        return new ShopNearbySearchListResponse(searchResponseList);
    }

}