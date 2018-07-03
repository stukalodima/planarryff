package com.planarry.erp.web.cargo;

import com.google.common.base.Strings;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.planarry.erp.entity.*;
import com.planarry.erp.entity.Currency;
import com.planarry.erp.service.*;
import com.planarry.erp.web.cargo.access.CargoAccessible;
import com.planarry.erp.web.cargo.clientchooser.ClientChooser;
import com.planarry.erp.web.map.MapPicker;
import com.planarry.erp.web.utils.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

public class CargoCreate extends AbstractEditor<Cargo> {

    private final Logger log = LoggerFactory.getLogger(CargoCreate.class);

    @Named("deliveryPointsDs")
    private CollectionDatasource<CargoDeliveryPoint, UUID> deliveryPointsDs;

    @Named("companyDS")
    private CollectionDatasource<Company, UUID> companyDS;

    @Named("clientDS")
    private CollectionDatasource<Company, UUID> clientDS;

    @Named("accessesDs")
    private CollectionDatasource<AccessToCargo, UUID> accessesDs;

    @Named("transportTypesDs")
    private HierarchicalDatasource<TransportType, UUID> transportTypesDs;

    @Named("deliveryPointsTable")
    private Table<CargoDeliveryPoint> deliveryPointsTable;

    @Named("windowActions")
    private Frame windowActions;

    @Named("fieldGroup_cargoType.cargoType")
    private LookupField cargoTypeField;

    @Named("fieldGroup_company")
    private FieldGroup fieldGroupCompany;

    @Named("fieldGroup_client")
    private FieldGroup fieldGroupClient;

    @Named("fieldGroup_cargoType")
    private FieldGroup fieldGroupCargoType;

    @Named("fieldGroup_company.company")
    private PickerField companyField;

    @Named("fieldGroup_client.client")
    private PickerField clientField;

    @Named("fieldGroup_company.manager")
    private PickerField managerField;

    @Named("fieldGroup_client.clientCaption")
    private TextField clientCaptionField;

    @Named("fieldGroup_client.phone")
    private MaskedField phoneField;

    @Named("fieldGroup_cargoType.palletsType")
    private LookupField palletsTypeField;

    @Named("fieldGroup_cargoType.numberOfPallets")
    private TextField numberOfPalletsField;

    @Named("sendPointServiceTimeTextField")
    private TextField sendPointServiceTimeTextField;

    @Named("fieldGroup_cargoType.volume")
    private TextField volumeField;

    @Named("fieldGroup_cargoType.weight")
    private TextField weightField;

    @Named("fieldGroup_dangerousCargo.dangerousCargo")
    private CheckBox dangerousCargoField;

    @Named("fieldGroup_dangerousCargo.classADR")
    private LookupField classADRField;

    @Named("fieldGroup_transportType.currency")
    private PickerField currencyField;

    @Named("transportTypeField")
    private PickerField transportTypeField;

    @Named("deliveryDateDeltaTxtField")
    private TextField deliveryDateDeltaTxtField;

    @Named("sentDateDeltaTxtField")
    private TextField sentDateDeltaTxtField;

    @Named("sentDateField")
    private DateField sentDateField;

    @Named("deliveryDateField")
    private DateField deliveryDateField;

    @Named("pointPalletsField")
    private TextField pointPalletsField;

    @Named("pointVolumeField")
    private TextField pointVolumeField;

    @Named("pointWeightField")
    private TextField pointWeightField;

    @Named("pointVolumeLabel")
    private Label pointVolumeLabel;

    @Named("pointWeightLabel")
    private Label pointWeightLabel;

    @Named("pointPalletsLabel")
    private Label pointPalletsLabel;

    @Named("pointServiceTimeField")
    private TextField pointServiceTimeField;

    @Named("popupButton")
    private PopupButton popupButton;

    @Named("editTrTypeBtn")
    private Button editTrTypeBtn;

    @Named("removeTrTypeBtn")
    private Button removeTrTypeBtn;

    @Named("shareToAllBtn")
    private Button shareToAllBtn;

    @Named("shareToCompaniesBtn")
    private Button shareToCompaniesBtn;

