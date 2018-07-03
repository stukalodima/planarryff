package com.planarry.erp.web.map.frame;

import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.CurrencyField;
import com.haulmont.cuba.gui.data.Datasource;
import com.planarry.erp.entity.AreaData;

import javax.inject.Inject;
import javax.inject.Named;

public class AreaDataFrame extends AbstractFrame {

    @Named("areaDataDs")
    private Datasource<AreaData> areaDataDs;
    @Inject
    private CurrencyField loadingTimePriceField;
    @Inject
    private CurrencyField unloadingTimePriceField;
    @Inject
    private CurrencyField exitPriceField;
    @Inject
    private CurrencyField entrancePriceField;
    @Inject
    private CurrencyField roadDistPriceField;
    @Inject
    private CurrencyField roadTimePriceField;
    @Inject
    private CurrencyField sumTimePriceField;
    @Inject
    private CurrencyField totalPriceField;

    public void initFrame(AreaData areaData, String currency){
        areaDataDs.setItem(areaData);

        if (areaData.getArea() == null){
            exitPriceField.setVisible(false);
            entrancePriceField.setVisible(false);
        }

        loadingTimePriceField.setCurrency(currency);
        unloadingTimePriceField.setCurrency(currency);
        exitPriceField.setCurrency(currency);
        entrancePriceField.setCurrency(currency);
        roadDistPriceField.setCurrency(currency);
        roadTimePriceField.setCurrency(currency);
        sumTimePriceField.setCurrency(currency);
        totalPriceField.setCurrency(currency);
    }
}
