package com.planarry.erp.web.toolkit.ui;

import com.haulmont.cuba.gui.components.Component;
import com.planarry.erp.web.toolkit.ui.client.ratingfieldcomponent.RatingFieldComponentServerRpc;
import com.planarry.erp.web.toolkit.ui.client.ratingfieldcomponent.RatingFieldComponentState;
import com.vaadin.ui.AbstractField;

public class RatingFieldComponent extends AbstractField<Integer> {

    private Component.ValueChangeListener ratingListener;

    private int oldValue;

    public RatingFieldComponent() {
        // register an interface implementation that will be invoked on a request from the client
        registerRpc((RatingFieldComponentServerRpc) value -> {
            setValue(value, true);
            ratingListener.valueChanged(new Component.ValueChangeEvent(null, oldValue, value));
            oldValue = value;
        });
    }

    // field value type
    @Override
    public Class<? extends Integer> getType() {
        return Integer.class;
    }

    // define own state class
    @Override
    protected RatingFieldComponentState getState() {
        return (RatingFieldComponentState) super.getState();
    }

    @Override
    protected RatingFieldComponentState getState(boolean markAsDirty) {
        return (RatingFieldComponentState) super.getState(markAsDirty);
    }

    // we need to refresh the state when setValue is invoked from the application code
    @Override
    protected void setInternalValue(Integer newValue) {
        super.setInternalValue(newValue);
        if (newValue == null) {
            newValue = 0;
        }
        getState().value = newValue;
    }

    public Component.ValueChangeListener getRatingListener() {
        return ratingListener;
    }

    public void setRatingListener(Component.ValueChangeListener ratingListener) {
        this.ratingListener = ratingListener;
    }
}