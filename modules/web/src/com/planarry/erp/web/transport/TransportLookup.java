package com.planarry.erp.web.transport;

import com.haulmont.cuba.gui.components.AbstractLookup;
import com.planarry.erp.entity.Company;
import com.planarry.erp.entity.ExtUser;
import com.planarry.erp.web.utils.ControllerAssistant;

import javax.inject.Named;
import java.util.Map;

public class TransportLookup extends AbstractLookup {

    @Named(ControllerAssistant.NAME)
    private ControllerAssistant controllerAssistant;

    @Override
    public void init(Map<String, Object> params) {
        ExtUser currentUser = controllerAssistant.getCurrentUser();

        if (currentUser != null) {
            Company company = currentUser.getCompany();
            params.put("currentUser", currentUser);
            params.put("availableToAll", true);
            params.put("availableToOwner", true);
            params.put("company", company);
        }
        super.init(params);
    }
}
