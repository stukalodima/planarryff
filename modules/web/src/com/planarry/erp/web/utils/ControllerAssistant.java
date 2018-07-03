package com.planarry.erp.web.utils;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.planarry.erp.entity.*;
import com.planarry.erp.entity.Currency;
import com.planarry.erp.service.ConstantService;
import com.planarry.erp.service.CurrencyService;
import com.planarry.erp.service.RouterService;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@org.springframework.stereotype.Component(ControllerAssistant.NAME)
public class ControllerAssistant {

    public static final String NAME = "erp_ControllerAssistant";

    @Inject
    private Messages messages;

    @Inject
    private ComponentsFactory componentsFactory;

    @Inject
    private ConstantService constantService;

    private ExtUser currentUser;

    private Company userCompany;

    public void init() {
        UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.class);
        DataManager dataManager = AppBeans.get(DataManager.class);
        currentUser = dataManager.reload((ExtUser) userSessionSource.getUserSession().getUser(), "extUser-with-company-view");
        userCompany = currentUser.getCompany();
    }

    public void checkBrowseParams(Map<String, Object> params) {
//        if (!currentUser.getIsAdmin()) {
//            params.put("company", userCompany);
//        }
    }

    public void addLookupAction(final PickerField field, final String screenId, final Map<String, Object> params) {
        PickerField.LookupAction lookupAction = field.addLookupAction();
        lookupAction.setLookupScreen(screenId);
        lookupAction.setLookupScreenOpenType(WindowManager.OpenType.DIALOG);
        if (params != null) {
            lookupAction.setLookupScreenParams(params);
        }
    }

    public void initCompanyDs(final CollectionDatasource<Company, UUID> companyDs) {
//        if (!currentUser.getIsAdmin()) {
//            Map<String, Object> params = new HashMap<>();
//            params.put("company", userCompany);
//            companyDs.setQuery("select e from erp$Company e where e.id = :custom$company");
//            companyDs.refresh(params);
//        }
    }

    public void initCompanyDsByActivitiesType(final CollectionDatasource<Company, UUID> companyDs, Map<String, Object> params) {
        String query = "select e from erp$Company e where e.activitiesType.id in :custom$activitiesTypeId";
//        if (!currentUser.getIsAdmin()) {
//            query += " AND e.id = :custom$company";
//            params.put("company", userCompany);
//        }
        companyDs.setQuery(query);
        companyDs.refresh(params);
    }

    public Map<String, Object> getCompanyParams() {
        Map<String, Object> params = new HashMap<>();
//        params.put("company", userCompany);
        return params;
    }

    public Map<String, Object> getCompanyTransportOwnerParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("activitiesTypeId", Arrays.asList(UUID.fromString("0133d88d-1600-4dd3-a2d6-98aa455dc0c5"),
                UUID.fromString("f5d9c3e0-51dc-4c07-99f2-183f18317bda"))); //transport_owner and forwarders_company id
        return params;
    }

    public Map<String, Object> getCompanyCargoOwnerParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("activitiesTypeId", Collections.singletonList(UUID.fromString("30c91df6-ce18-4e8b-a693-19a8a19821ba"))); //cargo_owner id
        return params;
    }

    public Map<String, Object> getCompanyCargoCreatorParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("activitiesTypeId", Arrays.asList(UUID.fromString("30c91df6-ce18-4e8b-a693-19a8a19821ba"), UUID.fromString("f5d9c3e0-51dc-4c07-99f2-183f18317bda")));
        return params;
    }

    public String makeCaption(String... args) {
        StringJoiner joiner = new StringJoiner(" ");
        for (String arg : args) {
            if (arg != null) {
                joiner.add(arg);
            }
        }
        return joiner.toString();
    }

    public int calcCoordinatesDiff(Double lat1, Double lon1, Double lat2, Double lon2) {
        return (int) Math.round(calcDifferenceBetweenCoordinates(lat1, lon1, lat2, lon2));
    }

    /**
     * Calc distance between coordinates of two points.
     */
    public Double calcDifferenceBetweenCoordinates(Double lat1, Double lon1, Double lat2, Double lon2) {
        double arg1 = Math.pow(Math.sin((Math.toRadians(lat1) - Math.toRadians(lat2)) / 2), 2);
        double arg2 = Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.pow(Math.sin((Math.toRadians(lon1) - Math.toRadians(lon2)) / 2), 2);
        double delta = Math.asin(Math.sqrt(arg1 + arg2)) * 2 * 6371000 / 1000;
        return delta;
    }

    /**
     * Calc distance from EntryPoint to Polygon side and return result for check.
     */
    public Double calcDistanceFromPointToSegment(Double lat1, Double lon1, Double lat2, Double lon2, Double latPoint, Double lonPoint) {
        Double denomSide = calcDifferenceBetweenCoordinates(lat1, lon1, lat2, lon2);
        Double sideA = calcDifferenceBetweenCoordinates(lat1, lon1, latPoint, lonPoint);
        Double sideB = calcDifferenceBetweenCoordinates(lat2, lon2, latPoint, lonPoint);

        //проверяем чтобы 2 угла были отсрыми - если так, то перпендикуляр принядлежит отрезку.
        double angleA = Math.pow(sideA, 2) + Math.pow(denomSide, 2) - Math.pow(sideB, 2);
        double angleB = Math.pow(sideB, 2) + Math.pow(denomSide, 2) - Math.pow(sideA, 2);
        if (angleA > 0 && angleB > 0) {
            Double p = (denomSide + sideA + sideB) / 2;
            Double triangleArea = Math.sqrt(p * (p - denomSide) * (p - sideA) * (p - sideB));
            Double res = 2 * triangleArea / denomSide;
            return res;
        }
        return 0.0;
    }

    public double calcConvertCurrencyRatio(double coefficientFrom, double currencyRateFrom, double coefficientTo, double currencyRateTo) {
        return (coefficientFrom / currencyRateFrom) * (currencyRateTo / coefficientTo);
    }

    public void setCurrencyIcon(Currency currency,
                                CurrencyField baseAttractionField,
                                CurrencyField extraAttractionField,
                                CurrencyField costHourField,
                                CurrencyField costKilometerField,
                                CurrencyField costTonKilometerField,
                                CurrencyField costSupplyField) {
        String shirtName = currency == null ? null : currency.getShirtName();
        extraAttractionField.setCurrency(shirtName);
        baseAttractionField.setCurrency(shirtName);
        costHourField.setCurrency(shirtName);
        costKilometerField.setCurrency(shirtName);
        costTonKilometerField.setCurrency(shirtName);
        costSupplyField.setCurrency(shirtName);
    }

    public <T extends AssistantAntity> void addCurrencyFieldValueChangeListener(LookupPickerField currencyField, T entity,
                                                                                CurrencyField baseAttractionField,
                                                                                CurrencyField extraAttractionField,
                                                                                CurrencyField costHourField,
                                                                                CurrencyField costKilometerField,
                                                                                CurrencyField costTonKilometerField,
                                                                                CurrencyField costSupplyField,
                                                                                CurrencyService currencyService,
                                                                                ConstantService constantService) {
        currencyField.addValueChangeListener(e -> {
            setCurrencyIcon(entity.getCurrency(), baseAttractionField, extraAttractionField, costHourField, costKilometerField, costTonKilometerField, costSupplyField);
            if (e.getValue() != null) {
                Currency currency = (Currency) e.getValue();
                CurrencyRate currencyRate = currencyService.getCurrencyRate(currency);
                Currency baseCurrency = currencyService.getBaseCurrency();
                CurrencyRate baseCurrencyRate = currencyService.getCurrencyRate(baseCurrency);
                if (currencyRate != null && baseCurrency != null && baseCurrencyRate != null) {
                    double convertRatio = calcConvertCurrencyRatio(baseCurrency.getCoefficient(), baseCurrencyRate.getRate(),
                            currency.getCoefficient(), currencyRate.getRate());
                    entity.setBaseCostAttraction(Double.valueOf(constantService.getConstant("baseCostOfAttraction").getValue()) * convertRatio);
                    entity.setExtraCostAttraction(entity.getExtraCostAttraction() * convertRatio);
                }
            }
        });
    }

    public void validateTechFields(ValidationErrors errors, String message, Double param_1, Double param_2, Class clazz) {
        if (param_1 != null && param_2 != null && param_1 > param_2) {
            errors.add(messages.getMessage(clazz, message));
        }
    }

    /**
     * @param params set params value for jpql query with filter;
     *               if current user is Admin - show all data from ds,
     *               if current user is not Admin - show all data from ds by user company,
     *               if Company field is null - show notification.
     * @return true if need display notification.
     */
    public boolean checkUserStatusAndCompany(Map<String, Object> params) {
        ExtUser currentUser = getCurrentUser();
        if (currentUser != null) {
            if (!currentUser.getIsAdmin() && currentUser.getCompany() == null) {
                params.put("company", UUID.fromString("d87ff090-9802-4a67-ada7-a6b0b8d7a870"));
                params.put("accessibleToAll", true);
                return true;
            } else if (!currentUser.getIsAdmin()) {
                params.put("company", getUserCompany());
                params.put("accessibleToAll", true);
            }
        }
        return false;
    }

    public <T extends AccessEntity> Component generateAccessColumn(boolean accessibleToOwner, boolean accessibleToAll, List<T> accessEntities, Frame frame,
                                                                   String ownerAccessMessageFull, String allAccessMessageFull) {
        LinkButton linkButton = componentsFactory.createComponent(LinkButton.class);
        if (accessibleToOwner) {
            linkButton.setCaption(messages.getMessage(messages.getMainMessagePack(), "ownerAccessMessageShirt"));
            linkButton.setAction(new BaseAction("showAccessibleToOwner").withHandler(e -> frame.showNotification(ownerAccessMessageFull)));
        } else if (accessibleToAll) {
            linkButton.setCaption(messages.getMessage(messages.getMainMessagePack(), "allAccessMessageShirt"));
            linkButton.setAction(new BaseAction("showAccessibleToOwner").withHandler(e -> frame.showNotification(allAccessMessageFull)));
        } else {
            linkButton.setCaption(messages.getMessage(messages.getMainMessagePack(), "companiesAccess"));
            linkButton.setAction(new BaseAction("openCompaniesList").withHandler(e -> {
                List<Company> companies = accessEntities.stream().map(AccessEntity::getCompany).collect(Collectors.toList());
                frame.openWindow("erp$CompanyAccessBrowse", WindowManager.OpenType.DIALOG, ParamsMap.of("companies", companies));
            }));
        }
        return linkButton;
    }

    public void changeEmployeeWindowCaption(Frame frame, String roleId, String transportManagerMessage, String cargoManagerMessage, Class clazz) {
        switch (roleId) {
            case "7c36da70-e965-4274-8486-78d15d3fad6a":
                frame.setCaption(messages.getMessage(clazz, transportManagerMessage));
                break;
            case "ea309298-a917-4b48-8686-85d1564436b9":
                frame.setCaption(messages.getMessage(clazz, cargoManagerMessage));
                break;
        }
    }

    public Employee updateEmployeeByUser(ExtUser user, Employee employee) {
        employee.setUser(user);
        employee.setEmail(user.getEmail());
        employee.setCompany(user.getCompany());
        employee.setFirstName(user.getFirstName());
        employee.setMiddleName(user.getMiddleName());
        employee.setLastName(user.getLastName());
        employee.setName(user.getName() == null
                ? makeCaption(user.getLastName(), user.getFirstName(), user.getMiddleName())
                : user.getName()
        );

        if (employee.getName() == null || employee.getName().isEmpty()) {
            employee.setName(user.getLogin());
        }
        return employee;
    }

    public RouterService defineRouterService(){
        if (userCompany != null) {
            if (userCompany.getRouter() == null) {
                Constant constant = constantService.getConstant("router");
                if (constant != null) {
                    String value = constant.getValue();
                    if (value.equalsIgnoreCase("osm")) {
                        return AppBeans.get("erp_OsmRouterService");
                    }
                }
            } else if (userCompany.getRouter() == ERouterType.osm) {
                return AppBeans.get("erp_OsmRouterService");
            }
        }

        return AppBeans.get("erp_DefaultRouterService");
    }

    public ExtUser getCurrentUser() {
        return currentUser;
    }

    public Company getUserCompany() {
        return userCompany;
    }
}
