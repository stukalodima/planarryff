
package com.planarry.erp.web.employee;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.planarry.erp.entity.Company;
import com.planarry.erp.entity.Employee;
import com.planarry.erp.entity.EmployeeRole;
import com.planarry.erp.entity.ExtUser;
import com.planarry.erp.service.EmployeeService;
import com.planarry.erp.web.utils.ControllerAssistant;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;
import java.util.UUID;

/**
 * @author Pavel Shynkarenko
 */
public class EmployeeEdit extends AbstractEditor<Employee> {

    @Inject
    private CollectionDatasource<Company, UUID> companiesDs;

    @Named("fieldGroup.role")
    private PickerField employeeRoleField;

    @Named("fieldGroup.user")
    private LookupPickerField userField;

    @Named("fieldGroup.firstName")
    private TextField firstNameField;

    @Named("fieldGroup.lastName")
    private TextField lastNameField;

    @Named("fieldGroup.middleName")
    private TextField middleNameField;

    @Named(DataManager.NAME)
    private DataManager dataManager;

    @Named(ControllerAssistant.NAME)
    private ControllerAssistant controllerAssistant;

    @Named(EmployeeService.NAME)
    private EmployeeService employeeService;

    private String roleId;

    private String caption;

    @Override
    public void init(Map<String, Object> params) {
        if (params.containsKey("role")) {
            controllerAssistant.addLookupAction(employeeRoleField, "erp$EmployeeRole.browse", params);

            roleId = params.get("role").toString();
            controllerAssistant.changeEmployeeWindowCaption(getFrame(), roleId,
                    "editorTransportManagerCaption",  "editorCargoManagerCaption", getClass());
        }

        super.init(params);
        controllerAssistant.initCompanyDs(companiesDs);
    }

    @Override
    protected void postInit() {
        super.postInit();

        userField.addValueChangeListener(e -> {
            if (e.getValue() != null){
                ExtUser extUser = dataManager.reload(userField.getValue(), "extUser-view");
                controllerAssistant.updateEmployeeByUser(extUser, getItem());
            }
        });

        caption = makeCaption();
        firstNameField.addValueChangeListener(e -> captionChangeHandler());
        lastNameField.addValueChangeListener(e -> captionChangeHandler());
        middleNameField.addValueChangeListener(e -> captionChangeHandler());
    }

    @Override
    protected void postValidate(ValidationErrors errors) {
        Employee existingEmployee = employeeService.getEmployeeByUser((ExtUser) getItem().getUser());
        if (existingEmployee != null && !getItem().equals(existingEmployee)) {
            errors.add(messages.getMessage(getClass(), "message.userUsed"));
        }
        super.postValidate(errors);
    }

    @Override
    protected void initNewItem(Employee item) {
        super.initNewItem(item);
        item.setIsFree(true);
        item.setCompany(controllerAssistant.getUserCompany());

        if (roleId != null){
            EmployeeRole employeeRole = employeeService.getEmployeeRoleById(roleId);
            item.setRole(employeeRole);
        }

        // fill user_field by current user, if current user does not has an employee
        if (employeeService.getEmployeeByCurrentUser() == null){
            controllerAssistant.updateEmployeeByUser(controllerAssistant.getCurrentUser(), item);
        }
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
}