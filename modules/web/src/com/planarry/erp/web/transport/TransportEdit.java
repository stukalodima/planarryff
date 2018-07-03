package com.planarry.erp.web.transport;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.planarry.erp.entity.*;
import com.planarry.erp.entity.Currency;
import com.planarry.erp.service.ConstantService;
import com.planarry.erp.service.CurrencyService;
import com.planarry.erp.service.EmployeeService;
import com.planarry.erp.service.JourneyService;
import com.planarry.erp.web.utils.ControllerAssistant;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

public class TransportEdit extends AbstractEditor<Transport> {

    @Inject
    private Datasource<Transport> transportDs;
    @Inject
    private CollectionDatasource<TransportArea, UUID> categoryAreasDs;
    @Inject
    private CollectionDatasource<TransportsAccess, UUID> transportsAccessDs;
    @Inject
    private HierarchicalDatasource<TransportType, UUID> transportTypesDs;
    @Inject
    private CollectionDatasource<Company, UUID> companyDS;
    @Inject
    private CollectionDatasource<TransportClassADR, UUID> classAdrDs;
    @Inject
    private CollectionDatasource<TransportCrewMembers, UUID> crewMembersDs;
    @Inject
    private CollectionDatasource<Employee, UUID> crewMembersSourceDs;
    @Inject
    private CollectionDatasource<TransportDrivers, UUID> driversDs;
    @Inject
    private CollectionDatasource<Employee, UUID> driversSourceDs;
    @Inject
    private CollectionDatasource<TransportForwarders, UUID> forwardersDs;
    @Inject
    private CollectionDatasource<Employee, UUID> forwardersSourceDs;
    @Inject
    private CollectionDatasource<TransportTrailers, UUID> trailersDs;
    @Inject
    private CollectionDatasource<Trailer, UUID> trailersSourceDs;
    @Inject
    private Table<TransportTrailers> trailersTable;
    @Inject
    private Table<Trailer> trailersSourceTable;
    @Inject
    private Table<TransportForwarders> forwardersTable;
    @Inject
    private Table<Employee> forwardersSourceTable;
    @Inject
    private Table<TransportDrivers> driversTable;
    @Inject
    private Table<Employee> driversSourceTable;
    @Inject
    private Table<TransportCrewMembers> crewMembersTable;
    @Inject
    private Table<Employee> crewMembersSourceTable;

    @Named("basicFieldGroup_1.employee")
    private PickerField transportRoleField;

    @Named("crewMembersTable.remove")
    private RemoveAction crewMembersTableRemove;

    @Named("driversTable.remove")
    private RemoveAction driversTableRemove;

    @Named("forwardersTable.remove")
    private RemoveAction forwardersTableRemove;

    @Named("trailersTable.remove")
    private RemoveAction trailersTableRemove;

    @Named("basicFieldGroup.truckType")
    private LookupField truckTypeField;

    @Named("basicFieldGroup.identNumber")
    private TextField identNumber;

    @Named("basicFieldGroup_1.company")
    private LookupPickerField companyPickerField;

    @Named("basicFieldGroup_1.employee")
    private PickerField transportManager;

    @Named("basicFieldGroup_1.vinCode")
    private TextField vinCodeField;

    @Named("basic_gbox.basicFieldGroup")
    private FieldGroup basicFieldGroup;

    @Named("technicalParams_gbox_2.technicalParamsFieldGroup_2")
    private FieldGroup technicalParamsFieldGroup_2;

    @Named("technicalParams_gbox_4.technicalParamsFieldGroup_4")
    private FieldGroup technicalParamsFieldGroup_4;

    @Named("technicalParams_gbox_4.technicalParamsFieldGroup_5")
    private FieldGroup technicalParamsFieldGroup_5;

    @Named("technicalParams_gbox.technicalParamsFieldGroup")
    private FieldGroup technicalParamsFieldGroup;

    @Named("ADR_gbox_3.ADR_field_group")
    private FieldGroup ADR_field_group;

    @Named("basic_gbox.basicFieldGroup_1")
    private FieldGroup basicFieldGroup_1;

    @Named("actionsButtonsPanel")
    private ButtonsPanel actionsButtonsPanel;

    @Named("shareButtonsPanel")
    private ButtonsPanel shareButtonsPanel;

    @Named("addAreaBtn")
    private Button addAreaBtn;

    @Named("saveAreaBtn")
    private Button saveAreaBtn;

    @Named("cancelButton")
    private Button cancelButton;

    @Named("editTrTypeBtn")
    private Button editTrTypeBtn;

    @Named("removeTrTypeBtn")
    private Button removeTrTypeBtn;

    @Named("trailersButtons")
    private VBoxLayout trailersButtons;

    @Named("tabSheet.OtherCrewMembersTab")
    private OrderedContainer otherCrewMembersTab;

    @Named("tabSheet.PhotoTab")
    private OrderedContainer photoTab;

    @Named("currencyField")
    private LookupPickerField currencyField;

    @Named("technicalParamsFieldGroup_2.maxWeight")
    private TextField maxWeightField;

    @Named("technicalParamsFieldGroup.sensorCode")
    private TextField sensorCodeField;

    @Named("temperatureContainer.lowTempField")
    private TextField lowTempField;

