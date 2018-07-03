
package com.planarry.erp.web.polygonmap;

import com.haulmont.charts.gui.components.map.MapViewer;
import com.haulmont.charts.gui.map.model.GeoPoint;
import com.haulmont.charts.gui.map.model.InfoWindow;
import com.haulmont.charts.gui.map.model.Marker;
import com.haulmont.charts.gui.map.model.Polygon;
import com.haulmont.charts.gui.map.model.drawing.*;
import com.haulmont.charts.gui.map.model.listeners.click.MapClickListener;
import com.haulmont.charts.gui.map.model.listeners.click.MarkerClickListener;
import com.haulmont.charts.gui.map.model.listeners.doubleclick.MarkerDoubleClickListener;
import com.haulmont.charts.gui.map.model.listeners.drag.MarkerDragListener;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.VBoxLayout;
import com.haulmont.cuba.gui.components.ValidationErrors;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.planarry.erp.entity.EntryPoint;
import com.planarry.erp.entity.PolygonMap;
import com.planarry.erp.entity.PolygonPoint;
import com.planarry.erp.entity.PolygonSegment;
import com.planarry.erp.web.utils.AddressPickerUtils;
import com.planarry.erp.web.utils.ControllerAssistant;
import com.planarry.erp.web.utils.MapAssistant;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

public class PolygonMapEdit extends AbstractEditor<PolygonMap> {

    private final static String POLYGON_COLOR = "#993366";
    private final static Double POLYGON_OPACITY = 0.7;

    @Named("segmentsDs")
    private CollectionDatasource<PolygonSegment, UUID> segmentsDs;

    @Named("pointsDs")
    private CollectionDatasource<PolygonPoint, UUID> pointsDs;

    @Named("entryPointDS")
    private CollectionDatasource<EntryPoint, UUID> entryPointDS;

    @Named("map")
    private MapViewer map;

    @Named("removeBtn")
    private Button removeBtn;

    @Named("editBtn")
    private Button editBtn;

    @Named("entryPoint")
    private Button entryPoint;

    @Named("legend")
    private VBoxLayout legend;

    @Named(Metadata.NAME)
    private Metadata metadata;

    @Named(MapAssistant.NAME)
    private MapAssistant mapAssistant;

    @Named(AddressPickerUtils.NAME)
    private AddressPickerUtils addressPickerUtils;

    @Named(ControllerAssistant.NAME)
    private ControllerAssistant controllerAssistant;

    private Polygon currentPolygon;

    private List<EntryPoint> entryPoints;

    private InfoWindow infoWindow;

    private List<InfoWindow> infoWindowList;

    private MarkerDoubleClickListener markerDoubleClickListener;

    private MarkerDragListener markerDragListener;

    private MarkerClickListener markerClickListener;

