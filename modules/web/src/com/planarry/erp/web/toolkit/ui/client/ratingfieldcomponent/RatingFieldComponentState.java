package com.planarry.erp.web.toolkit.ui.client.ratingfieldcomponent;

import com.vaadin.shared.AbstractFieldState;

public class RatingFieldComponentState extends AbstractFieldState {
    {   // change the main style name of the component
        primaryStyleName = "ratingfield";
    }
    // define a field for the value
    public int value = 0;
}
