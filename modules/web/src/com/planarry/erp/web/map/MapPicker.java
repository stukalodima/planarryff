
package com.planarry.erp.web.map;

import com.haulmont.charts.gui.components.map.MapViewer;
import com.haulmont.charts.gui.map.model.GeoPoint;
import com.haulmont.charts.gui.map.model.InfoWindow;
import com.haulmont.charts.gui.map.model.Marker;
import com.haulmont.charts.gui.map.model.Polygon;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.planarry.erp.entity.*;
import com.planarry.erp.web.map.frame.AreaDataFrame;
import com.planarry.erp.web.utils.AddressPickerUtils;
import com.planarry.erp.web.utils.MapAssistant;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

public class MapPicker extends AbstractWindow {

    @Named("journeyDataDs")
    private Datasource<JourneyData> journeyDataDs;

    @Named("scrollBox")
    private ScrollBoxLayout scrollBox;

    @Named("additionalTimePriceField")
    private CurrencyField additionalTimePriceField;

    @Named("entrancePriceField")
    private CurrencyField entrancePriceField;

    @Named("exitPriceField")
    private CurrencyField exitPriceField;

    @Named("loadingTimePriceField")
    private CurrencyField loadingTimePriceField;

    @Named("returnDistPriceField")
    private CurrencyField returnDistPriceField;

    @Named("roadDistPriceField")
    private CurrencyField roadDistPriceField;

    @Named("roadTimePriceField")
    private CurrencyField roadTimePriceField;

    @Named("sumTimePriceField")
    private CurrencyField sumTimePriceField;

    @Named("totalPriceField")
    private CurrencyField totalPriceField;

    @Named("supplyPriceField")
    private CurrencyField supplyPriceField;

    @Named("unloadingTimePriceField")
    private CurrencyField unloadingTimePriceField;

    @Named("split")
    private SplitPanel split;

    @Named("map")
    private MapViewer map;

    @Named(MapAssistant.NAME)
    private MapAssistant mapAssistant;

    @Inject
    private ComponentsFactory componentsFactory;

    private AddressPickerUtils addressPickerUtils;

    private String choosedAddress;

    private InfoWindow infoWindow;

    private final static String POLYGON_COLOR = "#993366";
    private final static Double POLYGON_OPACITY = 0.7;

    @Override
    public void init(Map<String, Object> params) {
        Action action = (Action) params.get("action");
        switch (action) {
            case GET_POINT:
                getPoint(params);
                break;
            case SHOW_POINT:
                showPoint(params);
                break;
            case SHOW_SUPPLY_TRACK:
                showSupplyTrack(params);
                break;
            case SHOW_TRANSPORTATION_TRACK:
                createTrackWithDeliveryPoints(params);
                break;
            case SHOW_DELIVERY_DATA:
                split.setMinSplitPosition(65, 8);
                createTrackWithDeliveryPoints(params);
                showPolygon(params);
                showJourneyData(params);
        }
    }

    private void showJourneyData(Map<String, Object> params) {
        scrollBox.setVisible(true);
        JourneyData journeyData = (JourneyData) params.get("journeyData");
        String currency = (String) params.get("currency");
        setCurrencyIcon(currency);

        journeyDataDs.setItem(journeyData);
        journeyData.getAreaData().forEach(areaData -> {
            GroupBoxLayout groupBox = componentsFactory.createComponent(GroupBoxLayout.class);
            groupBox.setCollapsable(true);
            groupBox.setCaption(areaData.getArea() == null
                    ? messages.getMessage(getClass(),"outsideZone")
                    : areaData.getArea().getPolygon().getName()
            );
            scrollBox.add(groupBox);

            AreaDataFrame frame = (AreaDataFrame) openFrame(groupBox, "areaDataFrame");
            frame.initFrame(areaData, currency);
        });
    }