    @Named("addPointBtn")
    private Button addPointBtn;

    @Named("savePointBtn")
    private Button savePointBtn;

    @Named("closeBtn")
    private Button closeBtn;

    @Named("plusSentDeltaBtn")
    private Button plusSentDeltaBtn;

    @Named("minusSentDeltaBtn")
    private Button minusSentDeltaBtn;

    @Named("startAddressPickerBtn")
    private Button startAddressPickerBtn;

    @Named("calculateBtn")
    private Button calculateBtn;

    @Named("desiredPriceField")
    private CurrencyField desiredPriceField;

    @Named("groupBox_end_fields")
    private GroupBoxLayout groupBoxEndFields;

    @Named("rtaComment")
    private RichTextArea rtaComment;

    @Named("deliveryPointsTable.remove")
    private RemoveAction deliveryPointsTableRemove;

    @Named(ControllerAssistant.NAME)
    private ControllerAssistant controllerAssistant;

    @Named(Metadata.NAME)
    private Metadata metadata;

    @Named(DataManager.NAME)
    private DataManager dataManager;

    @Named(AddressPickerUtils.NAME)
    private AddressPickerUtils addressPickerUtils;

    @Named(CurrencyService.NAME)
    private CurrencyService currencyService;

    @Named(CompanyService.NAME)
    private CompanyService companyService;

    @Named(EmployeeService.NAME)
    private EmployeeService employeeService;

    @Named(MapAssistant.NAME)
    private MapAssistant mapAssistant;

    @Named(CategoryService.NAME)
    private CategoryService categoryService;

    @Named("startAddressSuggestionField")
    private SuggestionField startAddressSuggestionField;

    @Named("endAddressSuggestionField")
    private SuggestionField endAddressSuggestionField;

    private RouterService routerService;

    @Named(TariffCalculator.NAME)
    private TariffCalculator tariffCalculator;

