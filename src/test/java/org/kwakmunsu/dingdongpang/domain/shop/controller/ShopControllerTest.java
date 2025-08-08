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
        given(sqsCommandService.getShop(any(), any())).willReturn(shopResponse);

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