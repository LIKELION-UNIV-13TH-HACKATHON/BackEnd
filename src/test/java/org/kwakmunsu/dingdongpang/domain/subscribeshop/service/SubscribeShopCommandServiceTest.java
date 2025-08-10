package org.kwakmunsu.dingdongpang.domain.subscribeshop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.member.entity.Member;
import org.kwakmunsu.dingdongpang.domain.member.repository.MemberRepository;
import org.kwakmunsu.dingdongpang.domain.member.service.dto.ShopRegisterServiceRequest;
import org.kwakmunsu.dingdongpang.domain.shop.entity.Shop;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.ShopRepository;
import org.kwakmunsu.dingdongpang.domain.shop.service.ShopCommandService;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.SubscribeShopRepository;
import org.kwakmunsu.dingdongpang.global.GeoFixture;
import org.kwakmunsu.dingdongpang.global.exception.DuplicationException;
import org.kwakmunsu.dingdongpang.global.exception.NotFoundException;
import org.locationtech.jts.geom.Point;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
record SubscribeShopCommandServiceTest(
        SubscribeShopCommandService subscribeShopCommandService,
        ShopCommandService shopCommandService,
        SubscribeShopRepository subscribeShopRepository,
        ShopRepository shopRepository,
        MemberRepository memberRepository
) {

    @DisplayName("매장 구독을 한다. 또한 이미 구독이 되어있다면 예외를 던진다.")
    @Test
    void subscribe() {
        var member = Member.createMember("testEmail@gmail.com", "nickname", "1234");
        memberRepository.save(member);

        var shopRegisterServiceRequest = getShopRegisterServiceRequest();
        Point point = GeoFixture.createPoint(1.2, 2.3);
        shopCommandService.register(shopRegisterServiceRequest, point, member.getId());

        Shop shop = shopRepository.findByMemberId(member.getId());
        var subscribeMember = 9999999L;

        subscribeShopCommandService.subscribe(shop.getId(), subscribeMember);

        boolean isSubscribed = subscribeShopRepository.existsByMemberIdAndShopId(subscribeMember, shop.getId());
        assertThat(isSubscribed).isTrue();

        // 이미 구독이 되어있다면 예외를 던진다
        assertThatThrownBy(() -> subscribeShopCommandService.subscribe(shop.getId(), subscribeMember))
                .isInstanceOf(DuplicationException.class);
    }

    @DisplayName("구독할 매장이 존재하지 않을 경우 예외를 던진다.")
    @Test
    void failSubscribe() {
        assertThatThrownBy(() -> subscribeShopCommandService.subscribe(-999L, 1L))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("구독 취소를 한다.")
    @Test
    void unSubscribe() {
        var member = Member.createMember("testEmail@gmail.com", "nickname", "1234");
        memberRepository.save(member);

        var shopRegisterServiceRequest = getShopRegisterServiceRequest();
        Point point = GeoFixture.createPoint(1.2, 2.3);
        shopCommandService.register(shopRegisterServiceRequest, point, member.getId());

        Shop shop = shopRepository.findByMemberId(member.getId());
        var subscribeMember = 9999999L;

        subscribeShopCommandService.subscribe(shop.getId(), subscribeMember);
        boolean isSubscribed = subscribeShopRepository.existsByMemberIdAndShopId(subscribeMember, shop.getId());
        assertThat(isSubscribed).isTrue();

        // 구독 취소
        subscribeShopCommandService.unsubscribe(shop.getId(), subscribeMember);

        isSubscribed = subscribeShopRepository.existsByMemberIdAndShopId(subscribeMember, shop.getId());
        assertThat(isSubscribed).isFalse();

    }

    private ShopRegisterServiceRequest getShopRegisterServiceRequest() {
        return new ShopRegisterServiceRequest(
                "My Shop",                                  // shopName
                ShopType.FOOD,                              // shopType (예: enum)
                "010-1234-5678",                            // shopPhoneNumber
                "서울특별시 강남구 역삼동 123-45",                 // address
                "1234567890",                               // businessNumber
                "홍길동",                                     // ownerName
                null,                                  // mainImage
                List.of(),                    // imageFiles
                List.of()                     // operationTimeRequests
        );
    }

}