    private AddressClassForGoogle deliveryAddressField = new AddressClassForGoogle();
    private AddressClassForGoogle dispatchAddressField = new AddressClassForGoogle();
    private CargoDeliveryPoint editableDeliveryPoint;
    private JourneyData journeyData;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        cargoTypeField.setNewOptionAllowed(false);
        controllerAssistant.initCompanyDsByActivitiesType(clientDS, controllerAssistant.getCompanyCargoOwnerParams());
        controllerAssistant.initCompanyDsByActivitiesType(companyDS, controllerAssistant.getCompanyCargoCreatorParams());
        routerService = controllerAssistant.defineRouterService();
    }

    @Override
    protected void initNewItem(Cargo item) {

        super.initNewItem(item);
        item.setAccessibleToOwner(true);
        item.setStatus(EStatusItems.created);
        item.setCurrency(currencyService.getBaseCurrency());
        item.setManager(employeeService.getEmployeeByCurrentUser());

        Company company = controllerAssistant.getCurrentUser().getCompany();
        if (Objects.nonNull(company)) {
            if (companyDS.containsItem(company.getId())) {
                item.setCompany(company);
            }
            if (clientDS.containsItem(company.getId())) {
                item.setClient(company);
            }
        }
    }

    @Override
    protected void postInit() {
        super.postInit();
        setCurrencyIcon();
        setCurrencyFieldRequired();
        addClientFieldValueChangeListener();
        addCargoTypeFieldValueChangeListener();
        addCurrencyFieldValueChangeListener();
        addCompanyFieldValueChangeListener();
        addDangerousCargoFieldValueChangeListener();
        currencyField.addValueChangeListener(e -> setCurrencyIcon());
        desiredPriceField.addValueChangeListener(e -> setCurrencyFieldRequired());
        addTransportTypeDsItemChangeListener();
        addClientCaptionFieldValueChangeListener();
        setDeliveryPointsTableDoubleClickAction();
        setDeliveryPointRemoveActionHandler();
        initCargoType();
        defineVisibilityCalculationBtn();
        defineShareBtnCaption();

        calculateBtn.setEnabled(getItem().getCompany() != null);
        controllerAssistant.addLookupAction(clientField, "erp$Company.browse", controllerAssistant.getCompanyCargoOwnerParams());
        controllerAssistant.addLookupAction(companyField, "erp$Company.browse", controllerAssistant.getCompanyCargoCreatorParams());
        controllerAssistant.addLookupAction(managerField, "erp$Employee.browse", ParamsMap.of("role", UUID.fromString("ea309298-a917-4b48-8686-85d1564436b9")));

        if (!PersistenceHelper.isNew(getItem())) {
            Point sendPoint = getItem().getSendPoint();
            startAddressSuggestionField.setValue(getItem().getSendPoint().getAddress());

            sendPointServiceTimeTextField.setValue(getItem().getSendPoint().getServiceTime());
            initGoogleClassFields(dispatchAddressField, sendPoint.getAddress(), sendPoint.getCountry(), sendPoint.getCity(),
                    sendPoint.getStreet(), sendPoint.getBuilding(), sendPoint.getLatitude(), sendPoint.getLongitude());
        }
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

        popupButton.addPopupVisibilityListener(e -> popupButton.setAutoClose(false));
        transportTypeField.removeAllActions();
        deliveryDateDeltaTxtField.setValue(0);

        deliveryDateField.addValueChangeListener(e -> {
            endAddressSuggestionField.setRequired(e.getValue() != null);
            boolean condition = endAddressSuggestionField.getValue() != null && e.getValue() != null;
            addPointBtn.setEnabled(condition);
            savePointBtn.setEnabled(condition);
        });

        hideFields();
    }

    @Override
    protected void postValidate(ValidationErrors errors) {
        if (dispatchAddressField.getCity().isEmpty() || dispatchAddressField.getCountry().isEmpty()) {
            errors.add(messages.getMessage(getClass(), "validate.addressAccuracy"));
        }

        clearDeliveryPointsValues();
        double weightSum = deliveryPointsDs.getItems().stream().mapToDouble(CargoDeliveryPoint::getWeight).sum();
        double volumeSum = deliveryPointsDs.getItems().stream().mapToDouble(CargoDeliveryPoint::getVolume).sum();
        int palletsSum = deliveryPointsDs.getItems().stream().mapToInt(CargoDeliveryPoint::getNumberOfPallets).sum();

        if (!getItem().getWeight().equals(weightSum)) {
            errors.add(messages.getMessage(getClass(), "validate.weightWrong"));
        }
        if (!getItem().getVolume().equals(volumeSum)) {
            errors.add(messages.getMessage(getClass(), "validate.volumeWrong"));
        }
        if (!getItem().getNumberOfPallets().equals(palletsSum)) {
            errors.add(messages.getMessage(getClass(), "validate.palletsWrong"));
        }

        if (deliveryPointsDs.size() == 0) {
            errors.add(messages.getMessage(getClass(), "validate.fillEndAddress"));
        }

        super.postValidate(errors);
    }

    @Override
    protected boolean preCommit() {
        Cargo cargo = getItem();
        if (clientField.getValue() == null) {
            CommitContext commitContext = new CommitContext();
            Company client = metadata.create(Company.class);
            client.setName(clientCaptionField.getValue());
            client.setPhoneNumber(phoneField.getValue());
            client.setType(EPersonTypeItems.naturalPerson);
            client.setAutoCreation(true);
            client.setTransportSearchNarrowRadius(50);
            client.setTransportSearchWideRadius(100);
            client.setPrefix(companyService.createPrefix());
            cargo.setClient(client);

            // load ActivitiesType entity to set it to company
            LoadContext<ActivitiesType> loadContext = LoadContext.create(ActivitiesType.class).setId(UUID.fromString("30c91df6-ce18-4e8b-a693-19a8a19821ba"));
            ActivitiesType activitiesType = dataManager.load(loadContext);
            client.setActivitiesType(activitiesType);

            commitContext.addInstanceToCommit(client);
            dataManager.commit(commitContext);
        }

        if (dispatchAddressField.getAddress() != null) {
            Point point = cargo.getSendPoint() != null ? cargo.getSendPoint() : metadata.create(Point.class);
            point.setAddress(dispatchAddressField.getAddress());
            point.setCountry(dispatchAddressField.getCountry());
            point.setCity(dispatchAddressField.getCity());
            point.setStreet(dispatchAddressField.getStreet());
            point.setBuilding(dispatchAddressField.getBuilding());
            point.setLatitude(dispatchAddressField.getLat());
            point.setLongitude(dispatchAddressField.getLon());
            point.setServiceTime(sendPointServiceTimeTextField.getValue());
            cargo.setSendPoint(null);
            cargo.setSendPoint(point);
        }

        return super.preCommit();
    }

    private void initGoogleClassFields(AddressClassForGoogle classForGoogle, String address, String country, String city, String street,
                                       String building, Double lat, Double lng) {
        classForGoogle.setLat(lat);
        classForGoogle.setLon(lng);
        classForGoogle.setCountry(country);
        classForGoogle.setCity(city);
        classForGoogle.setStreet(street);
        classForGoogle.setBuilding(building);
        classForGoogle.setAddress(address);
    }

    private List<String> searchExecutor(String searchString, Map<String, Object> searchParams) {
        log.info(searchParams.toString());
        return addressPickerUtils.getAddressFromGoogle(searchString, getFrame());
    }

    private final ValueChangeListener startAddressValueChangeListener = new ValueChangeListener() {
        @Override
        public void valueChanged(ValueChangeEvent e) {
            if (dispatchAddressField.getAddress() != null && !dispatchAddressField.getAddress().equals(e.getValue())) {
                dispatchAddressField = addressPickerUtils.getAddressClass((String) e.getValue());
            }
        }
    };

    private final ValueChangeListener endAddressValueChangeListener = new ValueChangeListener() {
        @Override
        public void valueChanged(ValueChangeEvent e) {
            if (deliveryAddressField.getAddress() != null && !deliveryAddressField.getAddress().equals(e.getValue())) {
                deliveryAddressField = addressPickerUtils.getAddressClass((String) e.getValue());
            }
            deliveryDateField.setRequired(e.getValue() != null);
            boolean condition = deliveryDateField.getValue() != null && e.getValue() != null;
            addPointBtn.setEnabled(condition);
            savePointBtn.setEnabled(condition);
        }
    };

    private void addClientCaptionFieldValueChangeListener() {
        clientCaptionField.addValueChangeListener(e -> {
            if (e.getValue() != null && clientField.getValue() == null) {
                List<Company> clientList = companyService.getCompaniesBySubName((String) e.getValue());
                if (!clientList.isEmpty()) {
                    ClientChooser clientChooser = (ClientChooser) openWindow("clientChooser", WindowManager.OpenType.DIALOG,
                            ParamsMap.of("clients", clientList));

                    clientChooser.addCloseListener(actionId -> {
                        Company client = clientChooser.getChosenCompany();
                        if (client != null) {
                            getItem().setClient(client);
                        }
                    });
                }
            }
        });
    }

    private void addDangerousCargoFieldValueChangeListener() {
        dangerousCargoField.addValueChangeListener(e -> {
            if (e.getValue() == null || e.getValue().equals(false)) {
                classADRField.setVisible(false);
                classADRField.setRequired(false);
            } else {
                classADRField.setVisible(true);
                classADRField.setRequired(true);
            }
        });
    }

    private void setCurrencyFieldRequired() {
        currencyField.setRequired(desiredPriceField.getValue() != null && !desiredPriceField.getValue().equals(0));
    }

    private void setCurrencyIcon() {
        desiredPriceField.setCurrency(getItem().getCurrency() == null ? null : getItem().getCurrency().getShirtName());
    }

    private void addClientFieldValueChangeListener() {
        clientField.addValueChangeListener(e -> {
            Company client = (Company) e.getValue();
            if (client == null) {
                getItem().setPhone(null);
                getItem().setClientCaption(null);
            } else {
                getItem().setPhone(client.getPhoneNumber());
                getItem().setClientCaption(client.getName());
            }
        });
    }

    private void addCompanyFieldValueChangeListener() {
        companyField.addValueChangeListener(e -> defineVisibilityCalculationBtn());
    }

    private void addCargoTypeFieldValueChangeListener() {
        cargoTypeField.addValueChangeListener(e -> {
            initCargoType();
        });
    }

    private void addCurrencyFieldValueChangeListener() {
        currencyField.addValueChangeListener(e -> {
            defineVisibilityCalculationBtn();
            if (e.getValue() != null) {
                double ratio = getConvertCurrencyRatio((Currency) e.getPrevValue(), (Currency) e.getValue());
                getItem().setDesiredPrice(getItem().getDesiredPrice() * ratio);

                if (journeyData != null) {
                    journeyData.calcAllPrices(ratio);
                }
            } else {
                getItem().setDesiredPrice(0.);
            }
        });
    }

    private void setDeliveryPointsTableDoubleClickAction() {
        deliveryPointsTable.setItemClickAction(new BaseAction("doubleClick").withHandler(e -> {
                    editableDeliveryPoint = deliveryPointsTable.getSingleSelected();
                    Point point = editableDeliveryPoint.getPoint();
                    deliveryDateField.setValue(editableDeliveryPoint.getDeliveryDate());
                    endAddressSuggestionField.setValue(point.getAddress());
                    deliveryDateDeltaTxtField.setValue(editableDeliveryPoint.getDeliveryDateDelta());
                    pointWeightField.setValue(editableDeliveryPoint.getWeight());
                    pointVolumeField.setValue(editableDeliveryPoint.getVolume());
                    pointPalletsField.setValue(editableDeliveryPoint.getNumberOfPallets());
                    pointServiceTimeField.setValue(point.getServiceTime());

                    deliveryAddressField.setAddress(point.getAddress());
                    deliveryAddressField.setCountry(point.getCountry());
                    deliveryAddressField.setCity(point.getCity());
                    deliveryAddressField.setStreet(point.getStreet());
                    deliveryAddressField.setBuilding(point.getBuilding());
                    deliveryAddressField.setLat(point.getLatitude());
                    deliveryAddressField.setLon(point.getLongitude());

                    addPointBtn.setVisible(false);
                    savePointBtn.setVisible(true);
                }
        ));
    }

    private void addTransportTypeDsItemChangeListener() {
        transportTypesDs.addItemChangeListener(e -> {
            TransportType transportType = e.getItem();
            if (transportType != null && !transportType.isRoot()) {
                transportTypeField.setValue(transportType);
                popupButton.setAutoClose(true);
            }
        });
    }

    private void initCargoType() {
        ECargoTypeItems cargoType = cargoTypeField.getValue();
        if (cargoType != null) {
            switch (cargoType) {
                case piledUp:
                case pourUp:
                case boxes:
                    numberOfPalletsField.setValue(null);
                    palletsTypeField.setValue(null);
                    changeVisibilityFields(false);
                    break;
                case pallets:
                    volumeField.setValue(null);
                    changeVisibilityFields(true);
                    break;
            }
        }
    }

    public void pickStartAddress() {
        pickAddress(startAddressSuggestionField, dispatchAddressField);
    }

    public void pickEndAddress() {
        pickAddress(endAddressSuggestionField, deliveryAddressField);
    }

    private void pickAddress(SuggestionField field, AddressClassForGoogle addressField) {
        Map<String, Object> params = new HashMap<>();
        params.put("action", MapPicker.Action.GET_POINT);
        if (!addressField.getAddress().isEmpty() && addressField.getLat() != 0 && addressField.getLon() != 0) {
            params.put("lat", addressField.getLat());
            params.put("lng", addressField.getLon());
            params.put("address", addressField.getAddress());
            params.put("zoomLevel", mapAssistant.getZoomLevel(addressField.getStreet(), addressField.getBuilding()));
        }

        MapPicker mapPicker = (MapPicker) openWindow("map-picker", WindowManager.OpenType.DIALOG, params);
        mapPicker.setAddressPickerUtils(addressPickerUtils);
        addressPickerUtils.pickAddressByMap(mapPicker, field, addressField);
    }

    public void onEditTransportType() {
        openLookup(TransportType.class, transportTypeLookupHandler, WindowManager.OpenType.DIALOG);
    }

    public void onRemoveTransportType() {
        transportTypesDs.setItem(null);
        getItem().setTransportType(null);
    }

    public void onMinusDeliveryDateDelta() {
        changeValueDateDeltaField(deliveryDateDeltaTxtField, -1);
    }

    public void onPlusDeliveryDateDelta() {
        changeValueDateDeltaField(deliveryDateDeltaTxtField, 1);
    }

    public void onMinusSentDateDelta() {
        changeValueDateDeltaField(sentDateDeltaTxtField, -1);
    }

    public void onPlusSentDateDelta() {
        changeValueDateDeltaField(sentDateDeltaTxtField, 1);
    }

    private void changeValueDateDeltaField(TextField textField, int i) {
        Integer value = textField.getValue();
        if (value > 0 || (value == 0 && i > 0)) {
            textField.setValue(value + i);
        }
    }

    private Lookup.Handler transportTypeLookupHandler = items -> {
        if (items.size() == 1) {
            for (Object item : items) {
                TransportType transportType = (TransportType) item;
                getItem().setTransportType(transportType);
            }
        }
    };

    public void onShareToAll() {
        clearAccessDS();
        getItem().setAccessibleToAll(!getItem().getAccessibleToAll());
        getItem().setAccessibleToOwner(!getItem().getAccessibleToAll());
        defineShareBtnCaption();
    }

    public void onShareToCompanies() {
        List<AccessToCargo> accesses = new ArrayList<>(accessesDs.getItems());
        List<Company> companies = accesses.stream().map(AccessToCargo::getCompany).collect(Collectors.toList());
        Map<String, Object> params = ParamsMap.of("cargo", getItem(), "accesses", accesses, "companies", companies);
        CargoAccessible window = (CargoAccessible) openWindow("cargoAccessible", WindowManager.OpenType.DIALOG, params);
        window.addCloseWithCommitListener(() -> {
            List<AccessToCargo> accessToCargoes = new ArrayList<>(accessesDs.getItems());
            accessToCargoes.forEach(item -> {
                if (!window.getNewAccesses().contains(item)) {
                    accessesDs.removeItem(item);
                }
            });
            window.getNewAccesses().forEach(item -> {
                if (accessesDs.getItems().stream().noneMatch(accessToCargo -> accessToCargo.getCompany().equals(item.getCompany()))) {
                    accessesDs.addItem(item);
                }
            });

            getItem().setAccessibleToOwner(accessesDs.size() == 0);
            getItem().setAccessibleToAll(false);
            defineShareBtnCaption();
        });
    }

    public void onAddDeliveryPoint() {
        if (!deliveryAddressField.getCity().isEmpty() && !deliveryAddressField.getCountry().isEmpty() && deliveryDateField.getValue() != null) {
            Point point = metadata.create(Point.class);
            CargoDeliveryPoint deliveryPoint = metadata.create(CargoDeliveryPoint.class);
            deliveryPoint.setOrder(deliveryPointsDs.size() + 1);
            saveDeliveryPoint(deliveryPoint, point);
            clearDeliveryPointFields();
            deliveryPointsDs.addItem(deliveryPoint);
        } else {
            showNotification(messages.getMessage(getClass(), "validate.addressAccuracy"), NotificationType.WARNING);
        }
    }

    public void onSaveDeliveryPoint() {
        if (!deliveryAddressField.getCity().isEmpty() && !deliveryAddressField.getCountry().isEmpty()) {
            saveDeliveryPoint(editableDeliveryPoint, editableDeliveryPoint.getPoint());
            clearDeliveryPointFields();

            savePointBtn.setVisible(false);
            addPointBtn.setVisible(true);
        }
    }

    public void onClose() {
        close(Window.CLOSE_ACTION_ID, true);
    }

    public void onUp() {
        boolean condition = deliveryPointsDs.getItem().getOrder() > 1;
        movePointInTable(-1, condition);
    }

    public void onDown() {
        boolean condition = deliveryPointsDs.getItem().getOrder() < deliveryPointsDs.size();
        movePointInTable(1, condition);
    }

    public void onCalculatePrice() {
        if (!PersistenceHelper.isNew(getItem())) {
            if (deliveryPointsDs.size() != 0 && !dispatchAddressField.getCity().isEmpty() && !dispatchAddressField.getCountry().isEmpty()) {
                Category category = categoryService.getActualCategory(getItem().getWeight(), getItem().getVolume(), getItem().getNumberOfPallets());
                if (category != null) {
                    PolygonMap returnArea = dataManager.reload(getItem().getCompany().getReturnArea(), "polygonMap-view");
                    journeyData = tariffCalculator.calculate(routerService, getItem(), returnArea, category);
                    journeyData.calcAllPrices(getConvertCurrencyRatio(currencyService.getBaseCurrency(), getItem().getCurrency()));
                    if (journeyData.isCorrect()){
                        getItem().setDesiredPrice(journeyData.getTotalPrice());
                        openJourneyDataWindow(category);
                    } else {
                        showNotification(messages.getMessage(getClass(), "calculationError"), NotificationType.WARNING);
                    }
                } else {
                    showNotification(messages.getMessage(getClass(), "categoryError"), NotificationType.WARNING);
                }
            } else {
                showNotification(messages.getMessage(getClass(), "emptyAddresses"), NotificationType.WARNING);
            }
        } else {
            showNotification(messages.getMessage(getClass(), "needSave"), NotificationType.WARNING);
        }
    }

    private void openJourneyDataWindow(Category category){
        HashMap<String, Object> params = new HashMap<>();
        params.put("action", MapPicker.Action.SHOW_DELIVERY_DATA);
        params.put("areas", category.getAreas());
        params.put("startPoint", getItem().getSendPoint());
        params.put("deliveryPoints", getItem().getDeliveryPoints().stream().map(CargoDeliveryPoint::getPoint).collect(Collectors.toList()));
        params.put("journeyData", journeyData);
        params.put("currency", getItem().getCurrency().getShirtName());
        params.put("track", journeyData.getTrack());

        openWindow("map-picker", WindowManager.OpenType.DIALOG.maximized(true), params);
    }

    private double getConvertCurrencyRatio(Currency prevCurrency, Currency currency) {
        if (prevCurrency != null) {
            CurrencyRate currencyRate = currencyService.getCurrencyRate(currency);
            CurrencyRate prevCurrencyRate = currencyService.getCurrencyRate(prevCurrency);
            if (currencyRate != null && prevCurrencyRate != null) {
                return controllerAssistant.calcConvertCurrencyRatio(prevCurrency.getCoefficient(), prevCurrencyRate.getRate(),
                        currency.getCoefficient(), currencyRate.getRate());
            }
        }
        return 1;
    }


    private void defineShareBtnCaption() {
        String captionMsg = getItem().getAccessibleToAll() ? "unshare" : "shareToAll";
        shareToAllBtn.setCaption(messages.getMessage(getClass(), captionMsg));
    }

    private void defineVisibilityCalculationBtn() {
        calculateBtn.setEnabled(getItem().getCompany() != null && getItem().getCurrency() != null);
    }

    private void clearAccessDS() {
        if (accessesDs.size() > 0) {
            List<AccessToCargo> items = new ArrayList<>(accessesDs.getItems());
            items.forEach(accessesDs::removeItem);
        }
    }

    private void clearDeliveryPointsValues() {
        ECargoTypeItems cargoType = cargoTypeField.getValue();
        if (cargoType != null) {
            switch (cargoType) {
                case piledUp:
                case pourUp:
                case boxes:
                    deliveryPointsDs.getItems().forEach(item -> item.setNumberOfPallets(0));
                    break;
                case pallets:
                    deliveryPointsDs.getItems().forEach(item -> item.setVolume(0.));
                    break;
            }
        }
    }

    private void saveDeliveryPoint(CargoDeliveryPoint deliveryPoint, Point point) {
        point.setAddress(deliveryAddressField.getAddress());
        point.setCountry(deliveryAddressField.getCountry());
        point.setCity(deliveryAddressField.getCity());
        point.setStreet(deliveryAddressField.getStreet());
        point.setBuilding(deliveryAddressField.getBuilding());
        point.setLatitude(deliveryAddressField.getLat());
        point.setLongitude(deliveryAddressField.getLon());
        point.setServiceTime(pointServiceTimeField.getValue());
        deliveryAddressField.setDefaultValues();

        deliveryPoint.setCargo(getItem());
        deliveryPoint.setDeliveryDateDelta(Integer.MAX_VALUE);//don't remove
        deliveryPoint.setDeliveryDate(deliveryDateField.getValue());
        deliveryPoint.setWeight(pointWeightField.getValue());
        deliveryPoint.setVolume(pointVolumeField.getValue());
        deliveryPoint.setNumberOfPallets(pointPalletsField.getValue());
        deliveryPoint.setDeliveryDateDelta(deliveryDateDeltaTxtField.getValue());
        deliveryPoint.setPoint(point);
    }

    private void changeVisibilityFields(boolean isPallet) {
        volumeField.setVisible(!isPallet);
        palletsTypeField.setVisible(isPallet);
        numberOfPalletsField.setVisible(isPallet);

        pointPalletsField.setVisible(isPallet);
        pointPalletsLabel.setVisible(isPallet);
        pointVolumeField.setVisible(!isPallet);
        pointVolumeLabel.setVisible(!isPallet);
        deliveryPointsTable.getColumn("weight").setCollapsed(false);
        deliveryPointsTable.getColumn("numberOfPallets").setCollapsed(!isPallet);
        deliveryPointsTable.getColumn("volume").setCollapsed(isPallet);
    }

    private void clearDeliveryPointFields() {
        deliveryDateDeltaTxtField.setValue(0);
        deliveryDateField.setValue(null);
        endAddressSuggestionField.setValue(null);
        pointWeightField.setValue(null);
        pointVolumeField.setValue(null);
        pointPalletsField.setValue(null);
        pointServiceTimeField.setValue(null);
    }

    private void hideFields() {
        Company company = controllerAssistant.getCurrentUser().getCompany();
        if ((company == null || (getItem().getCompany() != null && !company.equals(getItem().getCompany())))
                && !controllerAssistant.getCurrentUser().getIsAdmin()) {
            groupBoxEndFields.setEnabled(false);
            deliveryPointsTableRemove.setVisible(false);
            shareToAllBtn.setEnabled(false);
            shareToCompaniesBtn.setEnabled(false);
            dangerousCargoField.setEditable(false);
            fieldGroupCompany.setEditable(false);
            fieldGroupCargoType.setEditable(false);
            fieldGroupClient.setEditable(false);
            sentDateDeltaTxtField.setEditable(false);
            sentDateField.setEditable(false);
            startAddressPickerBtn.setEnabled(false);
            startAddressSuggestionField.setEditable(false);
            sendPointServiceTimeTextField.setEditable(false);
            desiredPriceField.setEditable(false);
            currencyField.setEditable(false);
            transportTypeField.setEditable(false);
            transportTypeField.setEnabled(false);
            classADRField.setEditable(false);
            closeBtn.setVisible(true);
            windowActions.setVisible(false);
            popupButton.setEnabled(false);
            editTrTypeBtn.setEnabled(false);
            removeTrTypeBtn.setEnabled(false);
            managerField.removeAllActions();
            plusSentDeltaBtn.setEnabled(false);
            minusSentDeltaBtn.setEnabled(false);
            rtaComment.setEditable(false);
        }
    }

    private void movePointInTable(int step, boolean condition) {
        CargoDeliveryPoint currentPoint = deliveryPointsDs.getItem();
        if (condition) {
            CargoDeliveryPoint nextPoint = null;
            for (CargoDeliveryPoint p : deliveryPointsDs.getItems()) {
                if (p.getOrder().equals(currentPoint.getOrder() + step)) {
                    nextPoint = p;
                    break;
                }
            }
            if (nextPoint != null) {
                nextPoint.setOrder(nextPoint.getOrder() - step);
                currentPoint.setOrder(currentPoint.getOrder() + step);
                deliveryPointsTable.sort("order", Table.SortDirection.ASCENDING);
            }
        }
    }

    private void setDeliveryPointRemoveActionHandler() {
        deliveryPointsTableRemove.setAfterRemoveHandler(removedItems -> {
            int pos = 1;
            for (CargoDeliveryPoint item : deliveryPointsDs.getItems()) {
                item.setOrder(pos);
                pos++;
            }
        });
    }
}
