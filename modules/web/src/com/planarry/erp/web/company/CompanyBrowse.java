package com.planarry.erp.web.company;

import com.haulmont.cuba.gui.components.AbstractLookup;
import com.planarry.erp.web.utils.ControllerAssistant;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

public class CompanyBrowse extends AbstractLookup {

    @Named(ControllerAssistant.NAME)
    private ControllerAssistant controllerAssistant;

    @Override
    public void init(Map<String, Object> params) {
        controllerAssistant.checkBrowseParams(params);
        super.init(params);
    }
}
