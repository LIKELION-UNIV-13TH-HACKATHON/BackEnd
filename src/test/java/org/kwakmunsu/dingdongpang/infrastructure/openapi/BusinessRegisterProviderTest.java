package org.kwakmunsu.dingdongpang.infrastructure.openapi;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
record BusinessRegisterProviderTest(BusinessRegisterProvider businessRegisterProvider) {

    @DisplayName("사업자 등록 여부를 조회한다.")
    @Test
    void getRegistrationBusiness() {
        var number = "8962801461";

        boolean register = businessRegisterProvider.isRegister(number);

        assertThat(register).isTrue();
    }

    @DisplayName("존재하지 않을 경우 false를 반환한다.")
    @Test
    void failGetRegistrationBusiness() {
        var number = "invalid-number";

        boolean register = businessRegisterProvider.isRegister(number);

        assertThat(register).isFalse();
    }

}