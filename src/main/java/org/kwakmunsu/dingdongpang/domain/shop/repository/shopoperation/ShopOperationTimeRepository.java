package org.kwakmunsu.dingdongpang.domain.shop.repository.shopoperation;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopOperationTime;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shopoperation.dto.ShopOperationTimeResponse;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ShopOperationTimeRepository {

    private final ShopOperationTimeJpaRepository shopOperationTimeJpaRepository;
    private final ShopOperationTimeBulkRepository shopOperationTimeBulkRepository;

    public void saveAll(List<ShopOperationTime> operationTimes) {
        shopOperationTimeJpaRepository.saveAll(operationTimes);
    }

    public void saveAll(List<ShopOperationTime> operationTimes, Shop shop) {
        shopOperationTimeBulkRepository.saveOperationTimesBulk(operationTimes, shop.getId());
    }

    public List<ShopOperationTimeResponse> findByShopId(Long shopId) {
        return shopOperationTimeJpaRepository.findByShopId(shopId);
    }

    public void deleteByShopId(Long shopId) {
        shopOperationTimeJpaRepository.deleteByShopId(shopId);
    }

}