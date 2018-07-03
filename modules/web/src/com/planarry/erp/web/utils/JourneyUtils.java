package com.planarry.erp.web.utils;

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.planarry.erp.entity.*;
import com.planarry.erp.service.JourneyService;
import com.planarry.erp.service.MathService;
import org.apache.commons.lang.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Component(JourneyUtils.NAME)
public class JourneyUtils {
    public static final String NAME = "erp_JourneyUtils";

    @Inject
    private Metadata metadata;

    @Inject
    private DataManager dataManager;

    @Inject
    private JourneyService journeyService;

    @Inject
    private MathService mathService;


    public Map<String, Object> calculateFullJourney(Journey journey, CollectionDatasource<Delivery, UUID> deliveriesDs,
                                                    CollectionDatasource<JourneyComposition, UUID> journeyCompositionsDs,
                                                    List<Cargo> cargoList, Date maxDate) {

        Map<String, Object> resultsMap = null;
        TransportState currentTransportLocation = journeyService.getTransportStateBeforeDateByTransport(journey.getTransport(),
                ETransportStateItems.free, maxDate);

        if (currentTransportLocation != null) {
            List<Point> pointList = getAllPointsFromCargoes(cargoList);
            String additionalData = defineDepotsSequence(journey.getJourneyNumber(), cargoList, currentTransportLocation);
            if (additionalData != null) {
                String taskId = getMathTaskId(journey.getJourneyNumber());
                String json = buildJsonRequest(taskId, cargoList, createDepotPoint(currentTransportLocation), currentTransportLocation.getStateDate(), true, additionalData);
                String result = mathService.calculateJourney(json, taskId);

                JSONArray routesJsonArr = parseCommonPartOfMathResult(result);
                if (routesJsonArr != null) {
                    JSONObject solution = new JSONObject(result).getJSONArray("solutions").getJSONObject(0);
                    List<String> pointsSequence = new ArrayList<>();
                    double totalDist = solution.getInt("totalDistance") / 1000;
                    double totalTime = solution.getLong("totalDuration") / 60;
                    Date startDate = null;
                    Date finishDate = null;
                    Point startPoint = null;
                    Point finishPoint = null;

                    resultsMap = new HashMap<>();

                    for (int j = 0; j < routesJsonArr.length(); j++) {
                        JSONArray deliveriesJsonArr = routesJsonArr.getJSONObject(j).getJSONArray("deliveries");
                        for (int k = 1; k < deliveriesJsonArr.length(); k++) { // 0 - transport location point
                            JSONObject deliveryJsonObj = deliveriesJsonArr.getJSONObject(k);

                            Date arrival = new Date(deliveryJsonObj.getLong("arrival") * 1000);
                            double distance = deliveryJsonObj.getDouble("distance") / 1000;
                            double duration = deliveryJsonObj.getInt("duration") / 60;
                            double downTime = deliveryJsonObj.getInt("downtime") / 60;
                            int pointId = deliveryJsonObj.getInt("pointId");
                            double serviceTime = deliveryJsonObj.getInt("servicetime") / 60;

                            //find point in list
                            Point point = pointList.stream().filter(item -> item.getCode().equals(pointId)).findFirst().get();
                            pointsSequence.add(point.getLatitude() + "," + point.getLongitude());

                            // if first
                            if (k == 1) {
                                totalDist = totalDist - distance;
                                totalTime = totalTime - duration + serviceTime;
                                startDate = arrival;
                                startPoint = point;
                                downTime = 0;
                                distance = 0;
                                duration = 0;
                            }
                            // if last
                            else if (k == deliveriesJsonArr.length() - 1) {
                                finishPoint = point;
                                finishDate = arrival;
                            } else {
                                totalTime += serviceTime;
                            }

                            updateOrCreateJourneyComposition(journey, journeyCompositionsDs, point, arrival);
                            updateOrCreateDeliveryComposition(deliveriesDs, point, arrival, (int) serviceTime, (int) downTime, distance, (int) duration);
                        }

                        //updateJourney(journey, startPoint, finishPoint, totalDist, (int) totalTime, startDate, finishDate, getTotalTrack(pointsSequence));
                        updateDeliveries(deliveriesDs.getItems());

                        resultsMap.put("startDate", startDate);
                        resultsMap.put("finishDate", finishDate);
                        resultsMap.put("startPoint", startPoint);
                        resultsMap.put("finishPoint", finishPoint);
                    }
                }
            }
        }
        return resultsMap;
    }

