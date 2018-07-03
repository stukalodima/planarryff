package com.planarry.erp.web.defaultwindow;

import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Window;

public class SubscribeInfoWindow extends AbstractWindow {

    public void onClose() {
        close(Window.CLOSE_ACTION_ID, true);
    }
}