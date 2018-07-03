package com.planarry.erp.web.trailer;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.planarry.erp.entity.*;
import com.planarry.erp.service.ConstantService;
import com.planarry.erp.service.CurrencyService;
import com.planarry.erp.service.EmployeeService;
import com.planarry.erp.web.trailer.access.TrailerAccessible;
import com.planarry.erp.web.utils.ControllerAssistant;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class TrailerEdit extends AbstractEditor<Trailer> {

    @Inject
    private CollectionDatasource<AccessToTrailer, UUID> accessesDs;

    @Inject
    private CollectionDatasource<Company, UUID> companyDS;

    @Named("basicFieldGroup.employee")
    private PickerField managerField;

    @Named("basicFieldGroup.identNumber")
    private TextField identNumberField;

    @Named("technicalParamsFieldGroup_4.temperatureConditions")
    private CheckBox temperatureConditionsField;

    @Named("technicalParamsFieldGroup_5.temperatureRetentionTime")
    private TextField temperatureRetentionTimeField;

    @Named("highTempField")
    private TextField highTempField;

    @Named("lowTempField")
    private TextField lowTempField;

    @Named("technicalParamsFieldGroup.maxWeight")
    private TextField maxWeightField;

    @Named("costHourField")
    private CurrencyField costHourField;

    @Named("costKilometerField")
    private CurrencyField costKilometerField;

    @Named("costTonKilometerField")
    private CurrencyField costTonKilometerField;

    @Named("costSupplyField")
    private CurrencyField costSupplyField;

    @Named("basicFieldGroup.company")
    private LookupPickerField companyField;

    @Named("currencyField")
    private LookupPickerField currencyField;

    @Named("basicFieldGroup")
    private FieldGroup basicFieldGroup;

    @Named("technicalParamsFieldGroup")
    private FieldGroup technicalParamsFieldGroup;

    @Named("shareToAllBtn")
    private Button shareToAllBtn;

    @Named("shareToCompaniesBtn")
    private Button shareToCompaniesBtn;

    @Named("closeBtn")
    private Button closeBtn;

    @Named("windowActions")
    private Frame windowActions;

    @Named("temp_hbox")
    private HBoxLayout temperatureHbox;

    @Named(CurrencyService.NAME)
    private CurrencyService currencyService;

    @Named(EmployeeService.NAME)
    private EmployeeService employeeService;

    @Named(ConstantService.NAME)
    private ConstantService constantService;

    @Named(ComponentsFactory.NAME)
    private ComponentsFactory componentsFactory;

    private CurrencyField baseAttractionField;

    private CurrencyField extraAttractionField;

    @Named(ControllerAssistant.NAME)
    private ControllerAssistant controllerAssistant;

    @Override
    public void init(Map<String, Object> params) {
        controllerAssistant.addLookupAction(managerField, "erp$Employee.browse",
                ParamsMap.of("role", UUID.fromString("7c36da70-e965-4274-8486-78d15d3fad6a")));
        super.init(params);
        controllerAssistant.initCompanyDsByActivitiesType(companyDS, controllerAssistant.getCompanyTransportOwnerParams());
    }

    @Override
    protected void initNewItem(Trailer item) {
        super.initNewItem(item);
        item.setServicePointWithoutRamp(true);
        item.setServicePointWithRamp(true);
        item.setIsFree(true);
        item.setEmployee(employeeService.getEmployeeByCurrentUser());
        item.setCurrency(currencyService.getBaseCurrency());
        item.setAccessibleToOwner(true);

        Company company = controllerAssistant.getCurrentUser().getCompany();
        if (company != null && companyDS.containsItem(company.getId())) {
            item.setCompany(company);
        }

        Currency currency = currencyService.getBaseCurrency();
        item.setCurrency(currency);

        Constant baseCostOfAttraction = constantService.getConstant("baseCostOfAttraction");
        if (baseCostOfAttraction != null && currency != null) {
            item.setBaseCostAttraction(Double.valueOf(baseCostOfAttraction.getValue()));
        }
    }

    @Override
    protected void postInit() {
        defineTempFieldsState();
        defineShareBtnCaption();
        controllerAssistant.setCurrencyIcon(getItem().getCurrency(), baseAttractionField, extraAttractionField, costHourField,
                costKilometerField, costTonKilometerField, costSupplyField);
        addMaxWeightValueChangeListener();
        temperatureConditionsField.addValueChangeListener(e -> defineTempFieldsState());
        controllerAssistant.addLookupAction(companyField, "erp$Company.browse", controllerAssistant.getCompanyTransportOwnerParams());
        controllerAssistant.addCurrencyFieldValueChangeListener(currencyField, getItem(), baseAttractionField, extraAttractionField,
                costHourField, costKilometerField, costTonKilometerField, costSupplyField, currencyService, constantService);
        hideFields();
        super.postInit();
    }

    @Override
    protected void postValidate(ValidationErrors errors) {
        if (lowTempField.getValue() != null && highTempField.getValue() != null) {
            controllerAssistant.validateTechFields(errors, "validate.wrongTemperature", (double) getItem().getLowTemperature(), (double) getItem().getHighTemperature(), getClass());
        }
        controllerAssistant.validateTechFields(errors, "validate.wrongMaxWeight&Capacity", getItem().getMaxWeight(), getItem().getLimitWeight(), getClass());
        controllerAssistant.validateTechFields(errors, "validate.wrongMax&MinWeight", getItem().getMinWeight(), getItem().getLimitWeight(), getClass());
        controllerAssistant.validateTechFields(errors, "validate.wrongCapacity&MinWeight", getItem().getMinWeight(), getItem().getMaxWeight(), getClass());
        controllerAssistant.validateTechFields(errors, "validate.wrongMax&MinVolume", getItem().getMinVolume(), getItem().getMaxVolume(), getClass());
        super.postValidate(errors);
    }

    @Override
    protected boolean preCommit() {
        if (getItem().getCostTonKilometer() == 0){
            getItem().setCostTonKilometer(getItem().getCostKilometer() / getItem().getMaxWeight());
        }
        return super.preCommit();
    }

    private void defineTempFieldsState() {
        boolean checked = temperatureConditionsField.isChecked();
        if (!checked) {
            getItem().setTemperatureRetentionTime(null);
            getItem().setLowTemperature(null);
            getItem().setHighTemperature(null);
        }
        highTempField.setRequired(checked);
        lowTempField.setRequired(checked);
        temperatureHbox.setVisible(checked);
    }

    private void addMaxWeightValueChangeListener() {
        maxWeightField.addValueChangeListener(e -> {
            if (getItem().getLimitWeight() == null || getItem().getLimitWeight() == 0) {
                getItem().setLimitWeight(getItem().getMaxWeight());
            }
        });
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

    public void onShareToAll() {
        clearAccessDS();
        getItem().setAccessibleToAll(!getItem().getAccessibleToAll());
        getItem().setAccessibleToOwner(!getItem().getAccessibleToAll());
        defineShareBtnCaption();
    }

    public void onShareToCompanies() {
        List<AccessToTrailer> accesses = new ArrayList<>(accessesDs.getItems());
        List<Company> companies = accesses.stream().map(AccessToTrailer::getCompany).collect(Collectors.toList());
        Map<String, Object> params = ParamsMap.of("trailer", getItem(), "accesses", accesses, "companies", companies);
        TrailerAccessible window = (TrailerAccessible) openWindow("trailerAccessible", WindowManager.OpenType.DIALOG, params);
        window.addCloseWithCommitListener(() -> {
            List<AccessToTrailer> accessToCargoes = new ArrayList<>(accessesDs.getItems());
            accessToCargoes.forEach(item -> {
                if (!window.getNewAccesses().contains(item)) {
                    accessesDs.removeItem(item);
                }
            });
            window.getNewAccesses().forEach(item -> {
                if (accessesDs.getItems().stream().noneMatch(accessToTrailer -> accessToTrailer.getCompany().equals(item.getCompany()))) {
                    accessesDs.addItem(item);
                }
            });

            getItem().setAccessibleToOwner(accessesDs.size() == 0);
            getItem().setAccessibleToAll(false);
            defineShareBtnCaption();
        });
    }

    public void onClose() {
        close(Window.CLOSE_ACTION_ID, true);
    }

    private void defineShareBtnCaption() {
        String captionMsg = getItem().getAccessibleToAll() ? "unshare" : "shareToAll";
        shareToAllBtn.setCaption(messages.getMessage(getClass(), captionMsg));
    }

    private void clearAccessDS() {
        if (accessesDs.size() > 0) {
            List<AccessToTrailer> items = new ArrayList<>(accessesDs.getItems());
            items.forEach(accessesDs::removeItem);
        }
    }

    private void hideFields() {
        Company company = controllerAssistant.getCurrentUser().getCompany();
        if ((company == null || (getItem().getCompany() != null && !company.equals(getItem().getCompany())))
                && !controllerAssistant.getCurrentUser().getIsAdmin()) {
            basicFieldGroup.setEditable(false);
            companyField.setVisible(false);
            managerField.setVisible(false);
            identNumberField.setVisible(false);
            technicalParamsFieldGroup.setEditable(false);
            temperatureConditionsField.setEditable(false);
            temperatureRetentionTimeField.setEditable(false);
            lowTempField.setEditable(false);
            highTempField.setEditable(false);
            shareToCompaniesBtn.setVisible(false);
            shareToAllBtn.setVisible(false);
            currencyField.setEditable(false);
            costHourField.setEditable(false);
            costKilometerField.setEditable(false);
            costSupplyField.setEditable(false);
            extraAttractionField.setEditable(false);
            closeBtn.setVisible(true);
            windowActions.setVisible(false);
        }
    }
}