    @Named("temperatureContainer.highTempField")
    private TextField highTempField;

    @Named("basicFieldGroup_1.company")
    private LookupPickerField companyField;

    @Named("costHourField")
    private CurrencyField costHourField;

    @Named("costKilometerField")
    private CurrencyField costKilometerField;

    @Named("costTonKilometerField")
    private CurrencyField costTonKilometerField;

    @Named("costSupplyField")
    private CurrencyField costSupplyField;

    @Named("classADRField")
    private OptionsGroup classADRField;

    @Named("technicalParamsFieldGroup_4.temperatureConditions")
    private CheckBox temperatureConditionsField;

    @Named("ADR_field_group.dangerousCargoPermission")
    private CheckBox dangerousCargoField;

    @Named("technicalParams_gbox_2")
    private GroupBoxLayout technicalParamsGBox;

    @Named("technicalParams_gbox_4")
    private GroupBoxLayout temperatureParamsGBox;

    @Named("temp_hbox")
    private HBoxLayout temperatureHbox;

    @Named("hbox_transportType")
    private HBoxLayout transportTypeHbox;

    @Named("popupButton")
    private PopupButton popupButton;

    @Named("basicFieldGroup.downloadType")
    private LookupField downloadTypeField;

    @Named("transportTypeField")
    private PickerField transportTypeField;

    @Named("shareToAll")
    private Button shareToAll;

    @Named("areaHourTextField")
    private TextField areaHourTextField;

    @Named("areaKilometerTextField")
    private TextField areaKilometerTextField;

    @Named("areaEntranceTextField")
    private TextField areaEntranceTextField;

    @Named("areaExitTextField")
    private TextField areaExitTextField;

    @Named("areaSupplyTextField")
    private TextField areaSupplyTextField;

    @Named("areaPickerField")
    private PickerField areaPickerField;

    @Named("areasTable")
    private Table<TransportArea> areasTable;

    @Named("uploadField")
    private FileUploadField uploadField;

    @Named(Metadata.NAME)
    private Metadata metadata;

    @Named(DataManager.NAME)
    private DataManager dataManager;

    @Named(ComponentsFactory.NAME)
    private ComponentsFactory componentsFactory;

    @Named(CurrencyService.NAME)
    private CurrencyService currencyService;

    @Named(EmployeeService.NAME)
    private EmployeeService employeeService;

    @Named(ConstantService.NAME)
    private ConstantService constantService;

    @Named(JourneyService.NAME)
    private JourneyService journeyService;

    @Named(ControllerAssistant.NAME)
    private ControllerAssistant controllerAssistant;

    private CurrencyField baseAttractionField;

    private CurrencyField extraAttractionField;

    private TransportArea currentTransportArea;

    private boolean inRoute;

    public interface Companion {
        <T extends TransportEquipmentTargetEntity, K extends OrderedTableEntity> void initDragAndDrop(Table<K> targetTable, Table<T> sourceTable,
                                                                                                      Class<T> entityClass, Class<K> wrapperClass,
                                                                                                      TableAddAction addAction, TableRemoveAction removeAction);
    }


    public interface TableAddAction {
        <T extends TransportEquipmentTargetEntity, K extends OrderedTableEntity> void addEntity(CollectionDatasource<K, UUID> targetDs,
                                                                                                CollectionDatasource<T, UUID> sourceDs,
                                                                                                T entity,
                                                                                                Class<K> wrapperClass);
    }

    public interface TableRemoveAction {
        <T extends TransportEquipmentTargetEntity, K extends OrderedTableEntity> void removeEntity(Set<K> removedItems, CollectionDatasource<K, UUID> ds,
                                                                                                   CollectionDatasource<T, UUID> sourceDs);
    }

    @Override
    public void init(Map<String, Object> params) {

        controllerAssistant.addLookupAction(transportRoleField, "erp$Employee.browse",
                ParamsMap.of("role", UUID.fromString("7c36da70-e965-4274-8486-78d15d3fad6a")));

        super.init(params);
        controllerAssistant.initCompanyDsByActivitiesType(companyDS, controllerAssistant.getCompanyTransportOwnerParams());

        Companion companion = getCompanion();
        if (companion != null) {
            companion.initDragAndDrop(trailersTable, trailersSourceTable, Trailer.class, TransportTrailers.class, this::includeEntityToTransport, this::preRemovalHandler);
            companion.initDragAndDrop(driversTable, driversSourceTable, Employee.class, TransportDrivers.class, this::includeEntityToTransport, this::removalHandler);
            companion.initDragAndDrop(forwardersTable, forwardersSourceTable, Employee.class, TransportForwarders.class, this::includeEntityToTransport, this::removalHandler);
            companion.initDragAndDrop(crewMembersTable, crewMembersSourceTable, Employee.class, TransportCrewMembers.class, this::includeEntityToTransport, this::removalHandler);
        }
    }