    //calculation of route when cargo was selected
    public Point calculateDefaultRoute(Journey journey, Cargo cargo) {
        Date startDate = DateUtils.addDays(cargo.getSentDate(), cargo.getSentDateDelta());
        Point startPoint = null;
        Point finishPoint = null;

        String taskId = getMathTaskId(journey.getJourneyNumber());
        String json = buildJsonRequest(taskId, Collections.singletonList(cargo), cargo.getSendPoint(), startDate,
                true, "");

        String result = mathService.calculateJourney(json, taskId);

        List<Point> pointList = getAllPointsFromCargoes(Collections.singletonList(cargo));

        JSONArray routesJsonArr = parseCommonPartOfMathResult(result);
        if (routesJsonArr != null) {
            JSONObject solution = new JSONObject(result).getJSONArray("solutions").getJSONObject(0);
            List<String> pointsSequence = new ArrayList<>();

            double totalDist = solution.getDouble("totalDistance") / 1000;
            int totalTime = solution.getInt("totalDuration") / 60;
            Date finishDate = DateUtils.addMinutes(startDate, totalTime);

            for (int j = 0; j < routesJsonArr.length(); j++) {
                JSONArray deliveriesJsonArr = routesJsonArr.getJSONObject(j).getJSONArray("deliveries");
                for (int k = 0; k < deliveriesJsonArr.length(); k++) {
                    JSONObject deliveryJsonObj = deliveriesJsonArr.getJSONObject(k);
                    totalTime = totalTime + deliveryJsonObj.getInt("servicetime") / 60;
                    int pointId = deliveryJsonObj.getInt("pointId");

                    Point point = pointList.stream().filter(item -> item.getCode().equals(pointId)).findFirst().get();
                    pointsSequence.add(point.getLatitude() + "," + point.getLongitude());

                    if (k == 0) {
                        startPoint = point;
                    } else if (k == deliveriesJsonArr.length() - 1) {
                        finishPoint = point;
                    }
                }
            }
            //updateJourney(journey, startPoint, finishPoint, totalDist, totalTime, startDate, finishDate, getTotalTrack(pointsSequence));
        }
        return finishPoint;
    }

    public Map<String, Long> calculateNewJourney(Journey journey, Cargo cargo, TransportState transportState) {
        cargo.getSendPoint().setPositionInRoute(1);
        String taskId = getMathTaskId(journey.getJourneyNumber());
        String json = buildJsonRequest(taskId, Collections.singletonList(cargo), createDepotPoint(transportState),
                transportState.getStateDate(), true, "");

        String result = mathService.calculateJourney(json, taskId);

        Map<String, Long> map = null;
        JSONArray routesJsonArr = parseCommonPartOfMathResult(result);
        if (routesJsonArr != null) {
            map = new HashMap<>();
            for (int j = 0; j < routesJsonArr.length(); j++) {
                JSONArray deliveriesJsonArr = routesJsonArr.getJSONObject(j).getJSONArray("deliveries");
                for (int k = 1; k < deliveriesJsonArr.length(); k++) { // 0 - transport location point
                    JSONObject point = deliveriesJsonArr.getJSONObject(k);
                    Long arrival = point.getLong("arrival");
                    if (k == 1) {
                        map.put("start", arrival);
                    } else if (k == deliveriesJsonArr.length() - 1) {
                        map.put("end", arrival);
                        map.put("finishPointId", point.getLong("pointId"));
                    }
                }
            }
        }
        return map;
    }


