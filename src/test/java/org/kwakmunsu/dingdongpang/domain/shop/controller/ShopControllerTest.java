package org.kwakmunsu.dingdongpang.domain.shop.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.ControllerTestSupport;
import org.kwakmunsu.dingdongpang.domain.shop.entity.ShopType;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.dto.ShopListResponse;
import org.kwakmunsu.dingdongpang.domain.shop.repository.shop.dto.ShopPreviewResponse;
import org.kwakmunsu.dingdongpang.domain.shop.service.dto.ShopResponse;
import org.kwakmunsu.dingdongpang.global.TestMember;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

class ShopControllerTest extends ControllerTestSupport {

    @TestMember
    @DisplayName("매장 정보를 조회한다.")
    @Test
    void getShop() {
        var shopResponse = createShopResponse();
        given(shopQueryService.getShop(any(), any())).willReturn(shopResponse);

        MvcTestResult result = mvcTester.get().uri("/shops/{shopId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .exchange();

        assertThat(result)
                .hasStatusOk()
                .apply(print())
                .bodyJson()
                .hasPathSatisfying("$.shopName", v -> v.assertThat().isEqualTo(shopResponse.shopName()))
                .hasPathSatisfying("$.shopType", v -> v.assertThat().isEqualTo(shopResponse.shopType().toString()))
                .hasPathSatisfying("$.address", v -> v.assertThat().isEqualTo(shopResponse.address()))
                .hasPathSatisfying("$.shopTellNumber", v -> v.assertThat().isEqualTo(shopResponse.shopTellNumber()))
                .hasPathSatisfying("$.mainImage", v -> v.assertThat().isEqualTo(shopResponse.mainImage()))
                .hasPathSatisfying("$.isSubscribe", v -> v.assertThat().isEqualTo(shopResponse.isSubscribe()))
                .hasPathSatisfying("$.operationTimeResponses", v -> v.assertThat().isEmpty())
                .hasPathSatisfying("$.shopImages", v -> v.assertThat().isEmpty());
    }

    @TestMember
    @DisplayName("매장 목록을 조회한다.")
    @Test
    void getShopList() {
        var shopListResponse = getShopListResponse();

        given(shopQueryService.getShopList(any())).willReturn(shopListResponse);

        ShopPreviewResponse response = shopListResponse.responses().getFirst();
        MvcTestResult result = mvcTester.get().uri("/shops")
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
                .hasPathSatisfying("$.responses[0].isSubscribe", v -> v.assertThat().isEqualTo(response.isSubscribe()))
                .hasPathSatisfying("$.responses[0].distance", v -> v.assertThat().isEqualTo(response.distance()));
    }

    private ShopListResponse getShopListResponse() {
        ShopPreviewResponse previewResponse = new ShopPreviewResponse(
                1L,
                "테스트 매장",
                "https://example.com/image.jpg",
                "서울시 강남구 테헤란로 123",
                5L,
                false,
                100.0
        );
        ShopPreviewResponse previewResponse2 = new ShopPreviewResponse(
                2L,
                "테스트 매장2",
                "https://example.com/image.jpg",
                "서울시 강남구 테헤란로 124",
                5L,
                false,
                110.0
        );
        return ShopListResponse.builder()
                .responses(List.of(previewResponse, previewResponse2))
                .hasNext(false)
                .nextCursorId(null)
                .build();
    }

    private ShopResponse createShopResponse() {
        return ShopResponse.builder()
                .shopName("shopName")
                .shopType(ShopType.FASHION)
                .address("shopAddress")
                .shopTellNumber("010-1234-5678")
                .mainImage("mainImage")
                .isSubscribe(false)
                .operationTimeResponses(List.of())
                .shopImages(List.of())
                .build();
    }
}