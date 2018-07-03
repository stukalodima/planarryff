/*
 * Copyright(c) 2017 Planarry
 */
package com.planarry.erp.web.employee;

import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.actions.CreateAction;
import com.haulmont.cuba.gui.components.actions.EditAction;
import com.planarry.erp.web.utils.ControllerAssistant;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;
import java.util.UUID;

/**
 * @author Pavel Shynkarenko
 */
public class EmployeeBrowse extends AbstractLookup {

    @Named("employeesTable.create")
    private CreateAction employeeCreate;

    @Named("employeesTable.edit")
    private EditAction employeeEdit;

    @Inject
    private ControllerAssistant controllerAssistant;

    @Override
    public void init(Map<String, Object> params) {

        if (params.containsKey("roleStr")) {
            String roleStr = (String) params.get("roleStr");
            params.put("role", UUID.fromString(roleStr));
            controllerAssistant.changeEmployeeWindowCaption(getFrame(), roleStr, "browseTransportManagerCaption",
                    "browseCargoManagerCaption", getClass());
        }
        employeeCreate.setWindowParams(params);
        employeeEdit.setWindowParams(params);
        controllerAssistant.checkBrowseParams(params);
        super.init(params);
    }
}