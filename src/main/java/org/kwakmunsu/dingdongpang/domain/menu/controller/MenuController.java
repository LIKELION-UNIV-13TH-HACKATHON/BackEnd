package org.kwakmunsu.dingdongpang.domain.menu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.menu.controller.dto.MenuRegisterRequest;
import org.kwakmunsu.dingdongpang.domain.menu.controller.dto.MenuUpdateRequest;
import org.kwakmunsu.dingdongpang.domain.menu.service.MenuCommandService;
import org.kwakmunsu.dingdongpang.domain.menu.service.MenuQueryService;
import org.kwakmunsu.dingdongpang.domain.menu.service.dto.MenuListResponse;
import org.kwakmunsu.dingdongpang.domain.menu.service.dto.MenuResponse;
import org.kwakmunsu.dingdongpang.global.annotation.AuthMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/shops")
@RequiredArgsConstructor
@RestController
public class MenuController extends MenuDocsController {

    private final MenuCommandService menuCommandService;
    private final MenuQueryService menuQueryService;

    @Override
    @PostMapping("/menus")
    public ResponseEntity<Void> register(
            @Valid @RequestPart MenuRegisterRequest request,
            @RequestPart(required = false) MultipartFile image,
            @AuthMember Long memberId
    ) {
        menuCommandService.register(request.toServiceRequest(image, memberId));

        return ResponseEntity.ok().build();
    }

    // 상인 전용 메뉴 목록 조회 API
    @Override
    @GetMapping("/menus")
    public ResponseEntity<MenuListResponse> getMenusByMerchant(@AuthMember Long memberId) {
        MenuListResponse response = menuQueryService.getMenusByMerchant(memberId);

        return ResponseEntity.ok(response);
    }

    // 고객 전용 메뉴 목록 조회 API
    @Override
    @GetMapping("/{shopId}/menus")
    public ResponseEntity<MenuListResponse> getMenusByCustomer(@PathVariable Long shopId) {
        MenuListResponse response = menuQueryService.getMenusByCustomer(shopId);

        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/menus/{menuId}")
    public ResponseEntity<MenuResponse> getMenu(@PathVariable Long menuId) {
        MenuResponse response = menuQueryService.getMenu(menuId);

        return ResponseEntity.ok(response);
    }

    @Override
    @PatchMapping("/menus/{menuId}")
    public ResponseEntity<Void> update(
            @Valid @RequestPart MenuUpdateRequest request,
            @RequestPart(required = false) MultipartFile image,
            @PathVariable Long menuId,
            @AuthMember Long memberId
    ) {
        menuCommandService.update(request.toServiceRequest(image, menuId, memberId));

        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping("/menus/{menuId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long menuId,
            @AuthMember Long memberId
    ) {
        menuCommandService.delete(menuId, memberId);

        return ResponseEntity.noContent().build();
    }

}