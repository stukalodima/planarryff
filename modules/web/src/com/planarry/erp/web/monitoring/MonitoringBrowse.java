package com.planarry.erp.web.monitoring;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.charts.gui.components.map.MapViewer;
import com.haulmont.charts.gui.map.model.GeoPoint;
import com.haulmont.charts.gui.map.model.Marker;
import com.haulmont.charts.gui.map.model.Polyline;
import com.haulmont.charts.gui.map.model.base.MarkerImage;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DatatypeFormatter;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Timer;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.planarry.erp.entity.*;
import com.planarry.erp.service.GpsService;
import com.planarry.erp.service.MonitoringService;
import com.planarry.erp.web.utils.ControllerAssistant;
import com.planarry.erp.web.utils.MapAssistant;
import org.apache.commons.lang.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.Calendar;
import java.util.stream.Collectors;

public class MonitoringBrowse extends AbstractWindow {

    @Inject
    private CollectionDatasource<Cargo, UUID> cargoesDS;
    @Inject
    private CollectionDatasource<Journey, UUID> journeysDS;
    @Inject
    private CollectionDatasource<TransportState, UUID> transportsStateDS;

    @Inject
    private Table<Cargo> cargoTable;

    @Inject
    private Table<Journey> journeyTable;

    @Inject
    private Table<TransportState> transportStateTable;

    @Named("showManualJourneyChkbox")
    private CheckBox showManualJourneyChkbox;

    @Named("currentJourneyLocationChkbox")
    private CheckBox currentJourneyLocationChkbox;

    @Named("currentTransportLocationChkbox")
    private CheckBox currentTransportLocationChkbox;

    @Named("dateField")
    private DateField dateField;

    @Named("map")
    private MapViewer map;

    @Inject
    private ComponentsFactory componentsFactory;

    @Named(MapAssistant.NAME)
    private MapAssistant mapAssistant;

    @Named(ControllerAssistant.NAME)
    private ControllerAssistant controllerAssistant;

    @Named(MonitoringService.NAME)
    private MonitoringService monitoringService;

    @Named(DatatypeFormatter.NAME)
    private DatatypeFormatter datatypeFormatter;

    @Named(GpsService.NAME)
    private GpsService gpsService;

    private MonitoringSettings settings;

    //list contains startMarker, endMarker, polyline
    private Map<Journey, List<Object>> journeyMapData = new HashMap<>();
    private Map<Transport, Marker> transportStateMapData = new HashMap<>();
    private Map<Cargo, Marker> cargoMapData = new HashMap<>();
    private Map<Transport, Marker> transportLocationMapData = new HashMap<>();
    private List<Journey> manualJourneyList = new ArrayList<>();

    @Inject
    private TimeSource timeSource;

    @Override
    public void init(Map<String, Object> params) {
        params.put("now", timeSource.currentTimestamp());
        super.init(params);
        initMap();

        addCargoTableColumnGenerator();
        addJourneyTableColumnGenerator();
        addTransportStateTableColumnGenerator();
        //addManualJourneyCheckBoxValueChangeListener();
        addCurrentJourneyLocationCheckBoxValueChangeListener();
        addCurrentTransportLocationCheckBoxValueChangeListener();
        loadSettings();
        createTimer();
        addDateFieldValueChangeListener();

        dateField.setValue(timeSource.currentTimestamp());
        mapAssistant.addMarkerOnClickListener(map);
        journeyTable.setItemClickAction(new BaseAction("doubleClick").withHandler(e -> {
                    if (!journeyTable.getSingleSelected().getManualJourney()) {
                        openWindow("journeyCompletion", WindowManager.OpenType.DIALOG, ParamsMap.of("journey", journeyTable.getSingleSelected()));
                    }
                }
        ));
    }

    @Override
    protected boolean preClose(String actionId) {
        stopTimer();
        return super.preClose(actionId);
    }

    private void initMap() {
        map.setCenter(map.createGeoPoint(50.45466, 30.5238000));
        map.setZoom(10);
        map.setMaxZoom(19);
    }

    private void loadSettings() {
        settings = monitoringService.getMonitoringSettingsByUser(controllerAssistant.getCurrentUser());
    }

    private void createTimer() {
        Timer timer = componentsFactory.createTimer();
        addTimer(timer);
        timer.setId("timer");
        timer.setDelay(settings.getUpdatePeriod() * 60 * 1000);
        timer.setRepeating(true);
        timer.addActionListener(tmpTimer -> {
            updateLocation(journeysDS, currentJourneyLocationChkbox);
            updateLocation(transportsStateDS, currentTransportLocationChkbox);
        });
        timer.start();
    }

