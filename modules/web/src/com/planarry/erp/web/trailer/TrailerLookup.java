package com.planarry.erp.web.trailer;

import com.haulmont.cuba.gui.components.AbstractLookup;
import com.planarry.erp.entity.Company;
import com.planarry.erp.web.utils.ControllerAssistant;

import javax.inject.Named;
import java.util.Map;

public class TrailerLookup extends AbstractLookup{

    @Named(ControllerAssistant.NAME)
    private ControllerAssistant controllerAssistant;

    @Override
    public void init(Map<String, Object> params) {
        Company userCompany = controllerAssistant.getUserCompany();
        if (userCompany != null){
            params.put("accessibleToAll", true);
            params.put("accessibleToOwner", true);
            params.put("company", userCompany);
        }
        super.init(params);
    }
}
