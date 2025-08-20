package org.kwakmunsu.dingdongpang.infrastructure.openapi;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Component
public class BusinessRegisterProvider {

    private static final String BUSINESS_INFO_URL = "https://api.odcloud.kr/api/nts-businessman/v1/status";
    private final RestClient restClient;

    @Value("${openapi.service-key}")
    private String openapiServiceKey;

    public boolean isRegister(String businessRegistrationNumber) {
        BusinessRegistrationResponse response = getBusinessStatusFromOpenApi(businessRegistrationNumber);

        String businessStatus = response.data().getFirst().b_stt_cd();

        log.info("Business Status is {}", businessStatus);
        // 01 일 경우 사업자 등록, 02 | 03  휴업자 및 폐업자, 빈 값은 존재하지 않는 경우
        return businessStatus.equals("01");
    }

    private BusinessRegistrationResponse getBusinessStatusFromOpenApi(String businessRegistrationNumber) {
        Map<String, List<String>> requestBody = Map.of("b_no", Collections.singletonList(businessRegistrationNumber));
        String url = BUSINESS_INFO_URL + "?serviceKey=" + openapiServiceKey;

        return restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(BusinessRegistrationResponse.class);
    }

}