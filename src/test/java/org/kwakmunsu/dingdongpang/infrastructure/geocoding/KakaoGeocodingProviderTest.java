package org.kwakmunsu.dingdongpang.infrastructure.geocoding;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.global.exception.InternalServerException;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled("IP 주소 상시 변경으로 비활성화")
@Slf4j
@SpringBootTest
record KakaoGeocodingProviderTest(KakaoGeocodingProvider kakaoGeocodingProvider) {

    @DisplayName("주소에 해당하는 위도 경도를 조회힌다.")
    @Test
    void transferToGeocode() {
        var address = "경기도 광주시 경충대로1461번길 12-4 코오롱 세이브 프라자 202호";

        var response = kakaoGeocodingProvider.transferToGeocode(address);

        assertThat(response).isNotNull();
        log.info(response.toString());
    }

    @DisplayName("유효하지 않은 주소 일 경우 에러를 던진다.")
    @Test
    void failTransferToGeocode() {
        var address = "invalid-address";

        assertThatThrownBy(() -> kakaoGeocodingProvider.transferToGeocode(address))
            .isInstanceOf(InternalServerException.class);
    }

}