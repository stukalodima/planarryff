package com.planarry.erp.web.transport;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.planarry.erp.entity.Transport;
import com.planarry.erp.web.utils.ControllerAssistant;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

public class TransportBrowse extends AbstractLookup {

    @Inject
    private GroupTable<Transport> transportsTable;

    @Named(ComponentsFactory.NAME)
    private ComponentsFactory componentsFactory;

    @Named(ControllerAssistant.NAME)
    private ControllerAssistant controllerAssistant;

    @Named(DataManager.NAME)
    private DataManager dataManager;

    @Override
    public void init(Map<String, Object> params) {
        if (controllerAssistant.checkUserStatusAndCompany(params)) {
            showNotification(messages.getMessage(getClass(), "message.cantLoadTransport"), Frame.NotificationType.WARNING);
        }
        controllerAssistant.checkBrowseParams(params);

        super.init(params);
    }

    public Component generateImageFileCell(Transport transport) {
        Image image = componentsFactory.createComponent(Image.class);
        image.setDatasource(transportsTable.getItemDatasource(transport), "photo");
        image.setHeight("50");
        image.setWidth("80");
        image.setScaleMode(Image.ScaleMode.CONTAIN);
        return image;
    }

    public Component generateAccessColumn(Entity entity) {
        Transport transport = (Transport) entity;
        String ownerMessage = messages.getMessage(getClass(), "ownerAccessMessageFull_1") + " " + transport.getCompany().getName()
                + messages.getMessage(getClass(), "ownerAccessMessageFull_2");
        return controllerAssistant.generateAccessColumn(transport.getAvailableForOwner(), transport.getAvailableForAll(), transport.getTransportsAccesses(),
                getFrame(), ownerMessage, messages.getMessage(getClass(), "allAccessMessageFull"));
    }
}
