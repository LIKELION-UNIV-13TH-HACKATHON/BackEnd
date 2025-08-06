package org.kwakmunsu.dingdongpang.domain.shop.service;

import static org.kwakmunsu.dingdongpang.global.util.TimeConverter.stringToTime;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.OperationTimeServiceRequest;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.ShopRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopOperationTime;
import org.kwakmunsu.dingdongpang.domain.shop.repository.ShopOperationTimeRepository;
import org.kwakmunsu.dingdongpang.domain.shop.repository.ShopRepository;
import org.kwakmunsu.dingdongpang.domain.shopimage.entity.ShopImage;
import org.kwakmunsu.dingdongpang.domain.shopimage.repository.ShopImageRepository;
import org.kwakmunsu.dingdongpang.infrastructure.geocoding.GeocodeResponse;
import org.kwakmunsu.dingdongpang.infrastructure.s3.S3Provider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ShopCommandService {

    private final S3Provider s3Provider;
    private final ShopRepository shopRepository;
    private final ShopImageRepository shopImageRepository;
    private final ShopOperationTimeRepository shopOperationTimeRepository;

    @Transactional
    public void register(ShopRegisterServiceRequest request, GeocodeResponse geocodeResponse, Long merchantId) {
        String mainImage = s3Provider.uploadImage(request.mainImage());
        Shop shop = Shop.create(request.toDomainRequest(merchantId, geocodeResponse, mainImage));
        shopRepository.save(shop);

        // 이미지 저장.
        List<String> uploadedImages = s3Provider.uploadImages(request.imageFiles());
        saveShopImages(uploadedImages, shop);

        // 운영 시간 따로 저장
        List<OperationTimeServiceRequest> operationTimeRequests = request.operationTimeRequests();
        saveShopOperationTimes(operationTimeRequests, shop);
    }

    private void saveShopImages(List<String> uploadedImages, Shop shop) {
        List<ShopImage> images = uploadedImages.stream()
                .map(image -> ShopImage.create(shop.getId(), image))
                .toList();
        shopImageRepository.saveAll(images);
    }

    private void saveShopOperationTimes(List<OperationTimeServiceRequest> operationTimeRequests, Shop shop) {
        List<ShopOperationTime> operationTimes = operationTimeRequests.stream()
                .map(ot -> ShopOperationTime.create(
                        shop.getId(),
                        ot.dayOfWeek(),
                        stringToTime(ot.openTime()),
                        stringToTime(ot.closeTime()),
                        ot.isClosed())
                ).toList();
        shopOperationTimeRepository.saveAll(operationTimes);
    }

}