    private MapClickListener mapClickListener;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        initMap();
    }

    @Override
    protected void postInit() {
        super.postInit();
        if (currentPolygon == null) {
            initCurrentPolygon();
            initEntryPoints();
        }
    }

    @Override
    protected void postValidate(ValidationErrors errors) {
        super.postValidate(errors);
        if (currentPolygon == null) {
            errors.add(messages.getMessage(getClass(), "validation.polygonNotExist"));
        }
    }

    @Override
    protected boolean preCommit() {
        if (!checkPointsAfterEdit()) {
            addPolygonPoints();
            addPolygonSegments();
            addEntryPointToEntryPointDs();
        }

        if (pointsDs.isModified()) {
            definePolygonSquare();
        }
        return super.preCommit();
    }

    private void definePolygonSquare() {
        double minLat = Double.MAX_VALUE;
        double maxLat = Double.MIN_VALUE;
        double minLon = Double.MAX_VALUE;
        double maxLon = Double.MIN_VALUE;

        for (PolygonPoint point : pointsDs.getItems()) {
            minLat = point.getLat() < minLat ? point.getLat() : minLat;
            maxLat = point.getLat() > maxLat ? point.getLat() : maxLat;
            minLon = point.getLon() < minLon ? point.getLon() : minLon;
            maxLon = point.getLon() > maxLon ? point.getLon() : maxLon;
        }
        getItem().setMinLat(minLat);
        getItem().setMinLon(minLon);
        getItem().setMaxLat(maxLat);
        getItem().setMaxLon(maxLon);
    }

    /**
     * For save correct EntryPoints before commit.
     */
    private void addEntryPointToEntryPointDs() {
        ArrayList<EntryPoint> entryPDs = new ArrayList<>(entryPointDS.getItems());
        if (entryPoints != null) {
            entryPDs.forEach(point -> {
                if (entryPoints.stream().noneMatch(p -> p.getLon().equals(point.getLon()) && p.getLat().equals(point.getLat()))) {
                    entryPointDS.removeItem(point);
//                }
                }
            });
            entryPoints.forEach(point -> {
                if (entryPointDS.getItems().stream().noneMatch(pointDs -> pointDs.getLon().equals(point.getLon()) && pointDs.getLat().equals(point.getLat()))) {
                    entryPointDS.addItem(point);
                }
            });
        } else {
            entryPDs.forEach(point -> entryPointDS.removeItem(point));
        }
    }

    private void addPolygonPoints() {
        List<PolygonPoint> points = new ArrayList<>(pointsDs.getItems());
        points.forEach(pointsDs::removeItem);

        for (int i = 0; i < currentPolygon.getCoordinates().size(); i++) {
            PolygonPoint polygonPoint = metadata.create(PolygonPoint.class);
            polygonPoint.setPolygon(getItem());
            polygonPoint.setLat(currentPolygon.getCoordinates().get(i).getLatitude());
            polygonPoint.setLon(currentPolygon.getCoordinates().get(i).getLongitude());
            polygonPoint.setOrder(i + 1);
            pointsDs.addItem(polygonPoint);
        }
    }

    private void addPolygonSegments() {
        List<PolygonSegment> segments = new ArrayList<>(segmentsDs.getItems());
        segments.forEach(segmentsDs::removeItem);

        List<PolygonPoint> points = new ArrayList<>(pointsDs.getItems());
        addSegment(points.get(points.size() - 1), points.get(0));
        for (int i = 0; i < points.size() - 1; i++){
            addSegment(points.get(i), points.get(i + 1));
        }
    }

    private void addSegment(PolygonPoint start, PolygonPoint end) {
        PolygonSegment segment = metadata.create(PolygonSegment.class);
        segment.setPolygon(getItem());
        segment.setStart(start);
        segment.setEnd(end);
        segment.calculateRectangle();
        segmentsDs.addItem(segment);
    }

    private void initMap() {
        map.setCenter(map.createGeoPoint(50.45466, 30.5238000));
        map.setZoom(10);
        map.setMaxZoom(19);
        addPolygonEditListener();
    }

    private void addPolygonEditListener() {
        map.addPolygonEditListener(polygonEditEvent -> {
            currentPolygon = polygonEditEvent.getPolygon();
        });
    }

    public void onStartDrawing() {
        onRemove();
        DrawingOptions drawingOptions = new DrawingOptions();
        PolygonOptions polygonOptions = new PolygonOptions(false, true, POLYGON_COLOR, POLYGON_OPACITY);
        polygonOptions.setStrokeWeight(1);
        CircleOptions circleOptions = new CircleOptions();
        ControlOptions controlOptions = new ControlOptions(Position.TOP_LEFT, Collections.singletonList(OverlayType.POLYGON));
        drawingOptions.setPolygonOptions(polygonOptions);
        drawingOptions.setCircleOptions(circleOptions);
        drawingOptions.setEnableDrawingControl(true);
        drawingOptions.setDrawingControlOptions(controlOptions);
        map.setDrawingOptions(drawingOptions);
        map.addPolygonCompleteListener(event -> {
            currentPolygon = event.getPolygon();
            currentPolygon.setEditable(false);
            map.setDrawingOptions(null);
            setButtonsEnabled(true);
        });
        defineEditBtnCaption();

    }

    private void initCurrentPolygon() {
        currentPolygon = buildPolygon();
        if (currentPolygon != null) {
            map.addPolygonOverlay(currentPolygon);
            setButtonsEnabled(true);
            mapAssistant.fitToBounds(map, currentPolygon.getCoordinates());
        }
    }

    private Polygon buildPolygon() {
        legend.setVisible(false);
        List<GeoPoint> coordinates = new ArrayList<>();
        pointsDs.getItems().stream().sorted(Comparator.comparing(PolygonPoint::getOrder)).forEach(point -> {
            GeoPoint geoPoint = map.createGeoPoint(point.getLat(), point.getLon());
            coordinates.add(geoPoint);
        });
        if (!coordinates.isEmpty()) {
            return map.createPolygon(coordinates, POLYGON_COLOR, POLYGON_OPACITY, "#2CA860", 0, 1);
        }
        return null;
    }

    /**
     * Init entryPoints list from entryPointDS and create markers by this points.
     */
    private void initEntryPoints() {
        entryPoints = new ArrayList<>(entryPointDS.getItems());
        if (!entryPoints.isEmpty()) {
            entryPoints.forEach(entryPoint -> {
                Marker marker = map.createMarker(entryPoint.getName(), map.createGeoPoint(entryPoint.getLat(), entryPoint.getLon()), true);
                marker.setClickable(true);
                marker.setDraggable(false);
                map.addMarker(marker);
            });
        }
    }

    public void onEdit() {
        stopEntryPointListeners();
        legend.setVisible(false);
        if (currentPolygon != null) {
            if (!checkPointsAfterEdit()) {
                currentPolygon.setEditable(!currentPolygon.isEditable());
                defineEditBtnCaption();
                map.setZoom(map.getZoom()); //оставить костыль!
            }
        }
        addMarkerRemoveListener();
    }

    /**
     * For remove Polygon, markers and EntryPoints.
     * Hides legend and buttons set Enabled = false.
     */
    public void onRemove() {
        legend.setVisible(false);
        stopEntryPointListeners();
        if (currentPolygon != null) {
            defineEditBtnCaption();
            map.removePolygonOverlay(currentPolygon);
            ArrayList<Marker> markers = new ArrayList<>(map.getMarkers());
            markers.forEach(marker -> map.removeMarker(marker));
            entryPoints.clear();
            currentPolygon = null;
            setButtonsEnabled(false);
        }
    }

    /**
     * Change caption button "editBtn".
     */
    private void defineEditBtnCaption() {
        String caption = currentPolygon != null && currentPolygon.isEditable() ? "finishEdit" : "edit";
        editBtn.setCaption(messages.getMessage(getClass(), caption));
    }

    /**
     * @param enabled receives for change buttons param Enabled.
     */
    private void setButtonsEnabled(boolean enabled) {
        removeBtn.setEnabled(enabled);
        editBtn.setEnabled(enabled);
        entryPoint.setEnabled(enabled);
    }

    /**
     * For create entry points. Calls when clicks button "entryPoint"
     * And calls listeners for create markers.
     * Show legend for use.
     */
    public void createEntryPoint() {
        checkPointsAfterEdit();
        legend.setVisible(true);
        if (currentPolygon != null) {
            currentPolygon.setEditable(false);
            defineEditBtnCaption();
            map.setZoom(map.getZoom()); //оставить костыль!
            addMarkerCreateListener();
            addMarkerClickListener();
            addMarkerDragListener();
        }
        addMarkerRemoveListener();
    }

    /**
     * Stop all listener for create entry points.
     */
    private void stopEntryPointListeners() {
        map.removeMarkerDoubleClickListener(markerDoubleClickListener);
        map.removeMarkerClickListener(markerClickListener);
        map.removeMapClickListener(mapClickListener);
        map.removeMarkerDragListener(markerDragListener);
        map.getMarkers().forEach(marker -> marker.setDraggable(false));
    }

    /**
     * Init on Map Click listener for add new marker and EntryPoint
     * and add this listener on map.
     * <p>
     * If point don`t have address will write "addressByCoordinates" caption with lat and lon.
     */
    private void addMarkerCreateListener() {
        mapClickListener = event -> {
            Double lat = event.getPosition().getLatitude();
            Double lon = event.getPosition().getLongitude();
            if (checkDistanceFromPointToPolygon(lat, lon)) {
                String addressByCoordinates = getNamePoint(lat, lon);
                Marker marker = map.createMarker(addressByCoordinates, map.createGeoPoint(lat, lon), true);
                marker.setCaption(addressByCoordinates);
                marker.setClickable(true);
                map.addMarker(marker);
                EntryPoint entryPoint = metadata.create(EntryPoint.class);
                entryPoint.setName(addressByCoordinates);
                entryPoint.setLat(lat);
                entryPoint.setLon(lon);
                entryPoint.setPolygonMap(getItem());
                entryPoints.add(entryPoint);
            } else {
                showNotification(messages.getMessage(getClass(), "message.notBeforePolygon"), NotificationType.WARNING);
            }
        };
        map.getMarkers().forEach(marker -> marker.setDraggable(true));
        map.addMapClickListener(mapClickListener);
    }

    /**
     * Receive name for point.
     */
    private String getNamePoint(Double lat, Double lon) {
        String addressByCoordinates = addressPickerUtils.findAddressByCoordinates(lat, lon, MapAssistant.ZoomLevel.STREET_LEVEL, getFrame());
        if (addressByCoordinates == null) {
            String message = messages.getMessage(getClass(), "message.entry_point");
            addressByCoordinates = String.format(message + "%.2f, %.2f", lat, lon);
        }
        return addressByCoordinates;
    }

    /**
     * Check all points of Polygon after edit and show notification for correct points.
     */
    private boolean checkPointsAfterEdit() {
        boolean checkRes = false;
        if (infoWindowList != null) {
            infoWindowList.forEach(info -> map.closeInfoWindow(info));
        }
        ArrayList<Marker> entryPointArrayList = new ArrayList<>(map.getMarkers());
        infoWindowList = new ArrayList<>();
        for (int i = 0; i < entryPointArrayList.size(); i++) {
            Marker marker = entryPointArrayList.get(i);
            if (!checkDistanceFromPointToPolygon(marker.getPosition().getLatitude(), marker.getPosition().getLongitude())) {
                InfoWindow wrongPoint = map.createInfoWindow("Wrong point", marker);
                infoWindowList.add(wrongPoint);
                checkRes = true;
            }
        }
        if (!infoWindowList.isEmpty()) {
            infoWindowList.forEach(info -> map.openInfoWindow(info));
            showNotification(messages.getMessage(getClass(), "message.wrongPoints"), NotificationType.WARNING);

        }
        return checkRes;
    }

    /**
     * Check distance between EntryPoint and all sides from currentPolygon(between points).
     *
     * @return true if the distance is less than 1km.
     */
    private boolean checkDistanceFromPointToPolygon(Double latPoint, Double lonPoint) {
        for (int i = 0; i < currentPolygon.getCoordinates().size(); i++) {
            GeoPoint geoPoint1 = currentPolygon.getCoordinates().get(i);
            GeoPoint geoPoint2;
            if (i == currentPolygon.getCoordinates().size() - 1) {
                geoPoint2 = currentPolygon.getCoordinates().get(0);
            } else {
                geoPoint2 = currentPolygon.getCoordinates().get(i + 1);
            }
            Double res = controllerAssistant.calcDistanceFromPointToSegment(geoPoint1.getLatitude(), geoPoint1.getLongitude(), geoPoint2.getLatitude(), geoPoint2.getLongitude(),
                    latPoint, lonPoint);
            if (res <= 1 && res != 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Init Marker Click listener for show info window
     * and add this listener on marker.
     */
    private void addMarkerClickListener() {
        markerClickListener = event -> {
            Marker marker = event.getMarker();
            if (infoWindow != null) {
                map.closeInfoWindow(infoWindow);
            }
            infoWindow = map.createInfoWindow(marker.getCaption(), marker);
            map.openInfoWindow(infoWindow);
        };
        map.addMarkerClickListener(markerClickListener);
    }

    /**
     * Init Marker Double Click listener for remove EntryPoint and marker
     * and add this listener on marker.
     */
    private void addMarkerRemoveListener() {
        markerDoubleClickListener = event -> {
            Marker marker = event.getMarker();
            ArrayList<EntryPoint> entryP = new ArrayList<>(entryPoints);
            entryP.forEach(entryPoint -> {
                if (entryPoint.getLat().equals(marker.getPosition().getLatitude()) && entryPoint.getLon().equals(marker.getPosition().getLongitude())) {
                    entryPoints.remove(entryPoint);
                }
            });
            map.removeMarker(marker);
            map.setZoom(map.getZoom()); //оставить костыль!
        };
        map.addMarkerDoubleClickListener(markerDoubleClickListener);
    }

    /**
     * Init Marker Drag listener for save all relocates of points.
     */
    private void addMarkerDragListener() {
        markerDragListener = event -> {
            Marker newMarker = event.getMarker();
            Double lat = newMarker.getPosition().getLatitude();
            Double lon = newMarker.getPosition().getLongitude();
            if (!checkPointsAfterEdit()) {
                newMarker.setCaption(getNamePoint(lat, lon));
                GeoPoint oldPosition = event.getOldPosition();
                entryPoints.forEach(point -> {
                    if (point.getLat().equals(oldPosition.getLatitude()) && point.getLon().equals(oldPosition.getLongitude())) {
                        point.setLat(lat);
                        point.setLon(lon);
                        point.setName(newMarker.getCaption());
                    }
                });
            }
            map.setZoom(map.getZoom()); //оставить костыль!
        };
        map.addMarkerDragListener(markerDragListener);
    }
}