package com.planarry.erp.web.utils;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.Metadata;
import com.planarry.erp.entity.*;
import com.planarry.erp.service.RouterService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Component(TariffCalculator.NAME)
@Scope("prototype")
public class TariffCalculator {

    public static final String NAME = "erp_TariffCalculator";

    @Inject
    private Metadata metadata;

    private RouterService routerService;

    private int errorInCalculate;

    private List<AreaData> areaDataSequense = new ArrayList<>();

    // Калькуляция без точек въезда, основана на расчете точек пересечения маршрута с зонами
    public JourneyData calculate(RouterService routerService, Cargo cargo, PolygonMap returnArea, AreaHolder areaHolder) {
        this.routerService = routerService;
        errorInCalculate = 0;
        areaDataSequense.clear();

        JourneyData journeyData = metadata.create(JourneyData.class);
        journeyData.setBaseCostValues(areaHolder);

        List<Point> deliveryPoints = new ArrayList<>();
        List<String> pointsSequence = new ArrayList<>();

        pointsSequence.add(cargo.getSendPoint().getLongitude() + "," + cargo.getSendPoint().getLatitude());
        cargo.getDeliveryPoints().forEach(item -> {
            pointsSequence.add(item.getPoint().getLongitude() + "," + item.getPoint().getLatitude());
            deliveryPoints.add(item.getPoint());
        });

        String routeJson = routerService.getRoute(pointsSequence);
        if (routeJson != null) {
            JSONArray coordinates = new JSONArray(routerService.parseRouteTrack(routeJson));

            Point prevPoint = cargo.getSendPoint();
            Area prevArea = findAreaWithPoint(areaHolder.getAreas(), prevPoint.getLatitude(), prevPoint.getLongitude());
            AreaData prevAreaData = journeyData.getActualAreaData(prevArea);
            prevAreaData.addLoadingTime(prevPoint.getServiceTime());

            Map<String, Double> routeSquare = getRouteSquare(coordinates, 0, coordinates.length() - 1);
            List<Area> overlapsAreas = findOverlapsAreas(areaHolder.getAreas(), routeSquare);
            if (overlapsAreas.isEmpty()) {
                for (CargoDeliveryPoint cargoDeliveryPoint : cargo.getDeliveryPoints()) {
                    Point nextPoint = cargoDeliveryPoint.getPoint();
                    prevAreaData.addUnloadingTime(nextPoint.getServiceTime());
                    prevAreaData.addRoadDist(getRouteDistance(routeJson));
                    prevAreaData.addRoadTime(getRouteTime(routeJson));
                }
            } else {
                pointsSequence.clear();
                pointsSequence.add(prevPoint.getLongitude() + "," + prevPoint.getLatitude());

                int startRange = 0;
                int endRange = coordinates.length() / 2;
                while (endRange > startRange + 1 || endRange < coordinates.length() - 1) {
                    Map<String, Double> subRouteSquare = getRouteSquare(coordinates, startRange, endRange);
                    if (subRouteSquare != null) {
                        List<Area> subOverlapsAreas = findOverlapsAreas(overlapsAreas, subRouteSquare);
                        JSONArray endCoords = coordinates.getJSONArray(endRange);
                        Area nextArea = findAreaWithPoint(areaHolder.getAreas(), endCoords.getDouble(1), endCoords.getDouble(0));

                        if (subOverlapsAreas.isEmpty()) {
                            startRange = endRange;
                            endRange *= 2;
                        } else if (subOverlapsAreas.size() == 1 && (nextArea == null ^ prevArea == null || prevArea != nextArea)) {
                            PolygonPoint overlapsPoint = getOverlapsPoint(subOverlapsAreas.get(0).getPolygon(), coordinates, startRange, endRange);

                            if (overlapsPoint != null) {
                                Iterator<Point> iterator = deliveryPoints.iterator();
                                while (iterator.hasNext()) {
                                    Point point = iterator.next();
                                    Area deliveryArea = findAreaWithPoint(overlapsAreas, point.getLatitude(), point.getLongitude());

                                    if ((prevArea == null && deliveryArea == null) || (prevArea != null && prevArea.equals(deliveryArea))) {
                                        areaDataSequense.add(prevAreaData);
                                        pointsSequence.add(point.getLongitude() + "," + point.getLatitude());
                                        prevAreaData.addUnloadingTime(point.getServiceTime());
                                        iterator.remove();
                                    } else {
                                        break;
                                    }
                                }

                                pointsSequence.add(overlapsPoint.getLon() + "," + overlapsPoint.getLat());
                                areaDataSequense.add(prevAreaData);
                                AreaData nextAreaData = journeyData.getActualAreaData(nextArea);

                                //check, if end of segment belongs to zero area
                                JSONArray startCoords = coordinates.getJSONArray(startRange);
                                if (nextArea == null || containPoint(nextArea.getPolygon(), startCoords.getDouble(1), startCoords.getDouble(0))) {
                                    prevAreaData.addExitNumber();
                                } else if (!containPoint(nextArea.getPolygon(), startCoords.getDouble(1), startCoords.getDouble(0))) {
                                    nextAreaData.addEntranceNumber();
                                }
                                prevArea = nextArea;
                                prevAreaData = nextAreaData;
                            }
                            startRange = endRange;
                            endRange = endRange / 2 + endRange;
                        } else {
                            endRange = (endRange + startRange) / 2;
                        }

                        if (endRange >= coordinates.length()) {
                            endRange = coordinates.length() - 1;
                        }
                    }
                }

                //add last area to sequence
                areaDataSequense.add(prevAreaData);
                Point lastPoint = cargo.getDeliveryPoints().get(cargo.getDeliveryPoints().size() - 1).getPoint();
                pointsSequence.add(lastPoint.getLongitude() + "," + lastPoint.getLatitude());

                //parsing all segments of route
                parseRouteSegment(routerService.getRoute(pointsSequence), journeyData);
            }
        } else errorInCalculate++;

        journeyData.setReturnDistance(findReturnDistance(returnArea, cargo.getDeliveryPoints().get(cargo.getDeliveryPoints().size() - 1).getPoint()));
        journeyData.setCorrect(errorInCalculate == 0);
        return journeyData;
    }

