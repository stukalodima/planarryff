package com.planarry.erp.web.toolkit.ui.client.ratingfieldcomponent;

import com.planarry.erp.web.toolkit.ui.RatingFieldComponent;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractFieldConnector;
import com.vaadin.shared.ui.Connect;

@Connect(RatingFieldComponent.class)
public class RatingFieldComponentConnector extends AbstractFieldConnector {

    @Override
    public RatingFieldComponentWidget getWidget() {
        RatingFieldComponentWidget widget = (RatingFieldComponentWidget) super.getWidget();

        if (widget.listener == null) {
            widget.listener = value -> getRpcProxy(RatingFieldComponentServerRpc.class).starClicked(value);
        }
        return widget;
    }

    // our state class is RatingFieldState
    @Override
    public RatingFieldComponentState getState() {
        return (RatingFieldComponentState) super.getState();
    }

    // react on server state change
    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);

        // refresh the widget if the value on server has changed
        if (stateChangeEvent.hasPropertyChanged("value")) {
            getWidget().setValue(getState().value);
        }
    }
}
