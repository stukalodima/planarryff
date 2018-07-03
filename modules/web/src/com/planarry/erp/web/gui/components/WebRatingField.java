package com.planarry.erp.web.gui.components;

import com.planarry.erp.gui.components.RatingField;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;

public class WebRatingField extends WebAbstractComponent<com.planarry.erp.web.toolkit.ui.RatingFieldComponent> implements RatingField {
    public WebRatingField() {
        this.component = new com.planarry.erp.web.toolkit.ui.RatingFieldComponent();
    }
}