    private void parseRouteSegment(String routeJson, JourneyData journeyData){
        if (!Strings.isNullOrEmpty(routeJson)) {
            journeyData.setTrack(routerService.parseRouteTrack(routeJson));

            JSONObject jsonObject = new JSONObject(routeJson);
            if (jsonObject.has("routes")) {
                JSONArray routes = jsonObject.getJSONArray("routes");
                if (routes.length() > 0) {
                    JSONArray legs = routes.getJSONObject(0).getJSONArray("legs");
                    for (int i = 0; i < legs.length(); i++) {
                        JSONObject leg = legs.getJSONObject(i);
                        AreaData areaData = areaDataSequense.get(i);
                        areaData.addRoadDist(leg.getDouble("distance") / 1000);
                        areaData.addRoadTime(leg.getInt("duration") / 60);
                    }
                }
            }
        } else errorInCalculate++;
    }


    private double findReturnDistance(PolygonMap returnArea, Point finishPoint) {
        if (returnArea == null) {
            return 0;
        }

        //check if last point located beyond the "return area"
        if (!containPoint(returnArea, finishPoint.getLatitude(), finishPoint.getLongitude())) {
            double centerLat = (returnArea.getMinLat() + returnArea.getMaxLat()) / 2;
            double centerLon = (returnArea.getMinLon() + returnArea.getMaxLon()) / 2;

            String routeJson = routerService.getRoute(finishPoint.getLatitude(), finishPoint.getLongitude(), centerLat, centerLon);
            if (routeJson != null) {
                JSONArray coordinates = new JSONArray(routerService.parseRouteTrack(routeJson));
                PolygonPoint point = getOverlapsPoint(returnArea, coordinates, 0, coordinates.length() - 1);
                if (point != null){
                    String returnRoute = routerService.getRoute(finishPoint.getLatitude(), finishPoint.getLongitude(), point.getLat(), point.getLon());
                    Double dist = routerService.parseRouteDistance(returnRoute);
                    if (dist != null){
                        return dist;
                    } else errorInCalculate ++;
                }
            } else errorInCalculate ++;
        }
        return 0;
    }

