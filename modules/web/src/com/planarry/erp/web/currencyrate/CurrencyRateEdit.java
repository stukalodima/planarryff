package com.planarry.erp.web.currencyrate;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.planarry.erp.entity.CurrencyRate;

import javax.inject.Named;
import java.util.List;

public class CurrencyRateEdit extends AbstractEditor<CurrencyRate> {

    @Named("cuba_DataManager")
    private DataManager dataManager;

    @Named("cuba_Message")
    private Messages messages;

    @Override
    protected boolean preCommit() {
        LoadContext<CurrencyRate> loadContext = LoadContext.create(CurrencyRate.class)
                .setQuery(LoadContext.createQuery("select e from erp$CurrencyRate e where e.date = :date and e.currency.id = :currency")
                .setParameter("date", getItem().getDate())
                .setParameter("currency", getItem().getCurrency()))
                .setView(View.MINIMAL);

        List<CurrencyRate> rates = dataManager.loadList(loadContext);
        if (rates.size() != 0 && !getItem().equals(rates.get(0))){
            showNotification(messages.getMessage(getClass(), "message.CurrencyRateExist"), NotificationType.WARNING);
            return false;
        }
        return super.preCommit();
    }
}