    private <T extends UpdatableMonitoringEntity> void updateLocation(CollectionDatasource<T, UUID> datasource, CheckBox checkBox) {
        if (checkBox.isChecked()) {
            datasource.getItems().forEach(item -> {
                if (item.getFlag()) {
                    addCurrentTransportLocationMarker(item.getTransport());
                }
            });
        }
    }

    private void addDateFieldValueChangeListener(){
        dateField.addValueChangeListener(e -> {
            if (e.getValue() == null){
                dateField.setValue(DateUtils.truncate(timeSource.currentTimestamp(), Calendar.DATE));
            } else {
                journeysDS.setItem(null);
                onHideJourney();

                Map<String, Object> customParams = new HashMap<>();
                customParams.put("statusRunning", EStatusItems.running);
                customParams.put("statusApproved", EStatusItems.approved);
                customParams.put("startDay", DateUtils.truncate(e.getValue(), Calendar.DATE));
                customParams.put("endDay", DateUtils.ceiling(e.getValue(), Calendar.DATE));
                journeysDS.refresh(customParams);
            }
        });
    }

    private void addCurrentJourneyLocationCheckBoxValueChangeListener() {
        currentJourneyLocationChkbox.addValueChangeListener(e -> {
            if (currentJourneyLocationChkbox.isChecked()) {
                journeysDS.getItems().forEach(journey -> {
                    if (journey.getFlag()) {
                        addCurrentTransportLocationMarker(journey.getTransport());
                    }
                });
            } else {
                journeysDS.getItems().forEach(journey -> {
                    if (journey.getFlag()) {
                        removeMarker(transportLocationMapData, journey.getTransport());
                    }
                });
            }
            mapAssistant.fitToBoundsByAllMarkers(map);
        });

    }

    private void addCurrentTransportLocationCheckBoxValueChangeListener() {
        currentTransportLocationChkbox.addValueChangeListener(e -> {
            if (currentTransportLocationChkbox.isChecked()) {
                transportsStateDS.getItems().forEach(transportState -> {
                    if (transportState.getFlag()) {
                        boolean added = addCurrentTransportLocationMarker(transportState.getTransport());
                        if (added) {
                            removeMarker(transportStateMapData, transportState.getTransport());
                        }
                    }
                });
            } else {
                transportsStateDS.getItems().forEach(transportState -> {
                    if (transportState.getFlag()) {
                        boolean removed = removeMarker(transportLocationMapData, transportState.getTransport());
                        if (removed) {
                            addTransportStateMarker(transportState);
                        }
                    }
                });
            }
            mapAssistant.fitToBoundsByAllMarkers(map);
        });
    }

    private void addManualJourneyCheckBoxValueChangeListener() {
        showManualJourneyChkbox.addValueChangeListener(e -> {
            if (showManualJourneyChkbox.isChecked()) {
                manualJourneyList = monitoringService.getCurrentManualJourneys();
                manualJourneyList.forEach(journeysDS::includeItem);
            } else {
                manualJourneyList.forEach(journey -> {
                    journey.setFlag(false);
                    journeysDS.excludeItem(journey);
                });
            }
        });
    }

    private void addJourneyTableColumnGenerator() {
        journeyTable.addGeneratedColumn("flag", journey -> {
            CheckBox checkBox = componentsFactory.createComponent(CheckBox.class);
            checkBox.setDatasource(journeyTable.getItemDatasource(journey), "flag");
            checkBox.addValueChangeListener(e -> {
                if (checkBox.isChecked()) {
                    List<GeoPoint> geoPoints = mapAssistant.parseTrack(map, journey.getTrack());
                    if (!geoPoints.isEmpty()) {
                        Marker startMarker = createAndAddMarker(geoPoints.get(0), MapAssistant.GREEN_MARKER_URL);
                        Marker endMarker = createAndAddMarker(geoPoints.get(geoPoints.size() - 1), MapAssistant.RED_MARKER_URL);
                        startMarker.setCaption(buildMarkerCaption(journey.getStartDate(), journey.getStartAddress(), startMarker));
                        endMarker.setCaption(buildMarkerCaption(journey.getEndDate(), journey.getEndAddress(), endMarker));

                        Polyline polyline = mapAssistant.createPolyline(map, geoPoints, 5, "#4682B4");
                        journeyMapData.put(journey, Arrays.asList(startMarker, endMarker, polyline));

                        if (currentJourneyLocationChkbox.isChecked()) {
                            addCurrentTransportLocationMarker(journey.getTransport());
                        }
                    }
                } else {
                    List<Object> mapElements = journeyMapData.remove(journey);
                    clearTrack(mapElements);
                    if (currentJourneyLocationChkbox.isChecked()) {
                        removeMarker(transportLocationMapData, journey.getTransport());
                    }
                }
                mapAssistant.fitToBoundsByAllMarkers(map);
            });
            return checkBox;
        });
    }

