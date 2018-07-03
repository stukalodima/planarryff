
package com.planarry.erp.service;

import org.springframework.stereotype.Service;

@Service("erp_DefaultRouterService")
public class DefaultRouterServiceBean extends AbstractRouterService implements RouterService {

    private static final String URL = "http://sngtrans.com.ua/osrm/route/v1/driving/";

    @Override
    protected String getUrl() {
        return URL;
    }

    // реализация под свой метод VIAROUTE
    /*public String getRoute(Double startLat, Double startLng, Double endLat, Double endLng) {
        String url = "http://router.s2.sngtrans.com.ua/viaroute";
        Map<String, Object> params = new HashMap<>();
        params.put("start", startLat + "," + startLng);
        params.put("dest", endLat + "," + endLng);
        params.put("transport", "0,0");
        params.put("z", 15);
        params.put("alt", false);
        params.put("instructions", false);
        params.put("compression", false);

        try {
            return doRequest(url, params);
        } catch (IOException | URISyntaxException e) {
            log.error(e.getLocalizedMessage());
            return null;
        }
    }

    public String  getRoute(List<String> coordinates){
        String url = "http://router.s2.sngtrans.com.ua/viaroute";
        Map<String, Object> params = new HashMap<>();
        params.put("loc", coordinates);
        params.put("transport", "0,0");
        params.put("z", 15);
        params.put("alt", false);
        params.put("instructions", false);
        params.put("compression", false);

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
            JSONArray routeGeometry = jsonObject.getJSONArray("route_geometry");
            return routeGeometry.toString();
        }
        return null;
    }

    //return route time in minutes
    public Integer parseRouteTime(String resultJson) {
        if (!Strings.isNullOrEmpty(resultJson)) {
            JSONObject jsonObject = new JSONObject(resultJson);
            JSONObject summary = jsonObject.getJSONObject("route_summary");
            return summary.getInt("total_time") / 60;
        }
        return null;
    }

    //return route distance in kilometers
    public Double parseRouteDistance(String resultJson) {
        if (!Strings.isNullOrEmpty(resultJson)) {
            JSONObject jsonObject = new JSONObject(resultJson);
            JSONObject summary = jsonObject.getJSONObject("route_summary");
            return summary.getDouble("total_distance") / 1000;
        }
        return null;
    }*/
}