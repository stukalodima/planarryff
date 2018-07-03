
package com.planarry.erp.web.extuser;

import com.haulmont.cuba.gui.app.security.user.browse.UserBrowser;
import com.planarry.erp.web.utils.ControllerAssistant;

import javax.inject.Named;
import java.util.Map;

public class ExtUserBrowser extends UserBrowser {

    @Named(ControllerAssistant.NAME)
    private ControllerAssistant controllerAssistant;

    @Override
    public void init(Map<String, Object> params) {
        controllerAssistant.checkBrowseParams(params);
        super.init(params);
    }
}