package org.kwakmunsu.dingdongpang.domain.shop.service;

import org.locationtech.jts.geom.Point;

public interface GeocodingProvider {

    Point transferToGeocode(String address);

}