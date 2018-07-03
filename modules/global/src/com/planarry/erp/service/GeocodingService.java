
package com.planarry.erp.service;


public interface GeocodingService {
    String NAME = "erp_GeocodingService";

    String findAddressByShirtAddress(String address);

    String findAddressByCoordinates(Double lat, Double lng);
}