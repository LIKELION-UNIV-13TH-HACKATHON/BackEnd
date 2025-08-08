package org.kwakmunsu.dingdongpang.domain.menu.service;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.menu.entity.Menu;
import org.kwakmunsu.dingdongpang.domain.menu.repository.MenuRepository;
import org.kwakmunsu.dingdongpang.domain.menu.service.dto.MenuRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.menu.service.dto.MenuUpdateServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.repository.ShopRepository;
import org.kwakmunsu.dingdongpang.global.exception.DuplicationException;
import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;
import org.kwakmunsu.dingdongpang.infrastructure.s3.S3Provider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class MenuCommandService {

    private final MenuRepository menuRepository;
    private final ShopRepository shopRepository;
    private final S3Provider s3Provider;

    public void register(MenuRegisterServiceRequest request) {
        Shop shop = shopRepository.findByMemberId(request.memberId());

        if (menuRepository.existsByShopIdAndName(shop.getId(), request.name())) {
            throw new DuplicationException(ErrorStatus.DUPLICATE_MENU);
        }
        // 이미지가 없을 경우 Null 반환
        String uploadImage = uploadImage(request.image());

        Menu menu = Menu.create(shop, request.name(), request.price(), request.description(), uploadImage);
        menuRepository.save(menu);
    }

    @Transactional
    public void update(MenuUpdateServiceRequest request) {
        Menu menu = menuRepository.findByIdAndShopMemberId(request.id(), request.memberId());

        deleteImage(menu.getImage());
        String uploadImage = uploadImage(request.image());

        menu.updateMenu(request.toDomainRequest(uploadImage));
    }

    @Transactional
    public void delete(Long menuId, Long memberId) {
        Menu menu = menuRepository.findByIdAndShopMemberId(menuId, memberId);

        deleteImage(menu.getImage());
        menuRepository.deleteById(menuId);
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