    private List<Area> findAllAreasWhichContainPoint(List<? extends Area> areaHolders, Double lat, Double lon) {
        if (areaHolders == null) {
            return null;
        }
        List<Area> areas = areaHolders.stream().filter(
                item -> containPoint(item.getPolygon(), lat, lon)).collect(Collectors.toList()
        );
        return areas;
    }

    //Finds polygon which contain a point. Return null if point is outside a polygons
    private Area findAreaWithPoint(List<? extends Area> areaHolders, Double lat, Double lon) {
        List<Area> areas = findAllAreasWhichContainPoint(areaHolders, lat, lon);
        if (areas == null || areas.isEmpty()) {
            return null;
        } else if (areas.size() == 1) {
            return areas.get(0);
        } else {
            for (Area area : areas) {
                boolean isInnerArea = true;
                for (Area otherArea : areas) {
                    PolygonPoint areaVert = area.getPolygon().getPolygonPoint().get(0);
                    //if any one vertex located inside other polygons - that mean this area is inner
                    if (!area.equals(otherArea) && !containPoint(otherArea.getPolygon(), areaVert.getLat(), areaVert.getLon())) {
                        isInnerArea = false;
                        break;
                    }
                }

                if (isInnerArea) {
                    return area;
                }
            }
        }
        return null;
    }


    //Return true if the given point is contained inside the polygon.
    private boolean containPoint(PolygonMap polygon, Double lat, Double lon) {
        List<PolygonPoint> points = polygon.getPolygonPoint();
        int i;
        int j;
        boolean result = false;
        for (i = 0, j = points.size() - 1; i < points.size(); j = i++) {
            if ((points.get(i).getLon() > lon) != (points.get(j).getLon() > lon) && (lat < (points.get(j).getLat() - points.get(i).getLat())
                    * (lon - points.get(i).getLon()) / (points.get(j).getLon() - points.get(i).getLon()) + points.get(i).getLat())) {
                result = !result;
            }
        }

        return result;
    }


    //return route distance in kilometers
    private double getRouteDistance(String resultJson) {
        Double routeDistance = routerService.parseRouteDistance(resultJson);
        if (routeDistance == null) {
            errorInCalculate++;
            return 0;
        }
        return routeDistance;
    }

    //return route time in minutes
    private int getRouteTime(String resultJson) {
        Integer routeTime = routerService.parseRouteTime(resultJson);
        if (routeTime == null) {
            errorInCalculate++;
            return 0;
        }
        return routeTime;
    }

    private PolygonPoint getOverlapsPoint(PolygonMap polygon, JSONArray coordinates, int start, int end) {
        int startRange = start;
        int endRange = (end + start) / 2;

        PolygonPoint point = null;
        while ((endRange > startRange + 1 || endRange < end) && point == null) {
            Map<String, Double> segmentRouteSquare = getRouteSquare(coordinates, startRange, endRange);
            long overlapsNumber = findOverlapsNumber(polygon, segmentRouteSquare);

            if (overlapsNumber == 0) {
                startRange = endRange;
                endRange *= 2;
            } else if (overlapsNumber < 7) { //todo если случается бесконечный цикл - увеличить параметр в условии
                for (int i = startRange; i < endRange && point == null; i++) {
                    JSONArray startCoords = coordinates.getJSONArray(i);
                    JSONArray endCoords = coordinates.getJSONArray(i + 1);

                    double X11 = startCoords.getDouble(0);
                    double Y11 = startCoords.getDouble(1);
                    double X12 = endCoords.getDouble(0);
                    double Y12 = endCoords.getDouble(1);

                    for (PolygonSegment segment : polygon.getSegments()) {
                        point = findOverlapsPoint(X11, Y11, X12, Y12, segment.getStart().getLon(), segment.getStart().getLat(),
                                segment.getEnd().getLon(), segment.getEnd().getLat());
                        if (point != null) {
                            break;
                        }
                    }
                }
                startRange = endRange;
                endRange = endRange / 2 + endRange;
            } else {
                endRange = (endRange + startRange) / 2;
            }

            if (endRange > end) {
                endRange = end;
            }
        }
        return point;
    }