    //calculation of depots sequence  and save their order. Return "additional data" to adding it to second calculation.
    public String calculateDepotsSequence(String journeyNumber, List<Cargo> cargoList, TransportState transportState) {
        //reset points position
        cargoList.forEach(item -> item.getSendPoint().setPositionInRoute(0));

        String taskId = getMathTaskId(journeyNumber);
        String json = buildJsonRequest(taskId, cargoList, createDepotPoint(transportState),
                transportState.getStateDate(), false, "");

        String result = mathService.calculateJourney(json, taskId);

        JSONArray routesJsonArr = parseCommonPartOfMathResult(result);
        if (routesJsonArr != null) {
            for (int j = 0; j < routesJsonArr.length(); j++) {
                JSONArray deliveriesJsonArr = routesJsonArr.getJSONObject(j).getJSONArray("deliveries");
                for (int k = 1; k < deliveriesJsonArr.length(); k++) { // 0 - transport location point
                    JSONObject delivery = deliveriesJsonArr.getJSONObject(k);
                    final int pos = k;

                    int pointId = delivery.getInt("pointId");
                    cargoList.stream().filter(item -> item.getSendPoint().getCode().equals(pointId)).forEach(
                            item -> item.getSendPoint().setPositionInRoute(pos)
                    );
                }
            }
            JSONObject solution = new JSONObject(result).getJSONArray("solutions").getJSONObject(0);
            return solution.getJSONObject("additional_data").toString();
        }
        return null;
    }

