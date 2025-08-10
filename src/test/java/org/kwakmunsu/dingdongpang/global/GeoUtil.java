package org.kwakmunsu.dingdongpang.global;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GeoUtil {

    private static final GeometryFactory GF = new GeometryFactory(new PrecisionModel(), 4326);

    public static Point createPoint(double latitude, double longitude) {
        // X=longitude, Y=latitude
        return GF.createPoint(new Coordinate(longitude, latitude));
    }

    public static Point createPoint() {
        // X=longitude, Y=latitude
        return GF.createPoint(new Coordinate(1.2, 2.3));
    }

}