    private void createTrackWithDeliveryPoints(Map<String, Object> params) {
        if (params.containsKey("track") && params.get("track") != null) {
            List<GeoPoint> coordinates = mapAssistant.parseTrack(map, (String) params.get("track"));
            if (!coordinates.isEmpty()) {
                mapAssistant.createPolyline(map, coordinates, 5, "#4682B4");
                initDeliveryPoints(params);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void initDeliveryPoints(Map<String, Object> params) {
        Point point = (Point) params.get("startPoint");
        List<Point> delivery = (List<Point>) params.get("deliveryPoints");
        List<GeoPoint> geoPoints = new ArrayList<>();
        if (point != null) {
            GeoPoint start = map.createGeoPoint(point.getLatitude(), point.getLongitude());
            geoPoints.add(start);
            Marker startMarker = map.createMarker(point.getAddress(), start, false, mapAssistant.createMarkerImage(map, MapAssistant.GREEN_MARKER_URL));
            map.addMarker(startMarker);
        }

        delivery.forEach(d -> {
            GeoPoint geoPoint = map.createGeoPoint(d.getLatitude(), d.getLongitude());
            geoPoints.add(geoPoint);
            Marker marker = map.createMarker(d.getAddress(), geoPoint, false, mapAssistant.createMarkerImage(map, MapAssistant.RED_MARKER_URL));
            map.addMarker(marker);
        });
        mapAssistant.addMarkerOnClickListener(map);
        //mapAssistant.fitToBounds(map, geoPoints); //в split-панели почему-то не работает
        map.setZoom(12);
        map.setCenter(geoPoints.get(0));
    }

    @SuppressWarnings("unchecked")
    private void showPolygon(Map<String, Object> params) {
        List<? extends Area> areas = (List<? extends Area>) params.get("areas");
        areas.forEach(area -> {
            List<PolygonPoint> polygonPoint = area.getPolygon().getPolygonPoint();
            List<GeoPoint> coordinates = new ArrayList<>();
            polygonPoint.stream().sorted(Comparator.comparing(PolygonPoint::getOrder)).forEach(point -> {
                GeoPoint geoPoint = map.createGeoPoint(point.getLat(), point.getLon());
                coordinates.add(geoPoint);
            });
            if (!polygonPoint.isEmpty()) {
                Polygon mapPolygon = map.createPolygon(coordinates, POLYGON_COLOR, POLYGON_OPACITY, "#2CA860", 0, 1);
                map.addPolygonOverlay(mapPolygon);
            }
        });
    }

    private void getPoint(Map<String, Object> params) {
        if (params.containsKey("lat") && params.containsKey("lng") && params.containsKey("zoomLevel") && params.containsKey("address")) {
            showPoint(params);

        } else {
            GeoPoint center = map.createGeoPoint(50.45466, 30.5238000);
            map.setCenter(center);
            map.setZoom(10);
        }

        map.addMapClickListener(event -> {
            if (!map.getMarkers().isEmpty()) {
                map.clearMarkers();
            }
            if (infoWindow != null) {
                map.closeInfoWindow(infoWindow);
            }

            Marker marker = map.createMarker("", event.getPosition(), false);
            findAddress(event.getPosition().getLatitude(), event.getPosition().getLongitude());
            map.addMarker(marker);

            createInfoWindow(marker, choosedAddress);
        });
    }

    private void showPoint(Map<String, Object> params) {
        MapAssistant.ZoomLevel zoomLevel = (MapAssistant.ZoomLevel) params.get("zoomLevel");
        Double lat = (Double) params.get("lat");
        Double lng = (Double) params.get("lng");
        String address = (String) params.get("address");

        GeoPoint center = map.createGeoPoint(lat, lng);
        Marker marker = map.createMarker("", center, false);
        map.addMarker(marker);
        map.setCenter(center);
        map.setZoom(mapAssistant.getZoomByZoomLevel(zoomLevel));
        createInfoWindow(marker, address);
    }

    private void showSupplyTrack(Map<String, Object> params) {
        List<GeoPoint> coordinates = mapAssistant.parseTrack(map, (String) params.get("track"));
        if (!coordinates.isEmpty()) {
            GeoPoint center = coordinates.get(coordinates.size() / 2);
            Marker startMarker = map.createMarker("", coordinates.get(0), false, mapAssistant.createMarkerImage(map, MapAssistant.GREEN_MARKER_URL));
            Marker finishMarker = map.createMarker("", coordinates.get(coordinates.size() - 1), false, mapAssistant.createMarkerImage(map, MapAssistant.RED_MARKER_URL));
            map.addMarker(startMarker);
            map.addMarker(finishMarker);
            mapAssistant.fitToBounds(map, Arrays.asList(startMarker.getPosition(), finishMarker.getPosition()));

            if (params.get("startAddress") != null && params.get("endAddress") != null) {
                startMarker.setCaption(mapAssistant.formatPointAddressInfo((String) params.get("startAddress"), startMarker));
                finishMarker.setCaption(mapAssistant.formatPointAddressInfo((String) params.get("endAddress"), finishMarker));
                mapAssistant.addMarkerOnClickListener(map);
            }
            mapAssistant.createPolyline(map, coordinates, 5, "#4682B4");
            createTrackInfo((String) params.get("dist"), (String) params.get("h"), (String) params.get("m"), center);
        }
    }

    private void createTrackInfo(String dist, String hours, String minutes, GeoPoint pos) {
        String textDist = messages.getMessage(getClass(), "dist") + dist + " " + messages.getMessage(getClass(), "km") + "</br>";
        String textTime = messages.getMessage(getClass(), "time") + hours + " " + messages.getMessage(getClass(), "hours") + " "
                + minutes + " " + messages.getMessage(getClass(), "minutes");

        InfoWindow w = map.createInfoWindow("<span style=\"color: #000000; font-size: 14px;\">" + textDist + textTime + "</span>");
        w.setPosition(pos);
        map.openInfoWindow(w);
    }

    private void findAddress(Double lat, Double lng) {
        choosedAddress = addressPickerUtils.findAddressByCoordinates(lat, lng, mapAssistant.getZoomLevel(map.getZoom()), getFrame());
    }

    private void createInfoWindow(Marker marker, String address) {
        if (address != null) {
            infoWindow = map.createInfoWindow(address, marker);
            map.openInfoWindow(infoWindow);
        }
    }

    private void setCurrencyIcon(String currency){
        additionalTimePriceField.setCurrency(currency);
        entrancePriceField.setCurrency(currency);
        exitPriceField.setCurrency(currency);
        loadingTimePriceField.setCurrency(currency);
        returnDistPriceField.setCurrency(currency);
        roadDistPriceField.setCurrency(currency);
        roadTimePriceField.setCurrency(currency);
        sumTimePriceField.setCurrency(currency);
        totalPriceField.setCurrency(currency);
        supplyPriceField.setCurrency(currency);
        unloadingTimePriceField.setCurrency(currency);
    }

    public String getAddress() {
        return choosedAddress;
    }


    public void setAddressPickerUtils(AddressPickerUtils addressPickerUtils) {
        this.addressPickerUtils = addressPickerUtils;
    }

    public void closeWindow() {
        close(Window.CLOSE_ACTION_ID, true);
    }

    public enum Action {
        GET_POINT,
        SHOW_SUPPLY_TRACK,
        SHOW_TRANSPORTATION_TRACK,
        SHOW_POINT,
        SHOW_DELIVERY_DATA
    }
}