    public JSONArray parseCommonPartOfMathResult(String result) {
        if (result != null) {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.has("solutions") && !jsonObject.isNull("solutions")) {
                JSONArray solutionsJsonArr = jsonObject.getJSONArray("solutions");
                for (int i = 0; i < solutionsJsonArr.length(); i++) {
                    JSONObject solution = solutionsJsonArr.getJSONObject(i);
                    JSONArray unhandledTasksJsonArr = solution.getJSONArray("unhandledJobs");
                    if (unhandledTasksJsonArr.length() == 0) {
                        return solution.getJSONArray("routes");
                    }
                }
            }
        }
        return null;
    }

    public Point createDepotPoint(TransportState transportState) {
        Point depot = metadata.create(Point.class);
        depot.setCode(Integer.MAX_VALUE - 1);
        depot.setLatitude(transportState.getLocationLatitude());
        depot.setLongitude(transportState.getLocationLongitude());
        return depot;
    }

    private List<Point> getAllPointsFromCargoes(List<Cargo> cargoList) {
        List<Point> pointList = new ArrayList<>();
        cargoList.forEach(cargo -> {
            pointList.add(cargo.getSendPoint());
            cargo.getDeliveryPoints().forEach(cargoDeliveryPoint -> pointList.add(cargoDeliveryPoint.getPoint()));
        });
        return pointList;
    }

    private String defineDepotsSequence(String journeyNumber, List<Cargo> cargoList, TransportState transportLocation) {
        if (cargoList.size() > 1) { //multi-cargo
            return calculateDepotsSequence(journeyNumber, cargoList, transportLocation);
        } else { //single cargo
            cargoList.get(0).getSendPoint().setPositionInRoute(1);
            return "";
        }
    }

    private void updateJourney(Journey journey, Point startPoint, Point finishPoint, double totalDist, int totalTime, Date startDate, Date finishDate, String track) {
        journey.setTransportationDistance(totalDist);
        journey.setTransportationTime(totalTime);
        journey.setStartDate(startDate);
        journey.setEndDate(finishDate);
        journey.setStartAddress(startPoint.getAddress());
        journey.setEndAddress(finishPoint.getAddress());
        journey.setTrack(track);
    }

    private void updateTransportState(TransportState tState, Date date, ETransportStateItems state, String address, Double lat, Double lng) {
        tState.setStateDate(date);
        tState.setState(state);
        tState.setLocationAddress(address);
        tState.setLocationLatitude(lat);
        tState.setLocationLongitude(lng);
    }

    public void updateOldTransportLocations(Journey journey, Date startDate, Date finishDate, Point startPoint, Point finishPoint) {
        List<TransportState> oldLocations = journeyService.getTransportStatesByJourney(journey);
        //if it is old journey - update old transport location
        if (oldLocations.size() == 2) {
            CommitContext commitContext = new CommitContext();
            updateTransportState(oldLocations.get(0), startDate, ETransportStateItems.engaged, startPoint.getAddress(),
                    startPoint.getLatitude(), startPoint.getLongitude());

            updateTransportState(oldLocations.get(1), finishDate, ETransportStateItems.free, finishPoint.getAddress(),
                    finishPoint.getLatitude(), finishPoint.getLongitude());

            commitContext.addInstanceToCommit(oldLocations.get(0));
            commitContext.addInstanceToCommit(oldLocations.get(1));
            dataManager.commit(commitContext);
        }
    }

    private void updateDeliveries(Collection<Delivery> deliveries) {
        deliveries.forEach(item -> {
            Double distance = 0.;
            Integer time = 0;
            List<DeliveryComposition> sortedCompositions = item.getDeliveryCompositions().stream().sorted(
                    Comparator.comparing(DeliveryComposition::getDeliveryDate)).collect(Collectors.toList()
            );

            for (int i = 0; i < sortedCompositions.size(); i++) {
                DeliveryComposition composition = sortedCompositions.get(i);

                distance += composition.getDistance();
                time = time + composition.getDuration() + composition.getServiceTime() + composition.getDownTime();
                if (i == 0) {
                    item.setStartDate(composition.getDeliveryDate());
                    item.setStartAddress(composition.getPoint().getAddress());
                } else if (i == item.getDeliveryCompositions().size() - 1) {
                    item.setEndDate(composition.getDeliveryDate());
                    item.setEndAddress(composition.getPoint().getAddress());
                }
            }

            item.setDistance(distance);
            item.setTransportationTime(time);
        });
    }

    private void updateOrCreateDeliveryComposition(CollectionDatasource<Delivery, UUID> deliveriesDs, Point point, Date arrival,
                                                   Integer serviceTime, Integer downTime, Double distance, Integer duration) {
        //if delivery-composition not found - create new, and add it to a new delivery
        boolean present = false;
        for (Delivery delivery : deliveriesDs.getItems()) {
            Optional<DeliveryComposition> item = delivery.getDeliveryCompositions().stream().filter(d -> d.getPoint().equals(point)).findFirst();
            if (item.isPresent()) {
                updateDeliveryComposition(item.get(), arrival, serviceTime, downTime, distance, duration);
                present = true;
                break;
            }
        }
        if (!present) {
            Optional<Delivery> deliveryOptional = deliveriesDs.getItems().stream().filter(PersistenceHelper::isNew).findFirst();
            if (deliveryOptional.isPresent()) {
                Delivery newDelivery = deliveryOptional.get();
                DeliveryComposition deliveryComposition = createDeliveryComposition(newDelivery, point, arrival, serviceTime, downTime, distance, duration);
                newDelivery.addDeliveryComposition(deliveryComposition);
            }
        }
    }

    private DeliveryComposition createDeliveryComposition(Delivery delivery, Point point, Date arrival, Integer serviceTime,
                                                          Integer downTime, Double distance, Integer duration) {
        DeliveryComposition deliveryComposition = metadata.create(DeliveryComposition.class);
        deliveryComposition.setPoint(point);
        deliveryComposition.setStatus(EStatusItems.planned);
        deliveryComposition.setDelivery(delivery);
        updateDeliveryComposition(deliveryComposition, arrival, serviceTime, downTime, distance, duration);
        return deliveryComposition;
    }

    private void updateDeliveryComposition(DeliveryComposition deliveryComposition, Date arrival, Integer serviceTime,
                                           Integer downTime, Double distance, Integer duration) {
        deliveryComposition.setDeliveryDate(arrival);
        deliveryComposition.setServiceTime(serviceTime);
        deliveryComposition.setDownTime(downTime);
        deliveryComposition.setDistance(distance);
        deliveryComposition.setDuration(duration);
    }

    private void updateOrCreateJourneyComposition(Journey journey, CollectionDatasource<JourneyComposition, UUID> journeyCompositionsDs,
                                                  Point point, Date arrival) {
        //change old composition of journey or add new
        Optional<JourneyComposition> firstComposition = journeyCompositionsDs.getItems().stream().filter(
                item -> item.getPoint().equals(point)
        ).findFirst();

        if (firstComposition.isPresent()) {
            firstComposition.get().setLocationDate(arrival);
        } else {
            JourneyComposition composition = createJourneyComposition(journey, arrival, point);
            journeyCompositionsDs.addItem(composition);
        }
    }

    private JourneyComposition createJourneyComposition(Journey journey, Date date, Point point) {
        JourneyComposition composition = metadata.create(JourneyComposition.class);
        composition.setJourney(journey);
        composition.setTransport(journey.getTransport());
        composition.setLocationDate(date);
        composition.setPoint(point);
        return composition;
    }