    private void addTransportStateTableColumnGenerator() {
        transportStateTable.addGeneratedColumn("flag", transportState -> {
            CheckBox checkBox = componentsFactory.createComponent(CheckBox.class);
            checkBox.setDatasource(transportStateTable.getItemDatasource(transportState), "flag");
            checkBox.addValueChangeListener(e -> {
                boolean condition = currentTransportLocationChkbox.isChecked() && transportState.getTransport().getSensorCode() != null;

                if (checkBox.isChecked()) {
                    if (condition) {
                        addCurrentTransportLocationMarker(transportState.getTransport());
                    } else {
                        addTransportStateMarker(transportState);
                    }
                } else {
                    if (condition) {
                        removeMarker(transportLocationMapData, transportState.getTransport());
                    } else {
                        removeMarker(transportStateMapData, transportState.getTransport());
                    }
                }
                mapAssistant.fitToBoundsByAllMarkers(map);
            });
            return checkBox;
        });
    }

    private void addCargoTableColumnGenerator() {
        cargoTable.addGeneratedColumn("flag", cargo -> {
            Datasource itemDatasource = cargoTable.getItemDatasource(cargo);
            CheckBox checkBox = componentsFactory.createComponent(CheckBox.class);
            checkBox.setDatasource(itemDatasource, "flag");
            checkBox.addValueChangeListener(e -> {
                if (checkBox.isChecked()) {
                    GeoPoint geoPoint = map.createGeoPoint(cargo.getSendPoint().getLatitude(), cargo.getSendPoint().getLongitude());
                    Marker marker = createAndAddMarker(geoPoint, MapAssistant.ORANGE_MARKER_URL);
                    String caption = buildMarkerCaption(cargo.getSentDate(), cargo.getSendPoint().getAddress(), marker) /*+ //todo
                            "<hr>" + buildMarkerCaption(cargo.getDeliveryDate(), cargo.getEndAddress(), marker)*/;
                    marker.setCaption(caption);
                    cargoMapData.put(cargo, marker);
                } else {
                    Marker marker = cargoMapData.remove(cargo);
                    map.removeMarker(marker);
                }
                zoomingCargoOnMap();
            });
            return checkBox;
        });
    }

    //return true if added
    private boolean addCurrentTransportLocationMarker(Transport transport) {
        if (transport.getSensorCode() != null) {
            Date now = AppBeans.get(TimeSource.class).currentTimestamp();
            Date from = DateUtils.addHours(now, -1);

            String gpsData = gpsService.getGpsData(transport.getSensorCode(), from, now);
            if (gpsData != null) {
                JSONArray jsonArray = new JSONArray(gpsData);
                if (jsonArray.length() > 0) {
                    JSONObject lastSignal = jsonArray.getJSONObject(jsonArray.length() - 1);
                    double lat = lastSignal.getDouble("lat");
                    double lon = lastSignal.getDouble("lon");
                    long time = lastSignal.getLong("t");

                    //if marker does not exist, create it
                    Marker marker = transportLocationMapData.get(transport);
                    GeoPoint geoPoint = map.createGeoPoint(lat, lon);
                    if (marker == null) {
                        marker = createAndAddMarker(geoPoint, MapAssistant.TRUCK_MARKER_URL);
                    } else {
                        marker.setPosition(geoPoint);
                        map.addMarker(marker);
                    }
                    marker.setCaption(buildTransportMarkerCaption(transport, time));
                    transportLocationMapData.put(transport, marker);
                    return true;
                }
            }
        }
        return false;
    }

    private void addTransportStateMarker(TransportState transportState) {
        GeoPoint geoPoint = map.createGeoPoint(transportState.getLocationLatitude(), transportState.getLocationLongitude());
        Marker marker = createAndAddMarker(geoPoint, MapAssistant.BLUE_MARKER_URL);
        marker.setCaption(buildMarkerCaption(null, transportState.getLocationAddress(), marker));
        transportStateMapData.put(transportState.getTransport(), marker);
    }

