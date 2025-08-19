package org.kwakmunsu.dingdongpang.domain.subscribeshop.controller;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto.SubscribeShopListResponse;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.service.SubscribeShopCommandService;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.service.SubscribeShopQueryService;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.service.dto.DailySubscriptionListResponse;
import org.kwakmunsu.dingdongpang.global.annotation.AuthMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/shops")
@RequiredArgsConstructor
@RestController
public class SubscribeController extends SubscribeDocsController {

    private final SubscribeShopCommandService subscribeShopCommandService;
    private final SubscribeShopQueryService subscribeShopQueryService;

    @Override
    @PostMapping("/{shopId}/subscriptions")
    public ResponseEntity<Void> subscribe(@PathVariable Long shopId, @AuthMember Long memberId) {
        subscribeShopCommandService.subscribe(shopId, memberId);

        return ResponseEntity.ok().build();
    }

    @Override
    @DeleteMapping("/{shopId}/subscriptions")
    public ResponseEntity<Void> unsubscribe(@PathVariable Long shopId, @AuthMember Long memberId) {
        subscribeShopCommandService.unsubscribe(shopId, memberId);

        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/subscriptions")
    public ResponseEntity<SubscribeShopListResponse> getSubscribedShop(@AuthMember Long memberId) {
        SubscribeShopListResponse response = subscribeShopQueryService.getSubscribedShop(memberId);

        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/{shopId}/subscriptions/weekly")
    public ResponseEntity<DailySubscriptionListResponse> getWeeklySubscriptions(@PathVariable Long shopId, @AuthMember Long memberId) {
        DailySubscriptionListResponse response = subscribeShopQueryService.getWeeklySubscriptions(shopId, memberId);

        return  ResponseEntity.ok(response);
    }

}