// preparing json

    public String buildJsonRequest(String taskId, List<Cargo> cargoList, Point depot, Date depotDate, boolean withDeliveries, String additionalData) {
        boolean withAdditional = !additionalData.isEmpty();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("company", "1");
        jsonObject.put("task_id", taskId);
        jsonObject.put("one_car_recalc", true);
        jsonObject.put("calculation_with_addition", withAdditional);
        jsonObject.put("cargo_list", new JSONArray());
        jsonObject.put("points", getJsonPoints(cargoList, depot, withDeliveries));
        jsonObject.put("inn_list", new JSONArray());
        jsonObject.put("depotList", getJsonDepots(depot, depotDate));
        jsonObject.put("jobList", getJsonJobs(cargoList, depot, withDeliveries));
        jsonObject.put("trList", getJsonTransports());
        jsonObject.put("margin_of_safety", 1 + ",");
        jsonObject.put("garbage", false + ",");
        jsonObject.put("etaps", 1 + ",");
        jsonObject.put("parent_id", "");

        if (withAdditional) {
            jsonObject.put("additional_data", new JSONObject(additionalData));
        }

        return jsonObject.toString();
    }

    private JSONArray getJsonJobs(List<Cargo> cargoList, Point depot, boolean withDeliveries) {
        JSONArray jsonArray = new JSONArray();
        cargoList.forEach(cargo -> {
            // it will be false only when we calculate simple "cargo journey"
            if (!depot.equals(cargo.getSendPoint())) {
                addJobsToArray(jsonArray, cargo.getSendPoint(), cargo.getSentDate(), cargo.getSentDateDelta());
            }

            if (withDeliveries) {
                cargo.getDeliveryPoints().forEach(item -> addJobsToArray(jsonArray, item.getPoint(), item.getDeliveryDate(), item.getDeliveryDateDelta()));
            }
        });
        return jsonArray;
    }

    private void addJobsToArray(JSONArray jsonArray, Point point, Date date, int dateDelta) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", point.getCode().toString());
        jsonObject.put("weigth", 0);
        jsonObject.put("volume", 0);
        jsonObject.put("value", 0);
        jsonObject.put("servicetime", 0);
        jsonObject.put("cargo_type", "");
        jsonObject.put("backhaul", false);
        jsonObject.put("penalty", 0);
        jsonObject.put("sort_proto_last", true);
        jsonObject.put("express_delivery", false);
        jsonObject.put("point", point.getCode().toString());
        jsonObject.put("vehicle_required", "1");
        jsonObject.put("position_vehicle_required", point.getPositionInRoute());
        jsonObject.put("routenumb_vehicle_required", 1);
        jsonObject.append("windows", getJsonShift(date, dateDelta));
        jsonArray.put(jsonObject);
    }

    private JSONArray getJsonPoints(List<Cargo> cargoList, Point depot, boolean withDeliveries) {
        JSONArray jsonPoints = new JSONArray();
        cargoList.forEach(cargo -> {
            // it will be false only when we calculate simple "cargo journey"
            if (!depot.equals(cargo.getSendPoint())) {
                addPointToArray(jsonPoints, cargo.getSendPoint());
            }

            if (withDeliveries) {
                cargo.getDeliveryPoints().forEach(deliveryPoint -> addPointToArray(jsonPoints, deliveryPoint.getPoint()));
            }
        });

        addPointToArray(jsonPoints, depot);
        return jsonPoints;
    }

    private void addPointToArray(JSONArray jsonArray, Point point) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ID", point.getCode().toString());
        jsonObject.put("lat", point.getLatitude());
        jsonObject.put("lon", point.getLongitude());
        jsonObject.put("region", "-1");
        jsonObject.put("servicetime", point.getServiceTime() * 60);
        jsonObject.put("add_servicetime", 0);
        jsonObject.put("max_height_transport", 0);
        jsonObject.put("max_length_transport", 0);
        jsonObject.put("only_pallets", false);
        jsonObject.put("ramp", false);
        jsonObject.put("need_refrigerator", false);
        jsonObject.put("temperature_control", false);
        jsonObject.put("ignore_cargo_incompatibility", false);
        jsonObject.put("ignore_pallet_incompatibility", false);

        jsonArray.put(jsonObject);
    }

    private JSONArray getJsonDepots(Point depot, Date date) {
        JSONObject jsonShift = new JSONObject();
        jsonShift.put("start", date.getTime() / 1000);
        jsonShift.put("finish", DateUtils.addDays(date, 365).getTime() / 1000);

        JSONArray jsonDepots = new JSONArray();
        JSONObject jsonPoint = new JSONObject();
        jsonPoint.put("id", depot.getCode().toString());
        jsonPoint.put("point", depot.getCode().toString());
        jsonPoint.put("window", jsonShift);
        jsonDepots.put(jsonPoint);

        return jsonDepots;
    }

    private JSONArray getJsonTransports() {
        JSONArray transportsJsonObjects = new JSONArray();
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", "1");
        jsonObject.put("cost_per_hour", 0);
        jsonObject.put("cost_per_km", 0);
        jsonObject.put("cost_onTime", 0);
        jsonObject.put("maxweigth", 1);
        jsonObject.put("all_restr_mult", 1);
        jsonObject.put("all_speed_mult", 1);
        jsonObject.put("maxvolume", 1);
        jsonObject.put("maxvalue", 0);
        jsonObject.put("amount_use", 1);
        jsonObject.put("points_limit", 0);
        jsonObject.put("time_load", 0);
        jsonObject.put("time_min", 0);
        jsonObject.put("time_max", 0);
        jsonObject.put("add_servicetime", 0);
        jsonObject.put("weigth_nominal", 1);
        jsonObject.put("cycled", false);
        jsonObject.put("road_speed", 1);
        jsonObject.put("point_speed", 1);
        jsonObject.put("height", 0);
        jsonObject.put("length", 0);
        jsonObject.put("start_point", "-1");
        jsonObject.put("main_depot", "");
        jsonObject.put("region", "-1");
        jsonObject.put("number_of_pallets", 0);
        jsonObject.put("refrigerator", false);
        jsonObject.put("temperature_control", false);
        jsonObject.put("low_temperature", 0);
        jsonObject.put("high_temperature", 0);
        jsonObject.put("time_preserving", 0);
        jsonObject.put("can_with_ramp", true);
        jsonObject.put("can_without_ramp", true);
        jsonObject.put("use_inn", false);
        jsonObject.put("min_rest_time", 0);
        jsonObject.put("donor", false);
        jsonObject.put("recipient", false);
        jsonObject.put("proto", false);
        jsonObject.put("multi_use", true);
        jsonObject.put("tr_constraints", new ArrayList());
        jsonObject.put("categories", new ArrayList());
        jsonObject.put("tr_permits", new ArrayList());

        transportsJsonObjects.put(jsonObject);
        return transportsJsonObjects;
    }

    private JSONObject getJsonShift(Date date, int daysDelta) {
        JSONObject jsonObject = new JSONObject();
        Date startWindow;
        Date endWindow;
        if (daysDelta == 0) {
            startWindow = date;
            endWindow = DateUtils.ceiling(date, Calendar.DATE);
        } else {
            startWindow = DateUtils.addDays(date, -daysDelta);
            endWindow = DateUtils.addDays(date, daysDelta);
        }
        jsonObject.put("start", startWindow.getTime() / 1000);
        jsonObject.put("finish", endWindow.getTime() / 1000);
        return jsonObject;
    }

    public String getMathTaskId(String journeyNumber) {
        return "ERP_" + String.valueOf(AppBeans.get(TimeSource.class).currentTimeMillis()) + "_" + journeyNumber;
    }

//end region
}