    private void zoomingJourneyOnMap() {
        List<GeoPoint> geoPointsList = new ArrayList<>();
        journeysDS.getItems().forEach(item -> {
            if (item.getFlag() && !item.getJourneyComposition().isEmpty()) {
                JourneyComposition startComposition = item.getJourneyComposition().stream().min(Comparator.comparing(JourneyComposition::getLocationDate)).get();
                JourneyComposition endComposition = item.getJourneyComposition().stream().max(Comparator.comparing(JourneyComposition::getLocationDate)).get();
                geoPointsList.add(map.createGeoPoint(startComposition.getPoint().getLatitude(), startComposition.getPoint().getLongitude()));
                geoPointsList.add(map.createGeoPoint(endComposition.getPoint().getLatitude(), endComposition.getPoint().getLatitude()));
            }
        });
        mapAssistant.fitToBounds(map, geoPointsList);
    }

    private void zoomingCargoOnMap() {
        List<Cargo> checkedCargoesList = cargoesDS.getItems().stream().filter(Cargo::getFlag).collect(Collectors.toList());
        if (checkedCargoesList.size() == 1) {
            Cargo checkedCargo = checkedCargoesList.get(0);
            MapAssistant.ZoomLevel zoomLevel = mapAssistant.getZoomLevel(checkedCargo.getSendPoint().getBuilding(), checkedCargo.getSendPoint().getStreet());
            map.setZoom(mapAssistant.getZoomByZoomLevel(zoomLevel));
            map.setCenter(map.createGeoPoint(checkedCargo.getSendPoint().getLatitude(), checkedCargo.getSendPoint().getLongitude()));
        } else if (checkedCargoesList.size() > 1) {
            List<GeoPoint> geoPointsList = new ArrayList<>();
            checkedCargoesList.forEach(item -> {
                if (item.getFlag()) {
                    geoPointsList.add(map.createGeoPoint(item.getSendPoint().getLatitude(), item.getSendPoint().getLongitude()));
                }
            });
            mapAssistant.fitToBounds(map, geoPointsList);
        }
    }

    private Marker createAndAddMarker(GeoPoint geoPoint, String iconUrl) {
        MarkerImage image = mapAssistant.createMarkerImage(map, iconUrl);
        Marker marker = map.createMarker("", geoPoint, false, image);
        map.addMarker(marker);
        return marker;
    }

    private String buildMarkerCaption(Date date, String address, Marker marker) {
        String addressInfo = mapAssistant.formatPointAddressInfo(address, marker);
        String dateInfo = "";
        if (date != null) {
            dateInfo = "</br>" + messages.getMessage(getClass(), "date") + " " + datatypeFormatter.formatDateTime(date);
        }
        return addressInfo + dateInfo;
    }

    private String buildTransportMarkerCaption(Transport transport, long time) {
        return transport.getName() + " " + transport.getIdentNumber() + "</br>" +
                messages.getMessage(getClass(), "signalTime") + ": " + datatypeFormatter.formatDateTime(new Date(time * 1000));
    }

    private void clearTrack(List<Object> mapElements) {
        if (mapElements != null) {
            map.removeMarker((Marker) mapElements.get(0));
            map.removeMarker((Marker) mapElements.get(1));
            map.removePolyline((Polyline) mapElements.get(2));
        }
    }

    private boolean removeMarker(Map<Transport, Marker> markerMapData, Transport transport) {
        Marker marker = markerMapData.remove(transport);
        if (marker != null) {
            map.removeMarker(marker);
            return true;
        }
        return false;
    }

    private <T extends MonitoringEntity> void showHide(Table<T> table, CollectionDatasource<T, UUID> datasource, boolean show) {
        Set<T> set = table.getSelected();
        if (set.size() > 1) {
            set.forEach(item -> item.setFlag(show));
        } else {
            datasource.getItems().forEach(item -> {
                if (item.getFlag() != show) {
                    item.setFlag(show);
                }
            });
        }
    }

    public void onOpenSettings() {
        AbstractWindow settingsWindow = openEditor(settings, WindowManager.OpenType.DIALOG);
        settingsWindow.addCloseWithCommitListener(() -> {
            loadSettings();
            Timer timer = getTimer("timer");
            timer.setDelay(settings.getUpdatePeriod() * 60 * 1000);
        });
    }

    public void onShowAllJourney() {
        showHide(journeyTable, journeysDS, true);
    }

    public void onShowAllTransportState() {
        showHide(transportStateTable, transportsStateDS, true);
    }

    public void onShowAllCargo() {
        showHide(cargoTable, cargoesDS, true);
    }

    public void onHideJourney() {
        showHide(journeyTable, journeysDS, false);
    }

    public void onHideTransportState() {
        showHide(transportStateTable, transportsStateDS, false);
    }

    public void onHideCargo() {
        showHide(cargoTable, cargoesDS, false);
    }

    private void stopTimer() {
        Timer timer = getTimer("timer");
        if (timer != null) {
            timer.stop();
        }
    }
}
