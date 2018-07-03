package com.planarry.erp.web.journey;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.CurrencyField;
import com.haulmont.cuba.gui.components.GroupBoxLayout;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.planarry.erp.entity.CargoDeliveryPoint;
import com.planarry.erp.entity.EStatusItems;
import com.planarry.erp.entity.Journey;
import com.planarry.erp.entity.JourneyCargo;
import com.planarry.erp.service.CurrencyService;
import javax.inject.Named;
import java.util.UUID;

public class JourneyEdit extends AbstractEditor<Journey> {

    @Named("journeyCargoesDs")
    private CollectionDatasource<JourneyCargo, UUID> journeyCargoesDs;

    @Named("cargoDeliveryPointsDs")
    private CollectionDatasource<CargoDeliveryPoint, UUID> cargoDeliveryPointsDs;

    @Named("infoBox_2")
    private GroupBoxLayout infoBox;

    @Named(ComponentsFactory.NAME)
    private ComponentsFactory componentsFactory;

    @Named(DataManager.NAME)
    private DataManager dataManager;

    @Named(CurrencyService.NAME)
    private CurrencyService currencyService;

    private CurrencyField priceField;

    private CurrencyField finalPriceField;


    public Component generateCurrencyField(Datasource datasource, String fieldId) {
        CurrencyField currencyField = componentsFactory.createComponent(CurrencyField.class);
        currencyField.setDatasource(datasource, fieldId);
        if (fieldId.equals("transportationPrice")) {
            priceField = currencyField;
            priceField.setEditable(false);
        } else if (fieldId.equals("finalPrice")) {
            finalPriceField = currencyField;
            finalPriceField.setEditable(false);
        }
        return currencyField;
    }

    @Override
    protected void postInit() {
        super.postInit();
        if (getItem().getStatus().equals(EStatusItems.done)) {
            infoBox.setVisible(true);
        }
        priceField.setCurrency(getItem().getCurrency().getShirtName());
        finalPriceField.setCurrency(getItem().getCurrency().getShirtName());

        journeyCargoesDs.getItems().forEach(item -> {
            item.getCargo().getDeliveryPoints().forEach(cargoDeliveryPointsDs::includeItem);
        });
    }
}