    @Override
    protected void postInit() {
        inRoute = journeyService.isTransportInJourney(getItem());

        preEditValidateOnUserCompany();
        validateOnShare();

        addUploadPhotoErrorListener();
        addClearPhotoListener();
        addTransportTypeDsItemChangeListener();
        defineTempFieldsState();
        defineADRFieldsVisibility();
        controllerAssistant.setCurrencyIcon(getItem().getCurrency(), baseAttractionField, extraAttractionField, costHourField,
                costKilometerField, costTonKilometerField, costSupplyField);
        addMaxWeightValueChangeListener();
        addTruckTypeFieldChangeListener();
        changeVisibilityFields();
        controllerAssistant.addCurrencyFieldValueChangeListener(currencyField, getItem(), baseAttractionField, extraAttractionField,
                costHourField, costKilometerField, costTonKilometerField, costSupplyField, currencyService, constantService);

        addAreaPickerFieldValueChangeListener();
        addAreaTableItemClickListener();

        initEmployeeSourceDs(driversSourceDs, EEmployeeTypeItems.driver);
        initEmployeeSourceDs(forwardersSourceDs, EEmployeeTypeItems.forwarder);
        initEmployeeSourceDs(crewMembersSourceDs, EEmployeeTypeItems.crewMember);
        initTrailersSoursDs();

        controllerAssistant.addLookupAction(companyField, "erp$Company.browse", controllerAssistant.getCompanyTransportOwnerParams());
        temperatureConditionsField.addValueChangeListener(e -> defineTempFieldsState());
        dangerousCargoField.addValueChangeListener(e -> defineADRFieldsVisibility());

        trailersTableRemove.setAfterRemoveHandler(removedItems -> removalHandler(removedItems, trailersDs, trailersSourceDs));
        driversTableRemove.setAfterRemoveHandler(removedItems -> removalHandler(removedItems, driversDs, driversSourceDs));
        forwardersTableRemove.setAfterRemoveHandler(removedItems -> removalHandler(removedItems, forwardersDs, forwardersSourceDs));
        crewMembersTableRemove.setAfterRemoveHandler(removedItems -> removalHandler(removedItems, crewMembersDs, crewMembersSourceDs));

        trailersTableRemove.setBeforeActionPerformedHandler(() -> {
            if (inRoute) {
                showNotification(messages.getMessage(getClass(), "message.cantRemoveTrailer"), NotificationType.WARNING);
                return false;
            }
            return true;
        });

        defineAdrClassValues();

        popupButton.addPopupVisibilityListener(e -> popupButton.setAutoClose(false));
        transportTypeField.removeAllActions();


    }

    private <T extends TransportEquipmentTargetEntity> void initEmployeeSourceDs(CollectionDatasource<T, UUID> sourceDs, EEmployeeTypeItems roleType) {
        Map<String, Object> params = new HashMap<>(ParamsMap.of("roleType", roleType));
        String query = "select e from erp$Employee e where e.role.type = :custom$roleType and e.isFree = true";
        if (!controllerAssistant.getCurrentUser().getIsAdmin()) {
            query += " AND e.company.id = :custom$company";
            params.put("company", controllerAssistant.getUserCompany());
        }
        sourceDs.setQuery(query);
        sourceDs.refresh(params);
    }

    private void initTrailersSoursDs() {
        ETrailerTypeItems type = truckTypeField.getValue() == ETruckTypeItems.truck ? ETrailerTypeItems.trailer : ETrailerTypeItems.semitrailer;
        Map<String, Object> params = new HashMap<>(ParamsMap.of("type", type));
        String query = "SELECT e FROM erp$Trailer e LEFT OUTER JOIN e.accesses at WHERE e.isFree = true AND e.type = :custom$type";

        Company userCompany = controllerAssistant.getUserCompany();
        if (controllerAssistant.getUserCompany() != null && !controllerAssistant.getCurrentUser().getIsAdmin()) {
            query += " AND (e.accessibleToAll = true" +
                    " OR e.company.id = :custom$userCompany" +
                    " OR at.company.id = :custom$userCompany)";
            params.put("userCompany", userCompany);
        }
        trailersSourceDs.setQuery(query);
        trailersSourceDs.refresh(params);
    }

    @Override
    protected boolean preCommit() {
        checkFreeStatusOfOldEntities();
        setItemTechnicalIndicators();
        setItemCost();
        saveAdrClassValues();

        return super.preCommit();
    }

    @Override
    protected boolean postCommit(boolean committed, boolean close) {
        if (committed) {
            CommitContext commitContext = new CommitContext();
            addTargetEntityToCommitContext(trailersDs, commitContext);
            addTargetEntityToCommitContext(driversDs, commitContext);
            addTargetEntityToCommitContext(forwardersDs, commitContext);
            addTargetEntityToCommitContext(crewMembersDs, commitContext);

            dataManager.commit(commitContext);
        }
        return super.postCommit(committed, close);
    }

    @Override
    protected void initNewItem(Transport item) {
        super.initNewItem(item);
        item.setIsFree(true);
        item.setServicePointWithoutRamp(true);
        item.setServicePointWithRamp(true);
        item.setEmployee(employeeService.getEmployeeByCurrentUser());
        item.setCurrency(currencyService.getBaseCurrency());

        Company company = controllerAssistant.getCurrentUser().getCompany();
        if (company != null && companyDS.containsItem(company.getId())) {
            item.setCompany(company);
        }

        Constant baseCostOfAttraction = constantService.getConstant("baseCostOfAttraction");
        if (baseCostOfAttraction != null) {
            item.setBaseCostAttraction(Double.valueOf(baseCostOfAttraction.getValue()));
        }
    }

