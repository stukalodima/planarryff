package com.planarry.erp.web.journey;

import com.google.common.collect.ImmutableMap;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.planarry.erp.entity.*;
import com.planarry.erp.entity.Currency;
import com.planarry.erp.service.*;
import com.planarry.erp.web.map.MapPicker;
import com.planarry.erp.web.utils.ControllerAssistant;
import com.planarry.erp.web.utils.MapAssistant;
import com.planarry.erp.web.utils.TariffCalculator;
import org.apache.commons.lang.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Named;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class JourneyCreate extends AbstractEditor<Journey> {

    @Named("journeyCargoDs")
    private CollectionDatasource<JourneyCargo, UUID> journeyCargoDs;

    @Named("journeyTransportDs")
    private CollectionDatasource<JourneyTransport, UUID> journeyTransportDs;

    @Named("transportsTable")
    private Table<JourneyTransport> transportTable;

    @Named("createdByTxtFld")
    private TextField createdByTxtFld;

    @Named("clientTxtFld")
    private TextField clientTxtFld;

    @Named("cargoField")
    private PickerField cargoField;

    @Named("createTsTxtFld")
    private TextField createTsTxtFld;

    @Named("currencyField")
    private PickerField currencyField;

    @Named("desiredPriceCurrencyField")
    private CurrencyField desiredPriceCurrencyField;

    @Named("endAddressTxtFld")
    private TextField endAddressTxtFld;

    @Named("startAddressTxtFld")
    private TextField startAddressTxtFld;

    @Named("endDateField")
    private DateField endDateField;

    @Named("startDateField")
    private DateField startDateField;

    @Named("showEndAddressBtn")
    private Button showEndAddressBtn;

    @Named("showStartAddressBtn")
    private Button showStartAddressBtn;

    @Named("nextTransportBtn")
    private Button nextTransportBtn;

    @Named("previousTransportBtn")
    private Button previousTransportBtn;

    @Named("searchBtn")
    private Button searchBtn;

    @Named("showTrackBtn")
    private Button showTrackBtn;

    @Named("buttonCargo")
    private Button cargoBtn;

    @Named("createAndCloseBtn")
    private Button createAndCloseBtn;

    @Named("createBtn")
    private Button createBtn;

    @Named("cancelBtn")
    private Button cancelBtn;

    @Named("info_groupBox")
    private GroupBoxLayout info_groupBox;

    @Named(CurrencyService.NAME)
    private CurrencyService currencyService;

    @Named(JourneyService.NAME)
    private JourneyService journeyService;

    @Named(CategoryService.NAME)
    private CategoryService categoryService;

    @Named(TariffCalculator.NAME)
    private TariffCalculator tariffCalculator;

    @Named(EmployeeService.NAME)
    private EmployeeService employeeService;

    @Named(MathService.NAME)
    private MathService mathService;

    @Named(NumerationService.NAME)
    private NumerationService numerationService;

    @Named(ControllerAssistant.NAME)
    private ControllerAssistant controllerAssistant;

    @Named(MapAssistant.NAME)
    private MapAssistant mapAssistant;

    private Metadata metadata = AppBeans.get(Metadata.NAME);

    private DataManager dataManager = AppBeans.get(DataManager.NAME);

    private RouterService routerService;

    private Cargo cargo;
    private Category category;
    private CurrencyRate currencyRate;
    private final int PAGE_CAPACITY = 10;
    private int page = 1;
    private int errorInCalculate = 0;
    private Point finishPoint;

    private List<JourneyTransport> transportList;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        routerService = controllerAssistant.defineRouterService();
    }

    @Override
    protected void initNewItem(Journey item) {
        super.initNewItem(item);
        item.setCurrency(currencyService.getBaseCurrency());
        item.setStatus(EStatusItems.planned);
        item.setManualJourney(false);
        item.setEmployee(employeeService.getEmployeeByUser(controllerAssistant.getCurrentUser()));

        Company company = controllerAssistant.getUserCompany();
        String prefix = "$$$";
        if (company != null) {
            prefix = controllerAssistant.getUserCompany().getPrefix();
        }
        item.setJourneyNumber(prefix + "-R" + numerationService.getNewDocNumber(controllerAssistant.getUserCompany()));
    }

    @Override
    protected void postInit() {
        super.postInit();
        addJourneyTransportDsItemChangeListener();
        addCargoFieldLookupAction();
        addCargoFieldValueChangeListener();
        addCurrencyFieldValueChangeListener();
        currencyRate = currencyService.getCurrencyRate(getItem().getCurrency());
        setInvisibleElements();
    }

    @Override
    protected void postValidate(ValidationErrors errors) {
        if (journeyTransportDs.getItem() == null) {
            errors.add(messages.getMessage(getClass(), "validate.emptyTransport"));
        }
        super.postValidate(errors);
    }
    // region of listeners

    private void addCurrencyFieldValueChangeListener() {
        currencyField.addValueChangeListener(e -> {
            Currency currency = (Currency) e.getValue();
            if (currency != null) {
                currencyRate = currencyService.getCurrencyRate(currency);
                if (transportList != null) {
                    transportList.forEach(this::calcTransportationValues);
                }
            } else {
                currencyRate = null;
            }
        });
    }

    private void addJourneyTransportDsItemChangeListener() {
        journeyTransportDs.addItemChangeListener(e -> {
            if (e.getItem() != null) {
                getItem().setTransport(e.getItem().getTransport());
                getItem().setAttractingPrice(e.getItem().getAttractingPrice());
                getItem().setTransportationPrice(e.getItem().getTransportationPrice());
            } else {
                getItem().setTransport(null);
            }
        });
    }

    private void addCargoFieldValueChangeListener() {
        cargoField.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                cargo = dataManager.reload((Cargo) e.getValue(), "cargo-with-points-view");

                if (!e.getValue().equals(e.getPrevValue())){
                    JourneyCargo journeyCargo = createJourneyCargo();
                    journeyCargoDs.addItem(journeyCargo);
                    if (e.getPrevValue() != null) {
                        Cargo oldCargo = (Cargo) e.getPrevValue();
                        journeyCargoDs.removeItem(journeyCargoDs.getItems().stream().filter(item -> item.getCargo().equals(oldCargo)).findAny().get());
                    }
                }

                journeyTransportDs.clear();
                calcRoute();
                if (errorInCalculate == 0 && getItem().getTrack() != null) {
                    setElementsVisible();
                } else if (errorInCalculate > 0){
                    showNotification(messages.getMessage(getClass(), "math.cantCalculateDefaultJourney"), NotificationType.WARNING);
                }
            }
        });
    }

    private void setInvisibleElements() {
        if (getItem().getStatus() != EStatusItems.planned) {
            cargoBtn.setEnabled(false);
            searchBtn.setEnabled(false);
            createBtn.setVisible(false);
            createAndCloseBtn.setVisible(false);
            currencyField.setEnabled(false);
            cancelBtn.setCaption(messages.getMessage(getClass(), "close"));
        }
    }

    private void setElementsVisible() {
        nextTransportBtn.setEnabled(false);
        previousTransportBtn.setEnabled(false);
        boolean elementVisible = cargo != null;
        searchBtn.setEnabled(elementVisible);
        showTrackBtn.setEnabled(elementVisible);
        info_groupBox.setVisible(elementVisible);
        if (cargo != null) {
            desiredPriceCurrencyField.setValue(cargo.getDesiredPrice());
            startAddressTxtFld.setValue(getItem().getStartAddress());
            endAddressTxtFld.setValue(getItem().getEndAddress());
            startDateField.setValue(getItem().getStartDate());
            endDateField.setValue(getItem().getEndDate());
            clientTxtFld.setValue(cargo.getClientCaption());
            createTsTxtFld.setValue(cargo.getCreateTs());
            createdByTxtFld.setValue(cargo.getCreatedBy());
            if (cargo.getCurrency() != null) {
                desiredPriceCurrencyField.setCurrency(cargo.getCurrency().getShirtName());
            }
        }
    }

    private void addCargoFieldLookupAction() {
        Map<String, Object> params = ParamsMap.of("status", EStatusItems.created);
        PickerField.LookupAction lookupAction = cargoField.addLookupAction();
        lookupAction.setLookupScreen("erp$Cargo.browse");
        lookupAction.setLookupScreenOpenType(WindowManager.OpenType.DIALOG);
        lookupAction.setLookupScreenParams(params);
    }

    private void findTransports() {
        if (category != null) {
            List<TransportState> transportStateList = journeyService.getTransportStatesByCategory(category, getItem().getStartDate());
            if (!transportStateList.isEmpty()) {
                checkTransportsLocation(transportStateList);
                nextBtnEnable();
                addTransportToTable();
            }
        }
        if (transportList == null || transportList.isEmpty()){
            showNotification(messages.getMessage(getClass(), "message.TransportNotFound"));
        }
    }

    private void checkTransportsLocation(List<TransportState> transportStateList) {
        List<JourneyTransport> tempList = new ArrayList<>();
        Journey journey = getItem();
        for (TransportState location : transportStateList) {

            //if transport not in route between a dates
            if (journeyService.journeyNotExistOnDates(location.getTransport(), journey.getStartDate(), journey.getEndDate())) {
                //find route from transport location to cargo location
                String routeJson = routerService.getRoute(location.getLocationLatitude(), location.getLocationLongitude(),
                        cargo.getSendPoint().getLatitude(), cargo.getSendPoint().getLongitude());
                int routeTime = getRouteTime(routeJson);

                //check if transport has time to move to cargo location
                if (DateUtils.addMinutes(location.getStateDate(), routeTime).compareTo(journey.getStartDate()) <= 0){
                    JourneyTransport jt = createJourneyTransport(location.getTransport(), routeJson);
                    tempList.add(jt);
                }
            }
        }
        transportList = sortJourneyTransportListByDistance(tempList);
    }

    // end region

    // region of actions

    private void showAddress(Double lat, Double lng, String address, String street, String building) {
        MapAssistant.ZoomLevel zoomLevel = mapAssistant.getZoomLevel(building, street);
        Map<String, Object> params = ImmutableMap.of("action", MapPicker.Action.SHOW_POINT,
                "lat", lat,
                "lng", lng,
                "address", address,
                "zoomLevel", zoomLevel);
        openWindow("map-picker", WindowManager.OpenType.DIALOG, params);
    }

    public void onShowStartAddress() {
        Point point = cargo.getSendPoint();
        showAddress(point.getLatitude(), point.getLongitude(), point.getAddress(), point.getStreet(), point.getBuilding());
    }

    public void onShowEndAddress() {
        showAddress(finishPoint.getLatitude(), finishPoint.getLongitude(), finishPoint.getAddress(), finishPoint.getStreet(), finishPoint.getBuilding());
    }

    public void onSearchTransports() {
        journeyTransportDs.clear();
        nextTransportBtn.setEnabled(false);
        previousTransportBtn.setEnabled(false);
        page = 1;
        findTransports();
    }

    public void onShowNextTransport() {
        page++;
        changePage(nextTransportBtn, previousTransportBtn, transportList.size() <= page * PAGE_CAPACITY);
    }

    public void onShowPreviousTransport() {
        page--;
        changePage(previousTransportBtn, nextTransportBtn, page == 1);
    }

    public void onOpenTransportForm(Entity item, String columnId) {
        JourneyTransport journeyTransport = (JourneyTransport) item;
        openEditor(journeyTransport.getTransport(), WindowManager.OpenType.DIALOG);
    }

    public void onShowSupplyTrack(Entity item, String columnId) {
        JourneyTransport journeyTransport = (JourneyTransport) item;
        showTrack(journeyTransport.getTrack(), journeyTransport.getDistance(), journeyTransport.getTime(), null, null);
    }

    public void onShowJourneyData(Entity item, String columnId) {
        JourneyTransport journeyTransport = (JourneyTransport) item;
        JourneyData journeyData = journeyTransport.getJourneyData();
        HashMap<String, Object> params = new HashMap<>();
        params.put("action", MapPicker.Action.SHOW_DELIVERY_DATA);
        params.put("areas", journeyTransport.getTransport().getAreas());
        params.put("startPoint", cargo.getSendPoint());
        params.put("deliveryPoints", cargo.getDeliveryPoints().stream().map(CargoDeliveryPoint::getPoint).collect(Collectors.toList()));
        params.put("journeyData", journeyData);
        params.put("currency", getItem().getCurrency().getShirtName());

        String route = routerService.getRoute(journeyData.getRouteCoordinates());
        params.put("track", routerService.parseRouteTrack(route));
        openWindow("map-picker", WindowManager.OpenType.DIALOG.maximized(true), params);
      }

    public void onShowTransportationTrack() {
        Map<String, Object> params = new HashMap<>();
        params.put("startPoint", cargo.getSendPoint());
        params.put("deliveryPoints", cargo.getDeliveryPoints().stream().map(CargoDeliveryPoint::getPoint).collect(Collectors.toList()));
        params.put("track", getItem().getTrack());
        params.put("action", MapPicker.Action.SHOW_TRANSPORTATION_TRACK);
        openWindow("map-picker", WindowManager.OpenType.DIALOG, params);
    }

    private void showTrack(String track, Double dist, Integer time, String startAddress, String endAddress) {
        Integer hours = time / 60;
        Integer minutes = time % 60;
        Map<String, Object> params = new HashMap<>();
        params.put("action", MapPicker.Action.SHOW_SUPPLY_TRACK);
        params.put("track", track);
        params.put("dist", dist.toString());
        params.put("h", hours.toString());
        params.put("m", minutes.toString());
        params.put("startAddress", startAddress);
        params.put("endAddress", endAddress);
        openWindow("map-picker", WindowManager.OpenType.DIALOG, params);
    }

    public void onButtonCargoClick() {
        openLookup(Cargo.class,
                items -> {
                    if (items.size() == 1) {
                        cargo = (Cargo) items.iterator().next();
                        cargoField.setValue(cargo);
                    }
                },
                WindowManager.OpenType.DIALOG,
                ParamsMap.of("status", EStatusItems.created)
        );
    }

    public void onCancel() {
        close(Window.CLOSE_ACTION_ID);
    }

    public void onSaveAndClose() {
        if (validateAll()) {
            showDialogWindow(saveAndCloseHandler);
        }
    }

    // end region

    private Consumer<Action.ActionPerformedEvent> saveAndCloseHandler = e -> {
        getItem().setFreighter(cargo.getCompany());
            setNewJourneyValues();
            if (commit()) {
                saveJourneyState();
                saveNewTransportLocations();
                saveNewCargoStatus();
                close(Window.COMMIT_ACTION_ID, true);
            }
    };

    private void showDialogWindow(Consumer<Action.ActionPerformedEvent> handler) {
        showOptionDialog(
                messages.getMessage(getClass(), "confirmDialog.title"),
                messages.getMessage(getClass(), "confirmDialog.message") + "\n" + buildTransportInfo(),
                MessageType.CONFIRMATION,
                new Action[]{
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withHandler(handler),
                        new DialogAction(DialogAction.Type.NO, Action.Status.NORMAL)
                }
        );
    }

    private void setNewJourneyValues() {
        Journey journey = getItem();
        JourneyTransport selectedJTransport = journeyTransportDs.getItem();
        JourneyData journeyData = selectedJTransport.getJourneyData();
        journey.setResidualWeight(selectedJTransport.getTransport().getTotalMaxCargoWeight() - cargo.getWeight());
        journey.setResidualVolume(selectedJTransport.getTransport().getTotalMaxCargoVolume() - cargo.getVolume());
        journey.setTrack(routerService.parseRouteTrack(routerService.getRoute(journeyData.getRouteCoordinates())));
        journey.setTransportationPrice(journeyData.getTotalPrice());
        journey.setTransportationDistance(journeyData.getRoadDist());
        journey.setTransportationTime(journeyData.getTotalTime());
        journey.setEndDate(DateUtils.addMinutes(cargo.getSentDate(), journeyData.getTotalTime()));
    }

    private void saveNewCargoStatus() {
        CommitContext commitContext = new CommitContext();
        cargo.setStatus(EStatusItems.planned);
        commitContext.addInstanceToCommit(cargo); // saving new cargo status
        dataManager.commit(commitContext);
    }

    private void saveNewTransportLocations() {
        Journey journey = getItem();
        JourneyTransport selectedJTransport = journeyTransportDs.getItem();

        CommitContext commitContext = new CommitContext();
        TransportState startState = createTransportState(selectedJTransport.getTransport(), journey.getStartDate(), ETransportStateItems.engaged,
                journey.getStartAddress(), cargo.getSendPoint().getLatitude(), cargo.getSendPoint().getLongitude());
        TransportState endState = createTransportState(selectedJTransport.getTransport(), journey.getEndDate(), ETransportStateItems.free,
                finishPoint.getAddress(), finishPoint.getLatitude(), finishPoint.getLongitude());

        commitContext.addInstanceToCommit(startState);
        commitContext.addInstanceToCommit(endState);
        dataManager.commit(commitContext);
    }

    private void saveJourneyState() {
        JourneyState journeyState = metadata.create(JourneyState.class);
        journeyState.setStateDate(getItem().getStartDate());
        journeyState.setJourney(getItem());
        journeyState.setUser(controllerAssistant.getCurrentUser());
        journeyState.setStatus(EStatusItems.planned);
        dataManager.commit(journeyState);
    }

    // end region

    // other functions

    private TransportState createTransportState(Transport transport, Date date, ETransportStateItems state, String address, Double lat, Double lng) {
        TransportState tState = metadata.create(TransportState.class);
        tState.setJourney(getItem());
        tState.setStateDate(date);
        tState.setTransport(transport);
        tState.setState(state);
        tState.setLocationAddress(address);
        tState.setLocationLatitude(lat);
        tState.setLocationLongitude(lng);
        tState.setDocType(ETransportStateDocTypeItems.journey);
        return tState;
    }

    private JourneyCargo createJourneyCargo() {
        JourneyCargo journeyCargo = metadata.create(JourneyCargo.class);
        journeyCargo.setCargo(cargo);
        journeyCargo.setJourney(getItem());
        return journeyCargo;
    }

    private JourneyTransport createJourneyTransport(Transport transport, String routeJson) {
        JourneyTransport journeyTransport = metadata.create(JourneyTransport.class);
        JourneyData journeyData = tariffCalculator.calculate(routerService, cargo, cargo.getCompany().getReturnArea(), transport);
        if (journeyData.isCorrect()){
            journeyTransport.setTransport(transport);
            journeyTransport.setJourneyData(journeyData);
        }

        //calc supply track from transport location to cargo location
        if (routeJson != null) {
            JSONObject jsonObject = new JSONObject(routeJson);
            JSONArray routeGeometry = jsonObject.getJSONArray("route_geometry");
            JSONObject summary = jsonObject.getJSONObject("route_summary");
            Double distance = (double) summary.getInt("total_distance") / 1000;
            journeyTransport.setDistance(Math.rint(distance * 100) / 100);
            journeyTransport.setTime(summary.getInt("total_time"));
            journeyTransport.setTrack(routeGeometry.toString());
        }

        if (currencyField.getValue() != null) {
            calcTransportationValues(journeyTransport);
        }

        return journeyTransport;
    }

    private String buildTransportInfo() {
        JourneyTransport journeyTransport = journeyTransportDs.getItem();

        Transport transport = journeyTransport.getTransport();

        String info = transport.getName() + " " + transport.getIdentNumber() + "\n"
                + messages.getMessage(getClass(), "distSupply") + ": "
                + journeyTransport.getDistance() + " " + messages.getMessage(getClass(), "km") + "\n"
                + messages.getMessage(getClass(), "time") + ": " + journeyTransport.getTime() / 60 + " "
                + messages.getMessage(getClass(), "minutes");

        if (journeyTransport.getAttractingPrice() > 0) {
            info += "\n" + messages.getMessage(getClass(), "attractingPrice") + ": " + journeyTransport.getAttractingPrice();
        }
        if (journeyTransport.getTransportationPrice() > 0) {
            info += "\n" + messages.getMessage(getClass(), "transportationPrice") + ": " + journeyTransport.getTransportationPrice();
        }
        return info;
    }

    private void changePage(Button toDisableBtn, Button toEnableBtn, boolean condition) {
        addTransportToTable();
        if (condition) {
            toDisableBtn.setEnabled(false);
        }
        toEnableBtn.setEnabled(true);
    }

    private void addTransportToTable() {
        journeyTransportDs.clear();
        for (int i = (page - 1) * PAGE_CAPACITY; i < transportList.size() && i < page * PAGE_CAPACITY; i++) {
            journeyTransportDs.includeItem(transportList.get(i));
        }
        transportTable.sort("transportationPrice", Table.SortDirection.ASCENDING);
    }

    private void nextBtnEnable() {
        if (transportList != null && transportList.size() > PAGE_CAPACITY) {
            nextTransportBtn.setEnabled(true);
        }
    }

    private List<JourneyTransport> sortJourneyTransportListByDistance(List<JourneyTransport> transports) {
        return transports.stream().sorted(Comparator.comparing(JourneyTransport::getDistance)).collect(Collectors.toList());
    }

    //end region

    //calculation functions

    private void calcTransportationValues(JourneyTransport jTransport) {
        Transport transport = jTransport.getTransport();
        if (transport.getCurrency() != null) {
            double ratio = 1.;
            CurrencyRate transportCurrencyRate = currencyService.getCurrencyRate(transport.getCurrency());
            if (!currencyField.getValue().equals(transport.getCurrency()) && currencyRate != null && transportCurrencyRate != null) {
                ratio = controllerAssistant.calcConvertCurrencyRatio(transport.getCurrency().getCoefficient(), transportCurrencyRate.getRate(),
                        ((Currency) currencyField.getValue()).getCoefficient(), currencyRate.getRate());
            }

            jTransport.getJourneyData().calcAllPrices(ratio);

            double attractionCost =  transport.getExtraCostAttraction() + (transport.getTotalBaseCostAttraction() / 1000) * jTransport.getJourneyData().getRoadDist();
            jTransport.setTransportationPrice(jTransport.getJourneyData().getTotalPrice());
            jTransport.setAttractingPrice(Math.rint(attractionCost * ratio * 100) / 100);
        }
    }

    private void calcRoute(){
        category = categoryService.getActualCategory(cargo.getWeight(), cargo.getVolume(), cargo.getNumberOfPallets());

        if (category != null) {
            List<String> pointsSequence = new ArrayList<>();
            pointsSequence.add(cargo.getSendPoint().getLatitude() + "," + cargo.getSendPoint().getLongitude());
            cargo.getDeliveryPoints().forEach(item -> pointsSequence.add(item.getPoint().getLatitude() + "," + item.getPoint().getLongitude()));

            String resultJson = routerService.getRoute(pointsSequence);
            if (resultJson != null) {
                finishPoint = cargo.getDeliveryPoints().get(cargo.getDeliveryPoints().size() - 1).getPoint();
                getItem().setStartAddress(cargo.getSendPoint().getAddress());
                getItem().setEndAddress(finishPoint.getAddress());
                getItem().setStartDate(cargo.getSentDate());
                getItem().setEndDate(DateUtils.addMinutes(cargo.getSentDate(), getRouteTime(resultJson)));
                getItem().setTrack(routerService.parseRouteTrack(resultJson));
            }
        } else {
            showNotification(messages.getMessage(getClass(), "message.categoryNotExist"), NotificationType.WARNING);
        }
    }

    //return route time in minutes
    private int getRouteTime(String resultJson) {
        Integer routeTime = routerService.parseRouteTime(resultJson);
        if (routeTime == null){
            errorInCalculate++;
            return 0;
        }
        return routeTime;
    }
}
