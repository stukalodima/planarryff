package com.planarry.erp.service;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;


public interface RouterService {

    String getRoute(Double startLat, Double startLng, Double endLat, Double endLng);

    String getRoute(List<String> coordinates);

    String parseRouteTrack(String resultJson);

    Integer parseRouteTime(String resultJson);

    Double parseRouteDistance(String resultJson);

}