    @Override
    protected void postValidate(ValidationErrors errors) {
        controllerAssistant.validateTechFields(errors, "validate.wrongMaxWeight&Capacity", getItem().getMaxWeight(), getItem().getLimitWeight(), getClass());
        controllerAssistant.validateTechFields(errors, "validate.wrongMax&MinWeight", getItem().getMinWeight(), getItem().getLimitWeight(), getClass());
        controllerAssistant.validateTechFields(errors, "validate.wrongCapacity&MinWeight", getItem().getMinWeight(), getItem().getMaxWeight(), getClass());
        controllerAssistant.validateTechFields(errors, "validate.wrongMax&MinVolume", getItem().getMinVolume(), getItem().getMaxVolume(), getClass());

        if (lowTempField.getValue() != null && highTempField.getValue() != null) {
            controllerAssistant.validateTechFields(errors, "validate.wrongTemperature", (double) getItem().getLowTemperature(), (double) getItem().getHighTemperature(), getClass());
        }

        if (dangerousCargoField.isChecked() && ((Collection<ECargoADRTypeItems>) classADRField.getValue()).isEmpty()) {
            errors.add(messages.getMessage(getClass(), "validate.classAdrEmpty"));
        }

        if (currentTransportArea != null && areaPickerField.getValue() == null){
            errors.add(messages.getMessage(getClass(), "validate.requiredPickerFieldMessage"));
        }
        super.postValidate(errors);
    }

