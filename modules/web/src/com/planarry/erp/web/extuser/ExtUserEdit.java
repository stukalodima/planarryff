
package com.planarry.erp.web.extuser;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.app.security.user.edit.UserEditor;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.planarry.erp.entity.Company;
import com.planarry.erp.entity.Employee;
import com.planarry.erp.entity.EmployeeRole;
import com.planarry.erp.entity.ExtUser;
import com.planarry.erp.service.EmployeeService;
import com.planarry.erp.web.utils.ControllerAssistant;

import javax.inject.Named;
import java.util.UUID;

public class ExtUserEdit extends UserEditor {


    @Named("companiesDs")
    private CollectionDatasource<Company, UUID> companiesDs;

    @Named("fieldGroupLeft.company")
    private LookupPickerField companyField;

    @Named("fieldGroupRight.isAdmin")
    private CheckBox adminCheckBox;

    @Named("createManagerBtn")
    private Button createManagerBtn;

    @Named(ControllerAssistant.NAME)
    private ControllerAssistant controllerAssistant;

    @Named(EmployeeService.NAME)
    private EmployeeService employeeService;

    @Named(DataManager.NAME)
    private DataManager dataManager;

    @Named(Metadata.NAME)
    protected Metadata metadata;

    @Override
    protected void postInit() {
        controllerAssistant.initCompanyDs(companiesDs);
        controllerAssistant.addLookupAction(companyField, "erp$Company.browse", controllerAssistant.getCompanyParams());
        changeCompanyFieldRequired();

        adminCheckBox.addValueChangeListener(this::changeCompanyFieldRequired);
    }

    @Override
    protected boolean postCommit(boolean committed, boolean close) {
        if (committed) {
            controllerAssistant.init();
        }
        return super.postCommit(committed, close);
    }

    private void changeCompanyFieldRequired() {
        companyField.setRequired(!adminCheckBox.isChecked());
    }

    public void onCreateManager() {
        Employee existingForwarder = employeeService.getEmployeeByUser((ExtUser) getItem());
        if (existingForwarder == null && PersistenceHelper.isNew(getItem())) {
            showManagerCreateDialogWindow();
        } else if (existingForwarder != null) {
            showNotification(messages.getMessage(getClass(), "message.forwarderExist"), NotificationType.WARNING);
        } else {
            saveManager();
        }

    }

    private void showManagerCreateDialogWindow() {
        showOptionDialog(
                messages.getMessage(getClass(), "confirmDialog.title"),
                messages.getMessage(getClass(), "confirmDialog.message"),
                MessageType.CONFIRMATION,
                new Action[]{
                        new DialogAction(DialogAction.Type.YES, Action.Status.PRIMARY).withHandler(e -> {
                            companyField.setRequired(true);
                            if (commit()) {
                                saveManager();
                            }
                        }),
                        new DialogAction(DialogAction.Type.NO, Action.Status.NORMAL)
                }
        );
    }

    private void saveManager() {
        if (companyField.getValue() == null){
            showNotification(messages.getMessage(getClass(), "message.companyEmpty"), NotificationType.WARNING);
        } else {
            Employee employee = metadata.create(Employee.class);
            employee = controllerAssistant.updateEmployeeByUser((ExtUser) getItem(), employee);
            employee.setRole(employeeService.getEmployeeRoleById("7c36da70-e965-4274-8486-78d15d3fad6a"));

            dataManager.commit(employee);
            showNotification(messages.getMessage(getClass(), "message.saveManagerComplete"), NotificationType.WARNING);
        }
    }

    private void changeCompanyFieldRequired(ValueChangeEvent e) {
        changeCompanyFieldRequired();
    }
}