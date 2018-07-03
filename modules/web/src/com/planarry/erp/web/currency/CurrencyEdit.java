package com.planarry.erp.web.currency;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.planarry.erp.entity.Currency;

import javax.inject.Named;
import java.util.List;

public class CurrencyEdit extends AbstractEditor<Currency> {

    @Named(DataManager.NAME)
    private DataManager dataManager;

    @Named(Messages.NAME)
    private Messages messages;

    @Override
    protected boolean preCommit() {
        if (getItem().getBaseCurrency() != null && getItem().getBaseCurrency()) {
            LoadContext<Currency> loadContext = LoadContext.create(Currency.class)
                    .setQuery(LoadContext.createQuery("select e from erp$Currency e where e.baseCurrency = :baseCurrency")
                            .setParameter("baseCurrency", true)).setView(View.MINIMAL);

            List<Currency> currencies = dataManager.loadList(loadContext);
            if (currencies.size() != 0 && !getItem().equals(currencies.get(0))){
                showNotification(messages.getMessage(getClass(), "message.baseCurrencyExist"), NotificationType.WARNING);
                return false;
            }
        }
        return super.preCommit();
    }
}
