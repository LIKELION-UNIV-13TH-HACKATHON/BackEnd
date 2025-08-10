package org.kwakmunsu.dingdongpang.infrastructure.geocoding;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.dingdongpang.global.exception.InternalServerException;
import org.kwakmunsu.dingdongpang.global.exception.dto.ErrorStatus;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
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

    private static final GeometryFactory GF = new GeometryFactory(new PrecisionModel(), 4326);
    private static final String GEOCODING_URL = "https://dapi.kakao.com/v2/local/search/address.json";
    private final RestTemplate restTemplate;

    @Value("${kakao.rest.api-key}")
    private String kakaoRestApiKey;

    public Point transferToGeocode(String address) {
        ResponseEntity<?> response = getGeocodeFromKakaoServer(address);

        Map<String, Object> attributes = (Map<String, Object>) response.getBody();

        return getLocation(attributes);
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

    private Point getLocation(Map<String, Object> attributes) {
        try {
            List<Map<String, Object>> documents = (List<Map<String, Object>>) requireNonNull(attributes).get("documents");

            Map<String, Object> document = documents.getFirst();
            Map<String, Object> roadAddress = (Map<String, Object>) document.get("road_address");

            double longitude = Double.parseDouble(String.valueOf(roadAddress.get("x")));
            double latitude = Double.parseDouble(String.valueOf(roadAddress.get("y")));

            return createPoint(longitude, latitude);
        } catch (NullPointerException | NoSuchElementException e) {
            throw new InternalServerException(ErrorStatus.FAIL_TRANSFER_GEOCODE, e);
        }
    }

    Point createPoint(double longitude, double latitude) {
        // X=longitude, Y=latitude
        return GF.createPoint(new Coordinate(longitude, latitude));
    }

}