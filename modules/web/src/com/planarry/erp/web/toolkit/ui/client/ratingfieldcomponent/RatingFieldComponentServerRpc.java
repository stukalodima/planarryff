package com.planarry.erp.web.toolkit.ui.client.ratingfieldcomponent;

import com.vaadin.shared.communication.ServerRpc;

public interface RatingFieldComponentServerRpc extends ServerRpc {

    void starClicked(int value);
}