package org.kwakmunsu.dingdongpang.domain.shopimage.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shopimage.entity.ShopImage;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ShopImageRepository {

    private final ShopImageJpaRepository shopImageJpaRepository;
    private final ShopImageBulkRepository shopImageBulkRepository;

    public void save(ShopImage shopImage) {
        shopImageJpaRepository.save(shopImage);
    }

    public void saveAll(List<ShopImage> shopImages) {
        shopImageJpaRepository.saveAll(shopImages);
    }
    public void saveAll(List<String> shopImages, Shop shop) {
        shopImageBulkRepository.saveShopImagesBulk(shopImages, shop.getId());
    }

    public List<String> findByShopId(Long shopId) {
        return shopImageJpaRepository.findByShopId(shopId);
    }

    public void deleteByShopId(Long shopId) {
        shopImageJpaRepository.deleteByShopId(shopId);
    }

}