package org.kwakmunsu.dingdongpang.domain.shop.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.repository.ShopOperationTimeRepository;
import org.kwakmunsu.dingdongpang.domain.shop.repository.ShopRepository;
import org.kwakmunsu.dingdongpang.domain.shop.repository.dto.ShopOperationTimeResponse;
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

        return ShopResponse.from(shop, isSubscribe, operationTimeResponses, shopImages);
    }

}