package com.planarry.erp.web.company;

import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.components.*;
import com.planarry.erp.entity.Company;
import com.planarry.erp.entity.EPersonTypeItems;
import com.planarry.erp.service.CompanyService;
import com.planarry.erp.web.utils.ControllerAssistant;
import com.planarry.erp.web.utils.RunReportBean;

import javax.inject.Named;
import java.util.ArrayList;

public class CompanyEdit extends AbstractEditor<Company> {

    @Named("fieldGroup.firstName")
    private TextField firstNameField;

    @Named("fieldGroup.lastName")
    private TextField lastNameField;

    @Named("fieldGroup.fullName")
    private TextField fullNameField;

    @Named("fieldGroup.middleName")
    private TextField middleNameField;

    @Named("fieldGroup.type")
    private LookupField typeField;

    @Named("groupBox_2")
    private GroupBoxLayout personGroupBox;

    @Named("tabSheet")
    private TabSheet tabSheet;

    @Named(ControllerAssistant.NAME)
    private ControllerAssistant controllerAssistant;

    @Named(CompanyService.NAME)
    private CompanyService companyService;

    @Named(RunReportBean.NAME)
    private RunReportBean runReportBean;

    private String caption;

    @Override
    protected void initNewItem(Company item) {
        super.initNewItem(item);
        item.setType(EPersonTypeItems.naturalPerson);
        item.setTransportSearchNarrowRadius(50);
        item.setTransportSearchWideRadius(100);

        item.setPrefix(companyService.createPrefix());
    }

    @Override
    public void ready() {
        super.ready();
        setVisibleElements();
        typeField.addValueChangeListener(e -> {
            setVisibleElements();
            resetValues();
        });
    }

    @Override
    protected void postInit() {
        super.postInit();
        checkCompanyBeforeEdit();
        caption = makeCaption();
        firstNameField.addValueChangeListener(e -> captionChangeHandler());
        lastNameField.addValueChangeListener(e -> captionChangeHandler());
        middleNameField.addValueChangeListener(e -> captionChangeHandler());
    }

    @Override
    protected void postValidate(ValidationErrors errors) {
        if (getRadiusFieldsState() && getItem().getTransportSearchNarrowRadius() > getItem().getTransportSearchWideRadius()) {
            errors.add(messages.getMessage(getClass(), "validate.radiusCompare"));
        }

        if (getRadiusFieldsState() && getItem().getTransportSearchNarrowRadius() < 1) {
            errors.add(messages.getMessage(getClass(), "validate.incorrectRadius"));
        }
        super.postValidate(errors);
    }

    @Override
    protected boolean postCommit(boolean committed, boolean close) {
        if (committed && controllerAssistant.getUserCompany().equals(getItem())){
            //re-init user company with actual router
            controllerAssistant.init();
        }
        return super.postCommit(committed, close);
    }

    private void resetValues(){
        firstNameField.setValue(null);
        lastNameField.setValue(null);
        middleNameField.setValue(null);
    }

    private void setVisibleElements() {
        boolean visibleItems = getItem().getType() == EPersonTypeItems.naturalPerson;
        firstNameField.setVisible(visibleItems);
        lastNameField.setVisible(visibleItems);
        middleNameField.setVisible(visibleItems);
        firstNameField.setRequired(visibleItems);
        lastNameField.setRequired(visibleItems);
        fullNameField.setVisible(!visibleItems);
    }

    private void captionChangeHandler(){
        if (getItem().getName() == null || getItem().getName().equals(caption)){
            caption = makeCaption();
            getItem().setName(caption);
        }
    }

    private String makeCaption(){
        return controllerAssistant.makeCaption(lastNameField.getValue(), firstNameField.getValue(), middleNameField.getValue());
    }

    private boolean getRadiusFieldsState(){
        return getItem().getTransportSearchNarrowRadius() != null && getItem().getTransportSearchWideRadius() != null;
    }

    /**
     * Check user company and edit company and if not equals will hide financial condition tab info.
     */
    private void checkCompanyBeforeEdit() {
        if (!controllerAssistant.getCurrentUser().getIsAdmin() && !getItem().equals(controllerAssistant.getUserCompany()) || PersistenceHelper.isNew(getItem())) {
            tabSheet.getTab("financialConditionTab").setVisible(false);
        }
    }

    /**
     * Run mutualSettlement report.
     * And show notification if did not load report.
     */
    public void runRapportMutualSettlement() {
        ArrayList<String> error = new ArrayList<>();
        if (!runReportBean.runReportMutualSettlement(error).isEmpty()) {
            showNotification(messages.getMessage(getClass(), "message.errorReport") + "\n" + error.get(0), NotificationType.WARNING);
        }
    }

    /**
     * Run clientCredit report.
     * And show notification if did not load report.
     */
    public void runRapportClientCredit() {
        ArrayList<String> error = new ArrayList<>();
        if (!runReportBean.runReportClientCredit(error).isEmpty()) {
            showNotification(messages.getMessage(getClass(), "message.errorReport") + error.get(0), NotificationType.WARNING);
        }
    }
}
