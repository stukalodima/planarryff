
package com.planarry.erp.web.transportstate;

import com.google.common.collect.ImmutableMap;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.planarry.erp.entity.*;
import com.planarry.erp.service.JourneyService;
import com.planarry.erp.service.RouterService;
import com.planarry.erp.web.map.MapPicker;
import com.planarry.erp.web.utils.AddressClassForGoogle;
import com.planarry.erp.web.utils.AddressPickerUtils;
import com.planarry.erp.web.utils.ControllerAssistant;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TransportStateCreate extends AbstractWindow {

    private final Logger log = LoggerFactory.getLogger(TransportState.class);

    @Named("journeyCompositionDs")
    private CollectionDatasource<JourneyComposition, UUID> journeyCompositionDs;

    @Named("locationDateLabel")
    private Label locationDateLabel;

    @Named("locationAddressLabel")
    private Label locationAddressLabel;

    @Named("locationCssLayout")
    private CssLayout locationCssLayout;

    @Named("locationDateField")
    private DateField locationDateField;

    @Named("endDateField")
    private DateField endDateField;

    @Named("startDateField")
    private DateField startDateField;

    @Named("compositionDateField")
    private DateField compositionDateField;

    @Named("transportField")
    private PickerField transportField;

    @Named("typeField")
    private OptionsGroup typeField;

    @Named("addBtn")
    private Button addBtn;

    @Named("journey_hbox")
    private HBoxLayout journeyHbox;

    @Inject
    private ButtonsPanel actionPanel;

    @Named("compositionTable")
    private Table<JourneyComposition> compositionTable;

    @Named("groupBox_location")
    private GroupBoxLayout locationGroupBox;

    @Named("startAddressSuggestionField")
    private SuggestionField startAddressSuggestionField;

    @Named("endAddressSuggestionField")
    protected SuggestionField endAddressSuggestionField;

    @Named("locationAddressSuggestionField")
    private SuggestionField locationAddressSuggestionField;

    @Named("compositionAddressSuggestionField")
    protected SuggestionField compositionAddressSuggestionField;

    private Metadata metadata = AppBeans.get(Metadata.NAME);

    private DataManager dataManager = AppBeans.get(DataManager.NAME);

    @Named(JourneyService.NAME)
    private JourneyService journeyService;

    @Named(AddressPickerUtils.NAME)
    private AddressPickerUtils addressPickerUtils;

    @Named(ControllerAssistant.NAME)
    private ControllerAssistant controllerAssistant;

    private RouterService routerService;

    private AddressClassForGoogle startAddressClass = new AddressClassForGoogle();
    private AddressClassForGoogle endAddressClass = new AddressClassForGoogle();
    private AddressClassForGoogle locationAddressClass = new AddressClassForGoogle();
    private AddressClassForGoogle compositionAddressClass = new AddressClassForGoogle();

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        routerService = controllerAssistant.defineRouterService();

        addTypeFieldValueChangeListener();
        addTransportFieldLookupAction();
        addTransportFieldValueChangeListener();
        locationDateField.setValue(AppBeans.get(TimeSource.class).currentTimestamp());
        addCompositionFieldsValueChangeListener(compositionDateField, compositionAddressSuggestionField);
        addCompositionFieldsValueChangeListener(compositionAddressSuggestionField, compositionDateField);

        startAddressSuggestionField.addValueChangeListener(startAddressValueChangeListener);
        startAddressSuggestionField.setEnterActionHandler(currentSearchString ->
                addressPickerUtils.getAddressSearchResult(startAddressSuggestionField, currentSearchString, getFrame()));
        startAddressSuggestionField.setArrowDownActionHandler(currentSearchString ->
                addressPickerUtils.getAddressSearchResult(startAddressSuggestionField, currentSearchString, getFrame()));
        startAddressSuggestionField.setSearchExecutor(this::searchExecutor);

        endAddressSuggestionField.addValueChangeListener(endAddressValueChangeListener);
        endAddressSuggestionField.setEnterActionHandler(currentSearchString ->
                addressPickerUtils.getAddressSearchResult(endAddressSuggestionField, currentSearchString, getFrame()));
        endAddressSuggestionField.setArrowDownActionHandler(currentSearchString ->
                addressPickerUtils.getAddressSearchResult(endAddressSuggestionField, currentSearchString, getFrame()));
        endAddressSuggestionField.setSearchExecutor(this::searchExecutor);

        locationAddressSuggestionField.addValueChangeListener(locationValueChangeListener);
        locationAddressSuggestionField.setEnterActionHandler(currentSearchString ->
                addressPickerUtils.getAddressSearchResult(locationAddressSuggestionField, currentSearchString, getFrame()));
        locationAddressSuggestionField.setArrowDownActionHandler(currentSearchString ->
                addressPickerUtils.getAddressSearchResult(locationAddressSuggestionField, currentSearchString, getFrame()));
        locationAddressSuggestionField.setSearchExecutor(this::searchExecutor);

        compositionAddressSuggestionField.addValueChangeListener(compositionAddressValueChangeListener);
        compositionAddressSuggestionField.setEnterActionHandler(currentSearchString ->
                addressPickerUtils.getAddressSearchResult(compositionAddressSuggestionField, currentSearchString, getFrame()));
        compositionAddressSuggestionField.setArrowDownActionHandler(currentSearchString ->
                addressPickerUtils.getAddressSearchResult(compositionAddressSuggestionField, currentSearchString, getFrame()));
        compositionAddressSuggestionField.setSearchExecutor(this::searchExecutor);
    }

    @Override
    protected void postValidate(ValidationErrors errors) {
        if (ETransportStateDocTypeItems.journey.equals(typeField.getValue())) {
            validateAddressField(errors, startAddressClass);
            validateAddressField(errors, endAddressClass);
        } else if (ETransportStateDocTypeItems.location.equals(typeField.getValue())) {
            validateAddressField(errors, locationAddressClass);
        }
        super.postValidate(errors);
    }

    private void addTypeFieldValueChangeListener() {
        typeField.addValueChangeListener(e -> {
            if (ETransportStateDocTypeItems.journey.equals(e.getValue())) {
                changeFieldsState(false);
                expand(compositionTable);
            } else if (ETransportStateDocTypeItems.location.equals(e.getValue())) {
                changeFieldsState(true);
                expand(actionPanel);
            }
        });
    }

    private void addTransportFieldValueChangeListener() {
        transportField.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                journeyCompositionDs.getItems().forEach(journeyComposition -> journeyComposition.setTransport((Transport) e.getValue()));
            }
        });
    }

    private void addCompositionFieldsValueChangeListener(Field targetField, Field companionField) {
        targetField.addValueChangeListener(e -> {
            boolean enabled = e.getValue() != null && companionField.getValue() != null && !compositionAddressClass.getCountry().isEmpty()
                    && !compositionAddressClass.getCity().isEmpty();
            addBtn.setEnabled(enabled);
        });
    }

    private void addTransportFieldLookupAction() {
        PickerField.LookupAction lookupAction = transportField.addLookupAction();
        lookupAction.setLookupScreen("erp$Transport.browse");
        lookupAction.setLookupScreenOpenType(WindowManager.OpenType.DIALOG);
    }

    private List<String> searchExecutor(String searchString, Map<String, Object> searchParams) {
        log.info(searchParams.toString());
        return addressPickerUtils.getAddressFromGoogle(searchString, getFrame());
    }

    private ValueChangeListener locationValueChangeListener = new ValueChangeListener() {
        @Override
        public void valueChanged(ValueChangeEvent e) {
            if (!locationAddressClass.getAddress().equals(e.getValue())) {
                locationAddressClass = addressPickerUtils.getAddressClass((String) e.getValue());
            }
        }
    };

    private ValueChangeListener startAddressValueChangeListener = new ValueChangeListener() {
        @Override
        public void valueChanged(ValueChangeEvent e) {
            if (!startAddressClass.getAddress().equals(e.getValue())) {
                startAddressClass = addressPickerUtils.getAddressClass((String) e.getValue());
            }
        }
    };

    private ValueChangeListener endAddressValueChangeListener = new ValueChangeListener() {
        @Override
        public void valueChanged(ValueChangeEvent e) {
            if (!endAddressClass.getAddress().equals(e.getValue())) {
                endAddressClass = addressPickerUtils.getAddressClass((String) e.getValue());
            }
        }
    };

    private ValueChangeListener compositionAddressValueChangeListener = new ValueChangeListener() {
        @Override
        public void valueChanged(ValueChangeEvent e) {
            if (!compositionAddressClass.getAddress().equals(e.getValue())) {
                compositionAddressClass = addressPickerUtils.getAddressClass((String) e.getValue());
            }
        }
    };


    private void pickAddress(SuggestionField field, AddressClassForGoogle addressClass) {
        Map<String, Object> params = ImmutableMap.of("action", MapPicker.Action.GET_POINT);
        MapPicker mapPicker = (MapPicker) openWindow("map-picker", WindowManager.OpenType.DIALOG, params);
        mapPicker.setAddressPickerUtils(addressPickerUtils);
        addressPickerUtils.pickAddressByMap(mapPicker, field, addressClass);
    }

    public void pickLocation() {
        pickAddress(locationAddressSuggestionField, locationAddressClass);
    }

    public void pickStartAddress() {
        pickAddress(startAddressSuggestionField, startAddressClass);
    }

    public void pickEndAddress() {
        pickAddress(endAddressSuggestionField, endAddressClass);
    }

    public void pickCompositionAddress() {
        pickAddress(compositionAddressSuggestionField, compositionAddressClass);
    }

    public void addJourneyComposition() {

        JourneyComposition composition = createJourneyComposition(null, compositionDateField.getValue(), compositionAddressClass);
        journeyCompositionDs.includeItem(composition);
        compositionDateField.setValue(null);
        compositionAddressSuggestionField.setValue("");
    }

    public void saveAndClose(){
        saveData(true);
    }

    public void save(){
        saveData(false);
    }

    private void saveData(boolean closeWindow) {
        if (validateAll()) {
            CommitContext commitContext = new CommitContext();
            if (ETransportStateDocTypeItems.journey.equals(typeField.getValue())) {
                if (journeyService.journeyNotExistOnDates(transportField.getValue(), startDateField.getValue(), endDateField.getValue())){
                    commitContext.addInstanceToCommit(createTransportState(startDateField, startAddressClass, ETransportStateItems.engaged));
                    commitContext.addInstanceToCommit(createTransportState(endDateField, endAddressClass, ETransportStateItems.free));

                    Journey journey = createJourney();
                    commitContext.addInstanceToCommit(journey);
                    journeyCompositionDs.getItems().forEach(journeyComposition -> {
                        journeyComposition.setJourney(journey);
                        commitContext.addInstanceToCommit(journeyComposition.getPoint());
                        commitContext.addInstanceToCommit(journeyComposition);
                    });
                    JourneyComposition startComposition = createJourneyComposition(journey, startDateField.getValue(), startAddressClass);
                    JourneyComposition endComposition = createJourneyComposition(journey, endDateField.getValue(), endAddressClass);
                    commitContext.addInstanceToCommit(startComposition.getPoint());
                    commitContext.addInstanceToCommit(endComposition.getPoint());
                    commitContext.addInstanceToCommit(startComposition);
                    commitContext.addInstanceToCommit(endComposition);

                    commitAction(commitContext, "message.journeySaved", closeWindow);
                }
                else {
                    showNotification(messages.getMessage(getClass(), "message.journeyExistOnDates"), NotificationType.WARNING);
                }
            } else {
                commitContext.addInstanceToCommit(createTransportState(locationDateField, locationAddressClass, ETransportStateItems.free));
                commitAction(commitContext, "message.locationSaved", closeWindow);
            }
        }
    }

    public void cancel() {
        close(Window.CLOSE_ACTION_ID);
    }

    private TransportState createTransportState(DateField dateField, AddressClassForGoogle addressClass, ETransportStateItems state) {
        TransportState transportState = metadata.create(TransportState.class);
        transportState.setStateDate(dateField.getValue());
        transportState.setState(state);
        transportState.setTransport(transportField.getValue());
        transportState.setDocType(typeField.getValue());
        transportState.setLocationAddress(addressClass.getAddress());
        transportState.setLocationLatitude(addressClass.getLat());
        transportState.setLocationLongitude(addressClass.getLon());
        return transportState;
    }

    private Journey createJourney() {
        Journey journey = metadata.create(Journey.class);
        journey.setStartAddress(startAddressClass.getAddress());
        journey.setEndAddress(endAddressClass.getAddress());
        journey.setStartDate(startDateField.getValue());
        journey.setEndDate(endDateField.getValue());
        journey.setTransport(transportField.getValue());
        journey.setStatus(EStatusItems.planned);
        journey.setResidualVolume(0.);
        journey.setResidualWeight(0.);
        journey.setManualJourney(true);
        getTrackAndTransportationData(journey);
        return journey;
    }

    private JourneyComposition createJourneyComposition(Journey journey, Date date, AddressClassForGoogle addressClass) {
        JourneyComposition composition = metadata.create(JourneyComposition.class);
        composition.setTransport(transportField.getValue());
        composition.setJourney(journey);
        composition.setLocationDate(date);
        composition.setPoint(createPoint(addressClass));
        return composition;
    }

    private Point createPoint(AddressClassForGoogle addressClass){
        Point point = metadata.create(Point.class);
        point.setAddress(addressClass.getAddress());
        point.setCountry(addressClass.getCountry());
        point.setCity(addressClass.getCity());
        point.setStreet(addressClass.getStreet());
        point.setBuilding(addressClass.getBuilding());
        point.setLatitude(addressClass.getLat());
        point.setLongitude(addressClass.getLon());
        return point;
    }

    private void changeFieldsState(boolean isLocationDocType) {
        locationAddressSuggestionField.setRequired(isLocationDocType);
        locationDateField.setRequired(isLocationDocType);
        locationGroupBox.setVisible(isLocationDocType);

        journeyHbox.setVisible(!isLocationDocType);
        compositionTable.setVisible(!isLocationDocType);
        startDateField.setRequired(!isLocationDocType);
        endDateField.setRequired(!isLocationDocType);
        startAddressSuggestionField.setRequired(!isLocationDocType);
        endAddressSuggestionField.setRequired(!isLocationDocType);
    }

    private void validateAddressField(ValidationErrors errors, AddressClassForGoogle addressClassForGoogle) {
        if (addressClassForGoogle.getCity().isEmpty() || addressClassForGoogle.getCountry().isEmpty()) {
            errors.add(messages.getMessage(getClass(), "validate.addressAccuracy"));
        }
    }

    private void clearFields(){
        transportField.setValue(null);
        journeyCompositionDs.clear();
        startDateField.setValue(null);
        endDateField.setValue(null);
        locationAddressSuggestionField.setValue(null);
        compositionAddressSuggestionField.setValue(null);
        endAddressSuggestionField.setValue(null);
        startAddressSuggestionField.setValue(null);

        startAddressClass.setDefaultValues();
        endAddressClass.setDefaultValues();
        locationAddressClass.setDefaultValues();
        compositionAddressClass.setDefaultValues();
    }

    private void commitAction(CommitContext commitContext, String message, boolean closeWindow){
        dataManager.commit(commitContext);
        if (closeWindow){
            close(Window.COMMIT_ACTION_ID);
        }
        clearFields();
        showNotification(messages.getMessage(getClass(), message), NotificationType.TRAY);
    }

    private void getTrackAndTransportationData(Journey journey) {
        String resultJson = routerService.getRoute(startAddressClass.getLat(), startAddressClass.getLon(), endAddressClass.getLat(), endAddressClass.getLon());
        journey.setTransportationTime(routerService.parseRouteTime(resultJson));
        journey.setTransportationDistance(routerService.parseRouteDistance(resultJson));
        journey.setTrack(routerService.parseRouteTrack(resultJson));
    }
}