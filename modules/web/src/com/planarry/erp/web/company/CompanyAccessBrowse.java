package com.planarry.erp.web.company;

import com.haulmont.cuba.gui.components.AbstractLookup;

public class CompanyAccessBrowse extends AbstractLookup {

    public void onClose() {
        close(CLOSE_ACTION_ID, true);
    }
}
