package com.planarry.erp.gui.xml.layout.loaders;

import com.planarry.erp.gui.components.RatingField;
import com.haulmont.cuba.gui.xml.layout.loaders.AbstractComponentLoader;

public class RatingFieldLoader extends AbstractComponentLoader<RatingField> {
    @Override
    public void createComponent() {
        resultComponent = factory.createComponent(RatingField.class);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
    }
}
