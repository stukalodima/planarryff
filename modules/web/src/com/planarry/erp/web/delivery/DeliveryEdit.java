
package com.planarry.erp.web.delivery;

import com.haulmont.cuba.gui.components.AbstractEditor;
import com.planarry.erp.entity.Delivery;

public class DeliveryEdit extends AbstractEditor<Delivery> {

    public void close() {
        close(CLOSE_ACTION_ID);
    }
}