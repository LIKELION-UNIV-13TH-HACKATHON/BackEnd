package org.kwakmunsu.dingdongpang.domain.shop.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopOperationTime;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ShopOperationTimeRepository {

    private final ShopOperationTimeJpaRepository shopOperationTimeJpaRepository;

    public void saveAll(List<ShopOperationTime> operationTimes) {
        shopOperationTimeJpaRepository.saveAll(operationTimes);
    }

}