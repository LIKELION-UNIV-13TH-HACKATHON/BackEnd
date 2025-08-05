package org.kwakmunsu.dingdongpang.infrastructure.geocoding;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.global.exception.InternalServerException;
import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class KakaoGeocodingProvider {

    private static final String GEOCODING_URL = "https://dapi.kakao.com/v2/local/search/address.json";
    private final RestTemplate restTemplate;

    @Value("${kakao.rest.api-key}")
    private String kakaoRestApiKey;

    public GeocodeResponse transferToGeocode(String address) {
        ResponseEntity<?> response = getGeocodeFromKakaoServer(address);

        Map<String, Object> attributes = (Map<String, Object>) response.getBody();

        return getGeocodeResponse(attributes);

    }

    private ResponseEntity<?> getGeocodeFromKakaoServer(String address) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoRestApiKey);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        String url = GEOCODING_URL + "?query=" + address;

        return restTemplate
                .exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        Map.class
                );
    }

    private GeocodeResponse getGeocodeResponse(Map<String, Object> attributes) {
        try {
            List<Map<String, Object>> documents = (List<Map<String, Object>>) requireNonNull(attributes).get("documents");

            Map<String, Object> document = documents.getFirst();
            Map<String, Object> roadAddress = (Map<String, Object>) document.get("road_address");
            String latitude = String.valueOf(roadAddress.get("y"));
            String longitude = String.valueOf(roadAddress.get("x"));

            return new GeocodeResponse(latitude, longitude);
        } catch (NullPointerException | NoSuchElementException e) {
            throw new InternalServerException(ErrorStatus.FAIL_TRANSFER_GEOCODE, e);
        }

    }

}