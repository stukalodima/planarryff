
package com.planarry.erp.web.screens;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Image;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.ThemeResource;
import com.haulmont.cuba.web.app.loginwindow.AppLoginWindow;
import com.planarry.erp.web.utils.ControllerAssistant;
import org.apache.commons.lang.StringUtils;

import javax.inject.Named;

/**
 * @author Dima Stukalo
 */
public class ExtAppLoginWindow extends AppLoginWindow {

    @Named("logoImageExt")
    private Image logoImage;

    @Named("welcomeLabelExt")
    private Label welcomeLabel;

    @Named("erp_ControllerAssistant")
    private ControllerAssistant controllerAssistant;

    @Override
    protected void initLogoImage() {
        String loginLogoImagePath = messages.getMainMessage("loginWindow.logoImage", app.getLocale());
        if (StringUtils.isBlank(loginLogoImagePath) || "loginWindow.logoImage".equals(loginLogoImagePath)) {
            welcomeLabel.setVisible(true);
            logoImage.setVisible(false);
        } else {
            welcomeLabel.setVisible(false);
            logoImage.setSource(ThemeResource.class).setPath(loginLogoImagePath);
        }
    }

    @Override
    protected void initPoweredByLink() {
        Component poweredByLink = getComponent("poweredByLink");
        if (poweredByLink != null) {
            poweredByLink.setVisible(true);
        }
    }

    @Override
    public void login() {
        super.login();
        controllerAssistant.init();
    }
}