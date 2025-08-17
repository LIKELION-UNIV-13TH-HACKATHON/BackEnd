package org.kwakmunsu.dingdongpang.domain.shop.service;

import static org.kwakmunsu.dingdongpang.global.util.TimeConverter.stringToTime;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopOperationTime;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shopoperation.ShopOperationTimeRepository;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.request.OperationTimeServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.request.ShopRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.request.ShopUpdateServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shopimage.entity.ShopImage;
import org.kwakmunsu.dingdongpang.domain.shopimage.repository.ShopImageRepository;
import org.kwakmunsu.dingdongpang.infrastructure.s3.S3Provider;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ShopCommandService {

    private final S3Provider s3Provider;
    private final ShopRepository shopRepository;
    private final ShopImageRepository shopImageRepository;
    private final ShopOperationTimeRepository shopOperationTimeRepository;

    @Transactional
    public void register(ShopRegisterServiceRequest request, Point point, Long merchantId) {
        // mainImage가 없을 시 Null 반환
        String mainImage = uploadImage(request.mainImage());
        Shop shop = Shop.create(request.toDomainRequest(merchantId, point, mainImage));
        shopRepository.save(shop);

        if (!request.imageFiles().isEmpty()) {
            List<String> uploadedImages = s3Provider.uploadImages(request.imageFiles());
            saveShopImages(uploadedImages, shop);
        }

        List<OperationTimeServiceRequest> operationTimeRequests = request.operationTimeRequests();
        saveShopOperationTimes(operationTimeRequests, shop);
    }

    @Transactional
    public void update(ShopUpdateServiceRequest request, Shop shop, Point point, Long merchantId) {
        String mainImage = uploadImage(request.mainImage());
        deleteImage(shop.getMainImage());
        shop.updateInfo(request.toDomainRequest(merchantId, point, mainImage));

        List<String> shopImages = shopImageRepository.findByShopId(shop.getId());
        s3Provider.deleteImages(shopImages);
        shopImageRepository.deleteByShopId(shop.getId());

        if (!request.imageFiles().isEmpty()) {
            List<String> uploadedImages = s3Provider.uploadImages(request.imageFiles());
            saveShopImages(uploadedImages, shop);
        }

        List<OperationTimeServiceRequest> operationTimeRequests = request.operationTimeRequests();
        shopOperationTimeRepository.deleteByShopId(shop.getId());
        shopOperationTimeRepository.findByShopId(shop.getId());
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

    private String uploadImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return null;
        }
        return s3Provider.uploadImage(image);
    }

    private void deleteImage(String image) {
        if (image == null || image.isEmpty()) {
            return;
        }
        s3Provider.deleteImage(image);
    }

}