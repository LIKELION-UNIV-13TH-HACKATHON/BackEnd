package org.kwakmunsu.dingdongpang.domain.subscribeshop.controller;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.service.SubscribeShopCommandService;
import org.kwakmunsu.dingdongpang.global.annotation.AuthMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/shops/{shopId}/subscriptions")
@RequiredArgsConstructor
@RestController
public class SubscribeController extends SubscribeDocsController{

    private final SubscribeShopCommandService subscribeShopCommandService;

    @Override
    @PostMapping
    public ResponseEntity<Void> subscribe(@PathVariable Long shopId, @AuthMember Long memberId) {
        subscribeShopCommandService.subscribe(shopId, memberId);

        return ResponseEntity.ok().build();
    }

    @Override
    @DeleteMapping
    public ResponseEntity<Void> unsubscribe(@PathVariable Long shopId, @AuthMember Long memberId) {
        subscribeShopCommandService.unsubscribe(shopId, memberId);

        return ResponseEntity.noContent().build();
    }

}