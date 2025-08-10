package org.kwakmunsu.dingdongpang.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GeoUtil {

    private static final double R_KM = 6371.0088; // 평균 지구 반경

    /**
     * X=longitude, Y=latitude
     * 위도·경도 두 점 사이의 지구 표면 최단거리(대권거리)를 구하는 하버사인(Haversine) 공식
    **/
    public static double distanceKm(double memberLatitude, double memberLongitude, double shopLatitude, double shopLongitude) {
        double distanceLatitude = Math.toRadians(shopLatitude - memberLatitude);
        double distanceLongitude = Math.toRadians(shopLongitude - memberLongitude);

        double a = Math.sin(distanceLatitude / 2) * Math.sin(distanceLatitude / 2)
                + Math.cos(Math.toRadians(memberLatitude)) * Math.cos(Math.toRadians(shopLatitude))
                * Math.sin(distanceLongitude / 2) * Math.sin(distanceLongitude / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R_KM * c;
    }

}