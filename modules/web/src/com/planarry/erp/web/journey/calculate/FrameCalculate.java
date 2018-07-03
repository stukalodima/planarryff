package com.planarry.erp.web.journey.calculate;

import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Image;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.ThemeResource;

import javax.inject.Named;
import java.util.Map;

public class FrameCalculate extends AbstractWindow {

    @Named("imageLoad")
    private Image imageLoad;

    @Named("labelStatus")
    private Label labelStatus;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        imageLoad.setSource(ThemeResource.class)
                .setPath("images/load.gif");
    }
}