    private void saveAdrClassValues() {
        if (dangerousCargoField.isChecked()) {
            List<ECargoADRTypeItems> selectedValues = new ArrayList<>(classADRField.getValue());
            List<TransportClassADR> itemsForRemove = classAdrDs.getItems().stream().filter(item ->
                    !selectedValues.contains(item.getClassADR())).collect(Collectors.toList()
            );
            itemsForRemove.forEach(item -> classAdrDs.removeItem(item));

            selectedValues.forEach(value -> {
                boolean isExist = false;
                for (TransportClassADR transportClassADR : classAdrDs.getItems()) {
                    if (transportClassADR.getClassADR() == value) {
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) {
                    TransportClassADR tADR = metadata.create(TransportClassADR.class);
                    tADR.setClassADR(value);
                    tADR.setTransport(getItem());
                    classAdrDs.addItem(tADR);
                }
            });
        } else {
            List<TransportClassADR> itemsForRemove = new ArrayList<>(classAdrDs.getItems());
            itemsForRemove.forEach(item -> classAdrDs.removeItem(item));
        }
    }

    private void addMaxWeightValueChangeListener() {
        maxWeightField.addValueChangeListener(e -> {
            if (getItem().getLimitWeight() == null || getItem().getLimitWeight() == 0) {
                getItem().setLimitWeight(getItem().getMaxWeight());
            }
        });
    }

    private void addTruckTypeFieldChangeListener() {
        truckTypeField.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                initTrailersSoursDs();
                changeVisibilityFields();
                setDefaultValuesToInvisibleFields();

                List<TransportTrailers> transportTrailers = new ArrayList<>(trailersDs.getItems());
                transportTrailers.forEach(tr -> {
                    tr.getTrailer().setIsFree(true);
                    trailersDs.removeItem(tr);
                });
            }
        });
    }

    private void addTransportTypeDsItemChangeListener() {
        transportTypesDs.addItemChangeListener(e -> {
            TransportType transportType = e.getItem();
            if (transportType != null && !transportType.isRoot()) {
                popupButton.setAutoClose(true);
                transportTypeField.setValue(transportType);
            }
        });
    }

    private void addUploadPhotoErrorListener() {
        uploadField.addFileUploadErrorListener(e ->
                showNotification(messages.getMessage(getClass(), "message.photoLoadError"), NotificationType.ERROR));
    }

    private void addClearPhotoListener() {
        uploadField.addAfterValueClearListener(event -> transportDs.getItem().setPhoto(null));
    }

    private void setCurrencyFieldRequired() {
        if (getItem().getCostHour() == 0 && getItem().getCostKilometer() == 0 && getItem().getCostSupply() == 0) {
            currencyField.setRequired(false);
        } else {
            currencyField.setRequired(true);
        }
    }

    private void defineTempFieldsState() {
        boolean condition = temperatureConditionsField.getValue() != null && temperatureConditionsField.getValue().equals(true);
        highTempField.setRequired(condition);
        lowTempField.setRequired(condition);
        temperatureHbox.setVisible(condition);

        if (!condition) {
            getItem().setTemperatureRetentionTime(null);
            getItem().setLowTemperature(null);
            getItem().setHighTemperature(null);
        }
    }

    private void defineADRFieldsVisibility() {
        classADRField.setVisible(dangerousCargoField.isChecked());
    }

    private void defineAdrClassValues() {
        List<ECargoADRTypeItems> cargoADRTypeItems = new ArrayList<>();
        classAdrDs.getItems().forEach(item -> cargoADRTypeItems.add(item.getClassADR()));
        classADRField.setValue(cargoADRTypeItems);
    }

    private void checkFreeStatusOfOldEntities() {
        Transport transportFromServer = dataManager.load(LoadContext.create(Transport.class).setId(getItem().getId()).setView("transport-edit-view"));
        if (transportFromServer != null) {
            checkFreeStatusOfOldSubEntities(transportFromServer.getTransportTrailers(), trailersDs);
            checkFreeStatusOfOldSubEntities(transportFromServer.getTransportDrivers(), driversDs);
            checkFreeStatusOfOldSubEntities(transportFromServer.getTransportForwarders(), forwardersDs);
            checkFreeStatusOfOldSubEntities(transportFromServer.getTransportCrewMembers(), crewMembersDs);
        }
    }

    private <T extends OrderedTableEntity> void checkFreeStatusOfOldSubEntities(List<T> entitiesFromServer,
                                                                                CollectionDatasource<T, UUID> entityDs) {
        if (entitiesFromServer != null) {
            Iterator<T> iterator = entitiesFromServer.iterator();
            while (iterator.hasNext()) {
                OrderedTableEntity orderedEntity = iterator.next();
                for (OrderedTableEntity currentItem : entityDs.getItems()) {
                    if (currentItem.getTargetEntity().equals(orderedEntity.getTargetEntity())) {
                        iterator.remove();
                        break;
                    }
                }
            }
            entitiesFromServer.forEach(entity -> {
                entity.getTargetEntity().setIsFree(true);
                dataManager.commit(entity.getTargetEntity());
            });
        }
    }

    private <T extends TransportEquipmentTargetEntity, K extends OrderedTableEntity> void addTargetEntityToCommitContext(CollectionDatasource<K, UUID> ds,
                                                                                                                         CommitContext commitContext) {
        ds.getItems().forEach(item -> {
            TransportEquipmentTargetEntity entity = item.getTargetEntity();
            entity.setIsFree(false);
            commitContext.addInstanceToCommit(entity);
        });
    }


    private <T extends TransportEquipmentTargetEntity, K extends OrderedTableEntity> void preRemovalHandler(Set<K> removedItems,
                                                                                                            CollectionDatasource<K, UUID> ds,
                                                                                                            CollectionDatasource<T, UUID> sourceDs) {
        if (inRoute) {
            showNotification(messages.getMessage(getClass(), "message.cantRemoveTrailer"), NotificationType.WARNING);
        } else {
            removalHandler(removedItems, ds, sourceDs);
        }
    }

    private <T extends TransportEquipmentTargetEntity, K extends OrderedTableEntity> void removalHandler(Set<K> removedItems,
                                                                                                         CollectionDatasource<K, UUID> ds,
                                                                                                         CollectionDatasource<T, UUID> sourceDs) {
        for (K removedItem : removedItems) {
            ds.removeItem(removedItem);

            TransportEquipmentTargetEntity entity = removedItem.getTargetEntity();
            entity.setIsFree(true);
            sourceDs.includeItem((T) entity);
        }

        // change position of remaining entities
        int order = 1;
        for (OrderedTableEntity item : ds.getItems()) {
            item.setOrder(order);
            order++;
        }
    }

    private <T extends TransportEquipmentTargetEntity, K extends OrderedTableEntity> void includeEntityToTransport(CollectionDatasource<K, UUID> entityDs,
                                                                                                                   CollectionDatasource<T, UUID> entitySourceDs,
                                                                                                                   TransportEquipmentTargetEntity entity,
                                                                                                                   Class<K> wrapperClass) {
        K orderedEntity = metadata.create(wrapperClass);
        orderedEntity.setOrder(entityDs.size() + 1);
        orderedEntity.setTransport(getItem());
        orderedEntity.setTargetEntity(entity);
        entity.setIsFree(false);
        entityDs.addItem(orderedEntity);
        entitySourceDs.excludeItem((T) entity);
    }

    public void onIncludeTrailerToTransport() {
        includeEntityToTransport(trailersDs, trailersSourceDs, trailersSourceDs.getItem(), TransportTrailers.class);
    }

    public void onIncludeDriverToTransport() {
        includeEntityToTransport(driversDs, driversSourceDs, driversSourceDs.getItem(), TransportDrivers.class);
    }

    public void onIncludeForwarderToTransport() {
        includeEntityToTransport(forwardersDs, forwardersSourceDs, forwardersSourceDs.getItem(), TransportForwarders.class);
    }

    public void onIncludeCrewMemberToTransport() {
        includeEntityToTransport(crewMembersDs, crewMembersSourceDs, crewMembersSourceDs.getItem(), TransportCrewMembers.class);
    }

    private Lookup.Handler transportTypeLookupHandler = items -> {
        if (items.size() == 1) {
            for (Object item : items) {
                TransportType transportType = (TransportType) item;
                getItem().setTransportType(transportType);
            }
        }
    };

    public void onEditTransportType() {
        openLookup(TransportType.class, transportTypeLookupHandler, WindowManager.OpenType.DIALOG);
    }

    public void onRemoveTransportType() {

        transportTypesDs.setItem(null);
        getItem().setTransportType(null);
    }

    public void onAddTrailer() {
        ETrailerTypeItems type = truckTypeField.getValue() == ETruckTypeItems.truck ? ETrailerTypeItems.trailer : ETrailerTypeItems.semitrailer;

        Map<String, Object> params = ParamsMap.of("type", type, "isFree", true);
        onAddTargetEntity(trailersDs, trailersSourceDs, TransportTrailers.class, "erp$Trailer.lookup", params);
    }

    public void onAddDriver() {
        Map<String, Object> params = ParamsMap.of("type", EEmployeeTypeItems.driver, "isFree", true);
        onAddTargetEntity(driversDs, driversSourceDs, TransportDrivers.class, "erp$Employee.browse", params);
    }

    public void onAddForwarder() {
        Map<String, Object> params = ParamsMap.of("type", EEmployeeTypeItems.forwarder, "isFree", true);
        onAddTargetEntity(forwardersDs, forwardersSourceDs, TransportForwarders.class, "erp$Employee.browse", params);
    }

    public void onAddCrewMembers() {
        Map<String, Object> params = ParamsMap.of("type", EEmployeeTypeItems.crewMember, "isFree", true);
        onAddTargetEntity(crewMembersDs, crewMembersSourceDs, TransportCrewMembers.class, "erp$Employee.browse", params);
    }

    public Component generateAttractionCurrencyField(Datasource datasource, String fieldId) {
        CurrencyField currencyField = componentsFactory.createComponent(CurrencyField.class);
        currencyField.setDatasource(datasource, fieldId);
        if (fieldId.equals("baseCostAttraction")) {
            baseAttractionField = currencyField;
            baseAttractionField.setEditable(false);
        } else if (fieldId.equals("extraCostAttraction")) {
            extraAttractionField = currencyField;
        }
        return currencyField;
    }

    public <T extends TransportEquipmentTargetEntity, K extends OrderedTableEntity> void onAddTargetEntity(CollectionDatasource<K, UUID> entityDs,
                                                                                                           CollectionDatasource<T, UUID> entitySourceDs,
                                                                                                           Class<K> wrapperClass,
                                                                                                           String screenId,
                                                                                                           Map<String, Object> params) {
        Lookup.Handler handler = items -> {
            for (Object o : items) {
                T entity = (T) o;
                boolean exist = entityDs.getItems().stream().anyMatch(c -> c.getTargetEntity().equals(entity));
                if (!exist) {
                    includeEntityToTransport(entityDs, entitySourceDs, entity, wrapperClass);
                }
            }
        };
        openLookup(screenId, handler, WindowManager.OpenType.DIALOG, params);
    }

    public void openEditForm(Entity item, String columnId) {
        ExtUser currentUser = controllerAssistant.getCurrentUser();
        if (getItem().getCompany() != null) {
            if (getItem().getCompany().equals(currentUser.getCompany())) {

                OrderedTableEntity orderedTableEntity = (OrderedTableEntity) item;
                openEditor(orderedTableEntity.getTargetEntity(), WindowManager.OpenType.DIALOG);
            }
        }
    }

    //item value setters region

    private void setItemTechnicalIndicators() {
        double bodyH = getItem().getBodyHeight();
        double truckH = getItem().getTransportHeight();
        double cargoMaxWeight = getItem().getMaxWeight();
        double cargoMaxVolume = getItem().getMaxVolume();

        double maxHeight = bodyH > truckH ? bodyH : truckH;
        double weight = getItem().getEmptyTruckWeight();
        double length = getItem().getTransportLength();
        double maxWidth = getItem().getBodyWidth();

        for (TransportTrailers trailer : trailersDs.getItems()) {
            weight += trailer.getTrailer().getEmptyTrailerWeight();
            length += trailer.getTrailer().getLength();
            cargoMaxWeight += trailer.getTrailer().getMaxWeight();
            cargoMaxVolume += trailer.getTrailer().getMaxVolume();


            double w = trailer.getTrailer().getWidth();
            if (w > maxWidth) {
                maxWidth = w;
            }

            double h = trailer.getTrailer().getHeight();
            if (h > maxHeight) {
                maxHeight = h;
            }
        }
        getItem().setTotalEmptyTransportWeight(weight);
        getItem().setTotalTransportLength(length);
        getItem().setTotalTransportWidth(maxWidth);
        getItem().setTotalTransportHeight(maxHeight);
        getItem().setTotalMaxCargoWeight(cargoMaxWeight);
        getItem().setTotalMaxCargoVolume(cargoMaxVolume);
    }

    private void setItemCost() {
        if (getItem().getCostTonKilometer() == 0) {
            getItem().setCostTonKilometer(getItem().getCostKilometer() / getItem().getMaxWeight());
        }
        double costKm = getItem().getCostKilometer();
        double costTonKm = getItem().getCostTonKilometer();
        double costSupply = getItem().getCostSupply();
        double costHour = getItem().getCostHour();
        double baseCostAttraction = getItem().getBaseCostAttraction();
        double extraCostAttraction = getItem().getExtraCostAttraction();

        if (getItem().getCurrency() != null) {
            for (TransportTrailers transportTrailer : trailersDs.getItems()) {
                Trailer trailer = transportTrailer.getTrailer();
                Currency trailerCurrency = trailer.getCurrency();
                double convertRatio = 1;
                if (trailerCurrency != null && !trailerCurrency.equals(getItem().getCurrency())) {
                    CurrencyRate trailerCurrencyRate = currencyService.getCurrencyRate(trailerCurrency);
                    CurrencyRate truckCurrencyRate = currencyService.getCurrencyRate(getItem().getCurrency());
                    if (truckCurrencyRate == null || trailerCurrencyRate == null) {
                        return;
                    }
                    convertRatio = controllerAssistant.calcConvertCurrencyRatio(trailerCurrency.getCoefficient(), trailerCurrencyRate.getRate(),
                            getItem().getCurrency().getCoefficient(), truckCurrencyRate.getRate());
                }
                costTonKm += trailer.getCostTonKilometer() * convertRatio;
                costKm += trailer.getCostKilometer() * convertRatio;
                costSupply += trailer.getCostSupply() * convertRatio;
                costHour += trailer.getCostHour() * convertRatio;
                baseCostAttraction += trailer.getBaseCostAttraction() * convertRatio;
                extraCostAttraction += trailer.getExtraCostAttraction() * convertRatio;
            }
        }

        getItem().setTotalCostTonKilometer(costTonKm);
        getItem().setTotalCostKilometer(costKm);
        getItem().setTotalCostSupply(costSupply);
        getItem().setTotalCostHour(costHour);
        getItem().setTotalBaseCostAttraction(baseCostAttraction);
        getItem().setTotalExtraCostAttraction(extraCostAttraction);

    }
    //end region

    private void changeVisibilityFields() {
        boolean visible = ETruckTypeItems.truck.equals(getItem().getTruckType());
        technicalParamsGBox.setVisible(visible);
        temperatureParamsGBox.setVisible(visible);
        downloadTypeField.setVisible(visible);
        transportTypeHbox.setVisible(visible);
        transportTypeField.setRequired(visible);
    }

    private void setDefaultValuesToInvisibleFields() {
        boolean visible = ETruckTypeItems.truck.equals(getItem().getTruckType());
        getItem().setMinVolume(null);
        getItem().setMaxVolume(null);
        getItem().setMinWeight(null);
        getItem().setMaxWeight(null);
        getItem().setLimitWeight(null);
        getItem().setHighTemperature(null);
        getItem().setLowTemperature(null);
        getItem().setTemperatureRetentionTime(null);
        getItem().setServicePointWithRamp(visible);
        getItem().setServicePointWithoutRamp(visible);
        getItem().setTemperatureConditions(false);
        getItem().setBodyHeight(null);
        getItem().setBodyLength(null);
        getItem().setBodyWidth(null);
        getItem().setTransportType(null);
        getItem().setDownloadType(null);
    }

    /**
     * Open new window for managed Access between Transport and Companies,
     * And connect addCloseWithCommitListener for follow to the changes after button click to save.
     * <p>
     * In listener checks new List<TransportsAccess> with some changes and save to transportAccessDs datasource,
     * if it`s size > 0 change availableForAll=false and availableToOwner=false.
     */
    public void openMachinesAccessEditWindow() {
        Transport transport = getItem();
        List<TransportsAccess> transportsAccesses = new ArrayList<>(transportsAccessDs.getItems());
        List<Company> collect = transportsAccessDs.getItems().stream().map(TransportsAccess::getCompany).collect(Collectors.toList());

        TransportsAccessEdit abstractWindow = (TransportsAccessEdit) openWindow("transportsAccessEditor", WindowManager.OpenType.THIS_TAB,
                ParamsMap.of("company", collect,
                        "transportsAccesses", transportsAccesses,
                        "transport", transport));
        abstractWindow.addCloseWithCommitListener(() -> {
            List<TransportsAccess> accesses = new ArrayList<>(transportsAccessDs.getItems());

            accesses.forEach(elem -> {
                if (!abstractWindow.getTransportsAccesses().contains(elem)) {
                    transportsAccessDs.removeItem(elem);
                }
            });
            abstractWindow.getTransportsAccesses().forEach(elem -> {
                if (transportsAccessDs.getItems().stream().noneMatch(transportsAccess -> transportsAccess.getCompany().equals(elem.getCompany()))) {
                    transportsAccessDs.addItem(elem);
                }
            });

            getItem().setAvailableForAll(false);
            getItem().setAvailableForOwner(transportsAccessDs.size() == 0);
            validateOnShare();
        });
    }

    /**
     * Before edit check user`s company and transport`s company and if they different changes some params:
     * setEditable(false);  setVisible(false);
     * <p>
     * Change transport can only Employee from it`s company.
     */
    private void preEditValidateOnUserCompany() {
        Company company = controllerAssistant.getCurrentUser().getCompany();
        if ((company == null || (getItem().getCompany() != null && !company.equals(getItem().getCompany())))
                && !controllerAssistant.getCurrentUser().getIsAdmin()) {
            basicFieldGroup.setEditable(false);
            basicFieldGroup_1.setEditable(false);

            truckTypeField.setVisible(false);
            identNumber.setVisible(false);
            companyPickerField.setVisible(false);
            transportManager.setVisible(false);
            vinCodeField.setVisible(false);
            sensorCodeField.setVisible(false);

            technicalParamsFieldGroup.setEditable(false);
            technicalParamsFieldGroup_2.setEditable(false);
            technicalParamsFieldGroup_4.setEditable(false);
            technicalParamsFieldGroup_5.setEditable(false);
            ADR_field_group.setEditable(false);

            actionsButtonsPanel.setVisible(false);
            shareButtonsPanel.setVisible(false);
            trailersButtons.setVisible(false);
            cancelButton.setVisible(true);
            transportTypeField.setEditable(false);
            popupButton.setVisible(false);
            editTrTypeBtn.setVisible(false);
            removeTrTypeBtn.setVisible(false);

            trailersTable.setEditable(false);
            trailersSourceTable.setVisible(false);

            otherCrewMembersTab.setVisible(false);
            photoTab.setVisible(false);

            currencyField.setEditable(false);
            costKilometerField.setEditable(false);
            costTonKilometerField.setEditable(false);
            costHourField.setEditable(false);
            costSupplyField.setEditable(false);
        }
    }

    /**
     * Button action: make Share to All companies or only Owner and changes buttons captions.
     */
    public void onShareToAll() {
        getItem().setAvailableForAll(!getItem().getAvailableForAll());
        getItem().setAvailableForOwner(!getItem().getAvailableForAll());
        clearTransportAccessDs();
        validateOnShare();
    }

    /**
     * For check @AvailableForAll and @AvailableForOwner params and set Caption to buttons.
     */
    private void validateOnShare() {
        String captionMsg = getItem().getAvailableForAll() ? "shareToOwner" : "shareToAll";
        shareToAll.setCaption(messages.getMessage(getClass(), captionMsg));
    }

    /**
     * Check transportAccess datasource and if more then 0 - cleans all.
     */
    private void clearTransportAccessDs() {
        if (transportsAccessDs.size() > 0) {
            ArrayList<TransportsAccess> items = new ArrayList<>(transportsAccessDs.getItems());
            items.forEach(transportsAccessDs::removeItem);
        }
    }

    /**
     * Button action. Will active if will edit transport with not user`s company.
     */
    public void onCancel() {
        close(Window.CLOSE_ACTION_ID);
    }


    //actions with areas
    public void onSaveArea() {
        if (areasNoneMatch(areaPickerField.getValue()) || areaPickerField.getValue().equals(currentTransportArea.getPolygon())) {
            updateTransportArea(currentTransportArea);
            clearCategoryFields();
            currentTransportArea = null;
            areaPickerField.setRequired(false);
            setButtonsVisibility(true);
        } else {
            showNotification(messages.getMessage(getClass(), "message.areasExist"));
        }
    }

    public void onAddArea() {
        if (areasNoneMatch(areaPickerField.getValue())) {
            TransportArea transportArea = metadata.create(TransportArea.class);
            transportArea = updateTransportArea(transportArea);
            categoryAreasDs.addItem(transportArea);
            clearCategoryFields();
        } else {
            showNotification(messages.getMessage(getClass(), "message.areasExist"));
        }
    }

    private void addAreaTableItemClickListener(){
        areasTable.setItemClickAction(new BaseAction("doubleClick").withHandler(e -> {
            areaPickerField.setRequired(true);
            currentTransportArea = areasTable.getSingleSelected();
            areaPickerField.setValue(currentTransportArea.getPolygon());
            areaHourTextField.setValue(currentTransportArea.getCostHour());
            areaKilometerTextField.setValue(currentTransportArea.getCostKilometer());
            areaEntranceTextField.setValue(currentTransportArea.getCostEntrancePenalty());
            areaExitTextField.setValue(currentTransportArea.getCostExitPenalty());
            areaSupplyTextField.setValue(currentTransportArea.getCostSupply());
            setButtonsVisibility(false);
        }));
    }

    private void addAreaPickerFieldValueChangeListener(){
        areaPickerField.addValueChangeListener(e -> {
            addAreaBtn.setEnabled(e.getValue() != null);
            saveAreaBtn.setEnabled(e.getValue() != null);
        });
    }

    private TransportArea updateTransportArea(TransportArea transportArea) {
        transportArea.setTransport(getItem());
        transportArea.setPolygon(areaPickerField.getValue());
        transportArea.setCostHour(areaHourTextField.getValue());
        transportArea.setCostKilometer(areaKilometerTextField.getValue());
        transportArea.setCostSupply(areaSupplyTextField.getValue());
        transportArea.setCostEntrancePenalty(areaEntranceTextField.getValue());
        transportArea.setCostExitPenalty(areaExitTextField.getValue());
        return transportArea;
    }

    private boolean areasNoneMatch(PolygonMap polygonMap){
        return categoryAreasDs.getItems().stream().noneMatch(categoryArea -> categoryArea.getPolygon().equals(polygonMap));
    }

    private void clearCategoryFields() {
        areaPickerField.setValue(null);
        areaHourTextField.setValue(null);
        areaKilometerTextField.setValue(null);
        areaSupplyTextField.setValue(null);
        areaExitTextField.setValue(null);
        areaEntranceTextField.setValue(null);
    }

    private void setButtonsVisibility(boolean isSaveAction) {
        addAreaBtn.setVisible(isSaveAction);
        saveAreaBtn.setVisible(!isSaveAction);
    }
}