package com.planarry.erp.service;

import com.google.common.base.Strings;
import com.planarry.erp.utils.HttpUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public abstract class AbstractRouterService implements RouterService {
    Logger log = LoggerFactory.getLogger(RouterService.class);

    @Override
    public String getRoute(Double startLat, Double startLng, Double endLat, Double endLng) {
        String url = getUrl() + startLng + "," + startLat + ";" + endLng + "," + endLat;
        Map<String, Object> params = new HashMap<>();
        params.put("overview", "full");
        params.put("geometries", "geojson");

        try {
            return doRequest(url, params);
        } catch (IOException | URISyntaxException e) {
            log.error(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public String getRoute(List<String> coordinates) {
        //in osm - first LON, last LAT, so need reverse the coordinates and concat them
        StringJoiner joiner = new StringJoiner(";");
        coordinates.forEach(joiner::add);
        String url = getUrl() + joiner.toString();
        Map<String, Object> params = new HashMap<>();
        params.put("overview", "full");
        params.put("geometries", "geojson");

        try {
            return doRequest(url, params);
        } catch (IOException | URISyntaxException e) {
            log.error(e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public String parseRouteTrack(String resultJson) {
        if (!Strings.isNullOrEmpty(resultJson)) {
            JSONObject jsonObject = new JSONObject(resultJson);
            if (jsonObject.has("routes")) {
                JSONArray routes = jsonObject.getJSONArray("routes");
                if (routes.length() > 0) {
                    JSONObject geometry = routes.getJSONObject(0).getJSONObject("geometry");
                    JSONArray coordinates = geometry.getJSONArray("coordinates");
                    return coordinates.toString();
                }
            }
        }
        return null;
    }

    @Override
    public Integer parseRouteTime(String resultJson) {
        if (!Strings.isNullOrEmpty(resultJson)) {
            JSONObject jsonObject = new JSONObject(resultJson);
            if (jsonObject.has("routes")) {
                JSONArray routes = jsonObject.getJSONArray("routes");
                if (routes.length() > 0) {
                    double time = routes.getJSONObject(0).getDouble("duration");
                    return (int) time / 60;
                }
            }
        }
        return null;
    }

    @Override
    public Double parseRouteDistance(String resultJson) {
        if (!Strings.isNullOrEmpty(resultJson)) {
            JSONObject jsonObject = new JSONObject(resultJson);
            if (jsonObject.has("routes")) {
                JSONArray routes = jsonObject.getJSONArray("routes");
                if (routes.length() > 0) {
                    return routes.getJSONObject(0).getDouble("distance") / 1000;
                }
            }
        }
        return null;
    }

    private String doRequest(String url, Map<String, Object> params) throws IOException, URISyntaxException {
        HttpUtils http = new HttpUtils();
        int codeResponse = http.get(url, params);
        boolean goodResult = codeResponse >= 200 && codeResponse < 300;

        if (!goodResult) {
            log.error(getClass() + "Answer from route server " + String.valueOf(codeResponse));
        } else if (http.getResultStr().isEmpty()) {
            log.error(getClass() + "Loss connection to route server");
        } else {
            return http.getResultStr();
        }
        return null;
    }

    abstract protected String getUrl();
}
