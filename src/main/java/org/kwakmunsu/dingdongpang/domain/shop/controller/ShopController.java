package org.kwakmunsu.dingdongpang.domain.shop.controller;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.shop.service.ShopQueryService;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.ShopResponse;
import org.kwakmunsu.dingdongpang.global.annotation.AuthMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/shops")
@RequiredArgsConstructor
@RestController
public class ShopController extends ShopDocsController {

    private final ShopQueryService shopQueryService;

    @Override
    @GetMapping("/{shopId}")
    public ResponseEntity<ShopResponse> getShop(@PathVariable Long shopId, @AuthMember Long memberId) {
        ShopResponse response = shopQueryService.getShop(shopId, memberId);

        return ResponseEntity.ok(response);
    }

}