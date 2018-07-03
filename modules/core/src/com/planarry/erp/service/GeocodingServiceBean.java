
package com.planarry.erp.service;

import com.google.common.collect.ImmutableMap;
import com.haulmont.cuba.core.sys.AppContext;
import com.planarry.erp.utils.HttpUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;


@Service(GeocodingService.NAME)
public class GeocodingServiceBean implements GeocodingService {

    private String googleKey = AppContext.getProperty("charts.map.apiKey");

    @Override
    public String findAddressByShirtAddress(String address) {
        if (address == null || address.isEmpty()){
            return "{status : INVALID_REQUEST}";
        }

        Map<String, Object> params = ImmutableMap.of(
                "address", address,
                "language", "ru",
                "key", googleKey);

        return doRequest(params);
    }

    @Override
    public String findAddressByCoordinates(Double lat, Double lng) {
        if (lat == null || lng == null){
            return "{status : INVALID_REQUEST}";
        }
        Map<String, Object> params = ImmutableMap.of(
                "latlng", lat + "," + lng,
                "language", "ru",
                "result_type", "street_address|locality",
                "key", googleKey);
        return doRequest(params);
    }

    private String doRequest(Map<String, Object> params){
        try {
            HttpUtils http = new HttpUtils();
            String url = "https://maps.googleapis.com/maps/api/geocode/json";
            int codeResponse = http.get(url, params);
            boolean goodResult = codeResponse >= 200 && codeResponse < 300;
            if (goodResult && !http.getResultStr().isEmpty()) {
                return http.getResultStr();
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return "{status : UNKNOWN_ERROR}";
    }
}