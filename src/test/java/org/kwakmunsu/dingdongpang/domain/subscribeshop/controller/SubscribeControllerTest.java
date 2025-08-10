package org.kwakmunsu.dingdongpang.domain.subscribeshop.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.ControllerTestSupport;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto.SubscribeShopListResponse;
import org.kwakmunsu.dingdongpang.domain.subscribeshop.repository.dto.SubscribeShopPreviewResponse;
import org.kwakmunsu.dingdongpang.global.TestMember;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

class SubscribeControllerTest extends ControllerTestSupport {

    @TestMember
    @DisplayName("매장 구독을 한다")
    @Test
    void subscribe() {
        assertThat(mvcTester.post().uri("/shops/{shopId}/subscriptions", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .hasStatusOk()
                .apply(print());
    }

    @TestMember
    @DisplayName("매장 구독을 취소 한다")
    @Test
    void unsubscribe() {
        assertThat(mvcTester.delete().uri("/shops/{shopId}/subscriptions", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .hasStatus(HttpStatus.NO_CONTENT)
                .apply(print());
    }

    @TestMember
    @DisplayName("구독 매장 목록을 조회한다.")
    @Test
    void getSubscribeShopList() {
        var subscribeShopListResponse = getSubscribeShopListResponse();

        given(subscribeShopQueryService.getSubscribedShop(any())).willReturn(subscribeShopListResponse);

        var response = subscribeShopListResponse.responses().getFirst();
        MvcTestResult result = mvcTester.get().uri("/shops/subscriptions")
                .param("longitude", "123.231")
                .param("latitude", "13.1")
                .contentType(MediaType.APPLICATION_JSON)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .apply(print())
                .bodyJson()
                .hasPathSatisfying("$.responses[0].shopId", v -> v.assertThat().isEqualTo(response.shopId().intValue()))
                .hasPathSatisfying("$.responses[0].shopName", v -> v.assertThat().isEqualTo(response.shopName()))
                .hasPathSatisfying("$.responses[0].mainImage", v -> v.assertThat().isEqualTo(response.mainImage()))
                .hasPathSatisfying("$.responses[0].address", v -> v.assertThat().isEqualTo(response.address()))
                .hasPathSatisfying("$.responses[0].distance", v -> v.assertThat().isEqualTo(response.distance()));
    }

    private SubscribeShopListResponse getSubscribeShopListResponse() {
        SubscribeShopPreviewResponse previewResponse = new SubscribeShopPreviewResponse(
                1L,
                "테스트 매장",
                "https://example.com/image.jpg",
                "서울시 강남구 테헤란로 123",
                5L,
                100.0
        );
        SubscribeShopPreviewResponse previewResponse2 = new SubscribeShopPreviewResponse(
                2L,
                "테스트 매장2",
                "https://example.com/image.jpg",
                "서울시 강남구 테헤란로 124",
                5L,
                110.0
        );
        return SubscribeShopListResponse.builder()
                .responses(List.of(previewResponse, previewResponse2))
                .hasNext(false)
                .nextCursorId(null)
                .build();
    }

}