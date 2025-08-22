package org.kwakmunsu.dingdongpang.global.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kwakmunsu.dingdongpang.domain.shop.service.GeocodingProvider;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class GeoUtilTest {

    @Autowired
    GeocodingProvider geocodingProvider;

    @DisplayName("거리 계산")
    @Test
    void cal() {
        var address = "경기 광주시 경충대로 1477";
        Point point = geocodingProvider.transferToGeocode(address);
        log.info("{}", point);
        var address2 = "경기 광주시 경충대로 1411 투썸플레이스";
        Point point2 = geocodingProvider.transferToGeocode(address2);
        log.info("{}", point2);
        double v = GeoUtil.distanceKm(point2.getY(), point2.getX(), point.getY(), point.getX());
        log.info("{}", v);
    }

}