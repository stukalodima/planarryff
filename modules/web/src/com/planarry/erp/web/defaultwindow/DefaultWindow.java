package com.planarry.erp.web.defaultwindow;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Accordion;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.security.global.UserSession;
import com.planarry.erp.entity.Cargo;
import com.planarry.erp.entity.ExtUser;
import com.planarry.erp.entity.Journey;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

public class DefaultWindow extends AbstractWindow {

    @Named("accordionTM")
    private Accordion accordionTM;

    @Named(Metadata.NAME)
    private Metadata metadata;

    @Inject
    private UserSession userSession;

    private boolean isTariffCalculatorRole;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        defineMainTabsVisibility();
    }

    private void defineMainTabsVisibility(){
        ExtUser user = (ExtUser) userSession.getUser();
        List<UserRole> userRoles = user.getUserRoles();
        isTariffCalculatorRole = userRoles.stream().anyMatch(
                userRole -> userRole.getRole().getId().toString().equals("8ee6a0d1-e403-4242-8dca-0bfe180ab583") //тарифный калькулятор
        );

        if (user.getIsAdmin() || isTariffCalculatorRole){
            accordionTM.getTabs().forEach(item -> item.setVisible(true));
        } else {
            for(UserRole userRole : userRoles){
                switch (userRole.getRole().getId().toString()){
                    case "9ba52dcb-911d-4561-b4ae-d62b8ece8f5a": //менеджер транспорта
                        setTabVisibility("tabTransports", true);
                        break;
                    case "0ee4a7d6-33fb-4463-82f9-846ea68c73ac": //менеджер груза
                        setTabVisibility("tabCargoes", true);
                        break;
                    case "30a159e4-79cd-49f6-8626-0a186bfa847e": // логист
                        setTabVisibility("tabJourney", true);
                        setTabVisibility("tabMaps", true);
                        break;
                    case "ba336316-3cfc-4b21-85a8-9654fadd5488": //управляющий
                        setTabVisibility("tabCurrency", true);
                        setTabVisibility("tabAdditionally", true);
                        break;
                }
            }
        }
    }

    private void setTabVisibility(String tabName, boolean visible){
        accordionTM.getTab(tabName).setVisible(visible);
    }

    private void showDialogWindow(){
        openWindow("infoWindow", WindowManager.OpenType.DIALOG);
    }

    public void onTransportListBtnClick() {
        if (isTariffCalculatorRole){
            showDialogWindow();
        } else {
            openWindow("erp$Transport.browse", WindowManager.OpenType.NEW_TAB);
        }
    }

    public void onTrailerListBtnClick() {
        if (isTariffCalculatorRole){
            showDialogWindow();
        } else {
            openWindow("erp$Trailer.browse", WindowManager.OpenType.NEW_TAB);
        }
    }

    public void onTransportPlaceBtnClick() {
        if (isTariffCalculatorRole){
            showDialogWindow();
        } else {
            openWindow("transportStateCreate", WindowManager.OpenType.NEW_TAB);
        }
    }

    public void onTransportManagersBtnClick() {
        if (isTariffCalculatorRole){
            showDialogWindow();
        } else {
            openWindow("erp$Employee.browse", WindowManager.OpenType.NEW_TAB,
                    ParamsMap.of("roleStr", "7c36da70-e965-4274-8486-78d15d3fad6a"));
        }
    }

    public void onCategoryListBtnClick() {
        openWindow("erp$Category.browse", WindowManager.OpenType.NEW_TAB);
    }

    public void onCargoCreationBtnClick() {
        openEditor("erp$Cargo.create", metadata.create(Cargo.class), WindowManager.OpenType.NEW_TAB);
    }

    public void onCargoListBtnClick() {
        openWindow("erp$Cargo.browse", WindowManager.OpenType.NEW_TAB);
    }

    public void onCargoManagersBtnClick() {
        if (isTariffCalculatorRole){
            showDialogWindow();
        } else {
            openWindow("erp$Employee.browse", WindowManager.OpenType.NEW_TAB,
                    ParamsMap.of("roleStr", "ea309298-a917-4b48-8686-85d1564436b9"));
        }
    }

    public void onJourneyListBtnClick() {
        if (isTariffCalculatorRole){
            showDialogWindow();
        } else {
            openWindow("erp$Journey.browse", WindowManager.OpenType.NEW_TAB);
        }
    }

    public void onJourneyCreationBtnClick() {
        if (isTariffCalculatorRole){
            showDialogWindow();
        } else {
            openEditor("erp$Journey.create", metadata.create(Journey.class), WindowManager.OpenType.NEW_TAB);
        }
    }

    public void onCurrencyRateBtnClick() {
        if (isTariffCalculatorRole){
            showDialogWindow();
        } else {
            openWindow("erp$CurrencyRate.browse", WindowManager.OpenType.NEW_TAB);
        }
    }

    public void onActualCurrencyRateBtnClick() {
        if (isTariffCalculatorRole){
            showDialogWindow();
        } else {
            openWindow("erp$ActualCurrencyRate.browse", WindowManager.OpenType.NEW_TAB);
        }
    }

    public void onCurrencyListBtnClick() {
        if (isTariffCalculatorRole){
            showDialogWindow();
        } else {
            openWindow("erp$Currency.browse", WindowManager.OpenType.NEW_TAB);
        }
    }

    public void onCompaniesListBtnClick() {
        if (isTariffCalculatorRole){
            showDialogWindow();
        } else {
            openWindow("erp$Company.browse", WindowManager.OpenType.NEW_TAB);
        }
    }

    public void onDepartmentBtnClick() {
        if (isTariffCalculatorRole){
            showDialogWindow();
        } else {
            openWindow("erp$Department.browse", WindowManager.OpenType.NEW_TAB);
        }
    }

    public void onEmployeeRoleListBtnClick() {
        if (isTariffCalculatorRole){
            showDialogWindow();
        } else {
            openWindow("erp$EmployeeRole.browse", WindowManager.OpenType.NEW_TAB);
        }
    }

    public void onEmployeesBtnClick() {
        if (isTariffCalculatorRole){
            showDialogWindow();
        } else {
            openWindow("erp$Employee.browse", WindowManager.OpenType.NEW_TAB);
        }
    }

    public void onMonitoringBtnClick() {
        if (isTariffCalculatorRole){
            showDialogWindow();
        } else {
            openWindow("monitoring", WindowManager.OpenType.NEW_TAB);
        }
    }

    public void onRegionListBtnClick() {
        openWindow("erp$PolygonMap.browse", WindowManager.OpenType.NEW_TAB);
    }
}