    private PolygonPoint findOverlapsPoint(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        double k1 = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));
        double k2 = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));

        if (k1 >= 0 && k1 <= 1 && k2 >= 0 && k2 <= 1) {
            double x = x1 + k1 * (x2 - x1);
            double y = y1 + k1 * (y2 - y1);


            PolygonPoint point = new PolygonPoint();
            point.setLat(y);
            point.setLon(x);
            return point;
        }
        return null;
    }

    private List<Area> findOverlapsAreas(List<? extends Area> areas, Map<String, Double> routeSquare) {
        return areas.stream().filter(area -> {
            PolygonMap polygon = area.getPolygon();
            boolean minLatInside = polygon.getMinLat() >= routeSquare.get("minLat") && polygon.getMinLat() <= routeSquare.get("maxLat");
            boolean maxLatInside = polygon.getMaxLat() >= routeSquare.get("minLat") && polygon.getMaxLat() <= routeSquare.get("maxLat");
            boolean minLonInside = polygon.getMinLon() >= routeSquare.get("minLon") && polygon.getMinLon() <= routeSquare.get("maxLon");
            boolean maxLonInside = polygon.getMaxLon() >= routeSquare.get("minLon") && polygon.getMaxLon() <= routeSquare.get("maxLon");
            boolean minLatSmallest = polygon.getMinLat() <= routeSquare.get("minLat");
            boolean maxLatBiggest = polygon.getMaxLat() >= routeSquare.get("maxLat");
            boolean minLonSmallest = polygon.getMinLon() <= routeSquare.get("minLon");
            boolean maxLonBiggest = polygon.getMaxLon() >= routeSquare.get("maxLon");

            return  ((minLatInside || maxLatInside) && (minLonInside || maxLonInside)
                    || (minLatSmallest && maxLatBiggest && (minLonInside || maxLonInside))
                    || (minLonSmallest && maxLonBiggest && (minLatInside || maxLatInside)));
        }).collect(Collectors.toList());
    }

    private long findOverlapsNumber(PolygonMap polygon, Map<String, Double> routeSquare) {
        return polygon.getSegments().stream().filter(segment -> {
            boolean minLatInside = segment.getMinLat() >= routeSquare.get("minLat") && segment.getMinLat() <= routeSquare.get("maxLat");
            boolean maxLatInside = segment.getMaxLat() >= routeSquare.get("minLat") && segment.getMaxLat() <= routeSquare.get("maxLat");
            boolean minLonInside = segment.getMinLon() >= routeSquare.get("minLon") && segment.getMinLon() <= routeSquare.get("maxLon");
            boolean maxLonInside = segment.getMaxLon() >= routeSquare.get("minLon") && segment.getMaxLon() <= routeSquare.get("maxLon");

            boolean minLatSmallest = segment.getMinLat() <= routeSquare.get("minLat");
            boolean maxLatBiggest = segment.getMaxLat() >= routeSquare.get("maxLat");
            boolean minLonSmallest = segment.getMinLon() <= routeSquare.get("minLon");
            boolean maxLonBiggest = segment.getMaxLon() >= routeSquare.get("maxLon");

            return  ((minLatInside || maxLatInside) && (minLonInside || maxLonInside)
                    || (minLatSmallest && maxLatBiggest && (minLonInside || maxLonInside))
                    || (minLonSmallest && maxLonBiggest && (minLatInside || maxLatInside))
                    || (minLatSmallest && maxLatBiggest && minLonSmallest && maxLonBiggest));
        }).count();
    }

    private Map<String, Double> getRouteSquare(JSONArray coordinates, int startRange, int endRange) {
        double minLat = Double.MAX_VALUE;
        double maxLat = Double.MIN_VALUE;
        double minLon = Double.MAX_VALUE;
        double maxLon = Double.MIN_VALUE;

        for (int i = startRange; i <= endRange; i++) {
            double lat = coordinates.getJSONArray(i).getDouble(1);
            double lon = coordinates.getJSONArray(i).getDouble(0);
            minLat = lat < minLat ? lat : minLat;
            maxLat = lat > maxLat ? lat : maxLat;
            minLon = lon < minLon ? lon : minLon;
            maxLon = lon > maxLon ? lon : maxLon;
        }

        Map<String, Double> resultMap = new HashMap<>();
        resultMap.put("minLat", minLat);
        resultMap.put("maxLat", maxLat);
        resultMap.put("minLon", minLon);
        resultMap.put("maxLon", maxLon);
        return resultMap;
    }
}
