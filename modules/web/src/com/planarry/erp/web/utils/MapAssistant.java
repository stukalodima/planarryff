package com.planarry.erp.web.utils;

import com.haulmont.charts.gui.components.map.MapViewer;
import com.haulmont.charts.gui.map.model.GeoPoint;
import com.haulmont.charts.gui.map.model.InfoWindow;
import com.haulmont.charts.gui.map.model.Marker;
import com.haulmont.charts.gui.map.model.Polyline;
import com.haulmont.charts.gui.map.model.base.MarkerImage;
import com.planarry.erp.entity.PolygonPoint;
import org.json.JSONArray;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component(MapAssistant.NAME)
public class MapAssistant {

    public static final String NAME = "erp_MapAssistant";

    public static final String GREEN_MARKER_URL = "https://www.clker.com/cliparts/C/O/J/w/6/G/google-maps-marker-for-residencelamontagne-md.png";
    public static final String RED_MARKER_URL = "http://www.clker.com/cliparts/v/E/r/a/2/E/google-maps-marker-for-residencelamontagne.svg.med.png";
    public static final String BLUE_MARKER_URL = "http://www.clker.com/cliparts/m/u/A/B/A/b/icon-pin-336699-md.png";
    public static final String ORANGE_MARKER_URL = "http://www.clker.com/cliparts/F/N/i/U/3/q/map-pin-orange.svg.hi.png";
    public static final String TRUCK_MARKER_URL = "https://sngtrans.com.ua/img/google/new/car_sngt.png";

    public List<GeoPoint> parseTrack(MapViewer map, String track) {
        List<GeoPoint> coordinates = new ArrayList<>();
        if (track != null) {
            JSONArray jsonArray = new JSONArray(track);
            for (int i = 0; i < jsonArray.length(); i++) {
                coordinates.add(map.createGeoPoint(jsonArray.getJSONArray(i).getDouble(1), jsonArray.getJSONArray(i).getDouble(0)));
            }
        }
        return coordinates;
    }

    public Polyline createPolyline(MapViewer map, List<GeoPoint> coordinates, int stroke, String color) {
        Polyline polyline = map.createPolyline(coordinates);
        polyline.setStrokeWeight(stroke);
        polyline.setStrokeColor(color);
        map.addPolyline(polyline);
        return polyline;
    }

    public MarkerImage createMarkerImage(MapViewer map, String imageUrl) {
        MarkerImage markerImage = map.createMarkerImage(imageUrl);
        markerImage.setSize(map.createSize(30, 45));
        markerImage.setScaledSize(map.createSize(30, 45));
        return markerImage;
    }

    public void fitToBounds(MapViewer map, List<GeoPoint> geoPoints) {
        if (geoPoints.size() == 1){
            map.setCenter(geoPoints.get(0));
            map.setZoom(13);
        }
        else if (geoPoints.size() > 1) {
            double minLat = Double.MAX_VALUE;
            double maxLat = Double.MIN_VALUE;
            double minLng = Double.MAX_VALUE;
            double maxLng = Double.MIN_VALUE;
            for (GeoPoint geoPoint : geoPoints) {
                minLat = geoPoint.getLatitude() < minLat ? geoPoint.getLatitude() : minLat;
                maxLat = geoPoint.getLatitude() > maxLat ? geoPoint.getLatitude() : maxLat;
                minLng = geoPoint.getLongitude() < minLng ? geoPoint.getLongitude() : minLng;
                maxLng = geoPoint.getLongitude() > maxLng ? geoPoint.getLongitude() : maxLng;
            }
            map.fitToBounds(map.createGeoPoint(maxLat, maxLng), map.createGeoPoint(minLat, minLng));
        }
    }

    public void fitToBoundsByAllMarkers(MapViewer map){
        Collection<Marker> markers = map.getMarkers();
        List<GeoPoint> geoPoints = markers.stream().map(Marker::getPosition).collect(Collectors.toList());
        fitToBounds(map, geoPoints);
    }

    public void addMarkerOnClickListener(MapViewer map) {
        map.addMarkerClickListener(event -> {
            Marker marker = event.getMarker();
            InfoWindow info = map.createInfoWindow("<span style=\"color: #000000; font-size: 14px;\">" + marker.getCaption() + "</span>", marker);
            map.openInfoWindow(info);
        });
    }

    public String formatPointAddressInfo(String address, Marker marker) {
        return String.format(address + " %.4f, %.4f", marker.getPosition().getLatitude(), marker.getPosition().getLongitude());
    }

    public ZoomLevel getZoomLevel(int zoom) {
        ZoomLevel addressLevel;
        if (zoom < 10) {
            addressLevel = ZoomLevel.COUNTRY_LEVEL;
        } else if (zoom < 14) {
            addressLevel = ZoomLevel.CITY_LEVEL;
        } else if (zoom < 17) {
            addressLevel = ZoomLevel.STREET_LEVEL;
        } else {
            addressLevel = ZoomLevel.BUILDING_LEVEL;
        }
        return addressLevel;
    }

    public ZoomLevel getZoomLevel(String street, String building) {
        ZoomLevel zoomLevel;
        if (building != null) {
            zoomLevel = ZoomLevel.BUILDING_LEVEL;
        } else if (street != null) {
            zoomLevel = ZoomLevel.STREET_LEVEL;
        } else {
            zoomLevel = ZoomLevel.COUNTRY_LEVEL;
        }
        return zoomLevel;
    }

    public int getZoomByZoomLevel(ZoomLevel zoomLevel) {
        int zoom;
        switch (zoomLevel) {
            case COUNTRY_LEVEL:
                zoom = 10;
                break;
            case CITY_LEVEL:
                zoom = 12;
                break;
            case STREET_LEVEL:
                zoom = 14;
                break;
            case BUILDING_LEVEL:
                zoom = 18;
                break;
            default:
                zoom = 10;
                break;
        }
        return zoom;
    }

    public enum ZoomLevel {
        COUNTRY_LEVEL,
        CITY_LEVEL,
        STREET_LEVEL,
        BUILDING_LEVEL
    }
}
