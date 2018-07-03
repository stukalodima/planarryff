/*
 * Copyright(c) 2017 Planarry
 */
package com.planarry.erp.web.servicespayment;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.data.Datasource;
import com.planarry.erp.entity.ClientCredit;
import com.planarry.erp.entity.MutualSettlements;
import com.planarry.erp.entity.ServicesPayment;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

/**
 * @author Aleksandr Iushko
 */
public class ServicesPaymentEdit extends AbstractEditor<ServicesPayment> {

    @Inject
    private Datasource<ServicesPayment> servicesPaymentDs;

    @Named("fieldGroup.counterparty")
    private PickerField counterpartyField;

    @Named(DataManager.NAME)
    private DataManager dataManager;

    @Named(Metadata.NAME)
    private Metadata metadata;

    @Override
    public void init(Map<String, Object> params) {
        checkTypePayment(getItem().getTypePayment().getId());
        addItemChangeListener();
        super.init(params);
    }

    @Override
    protected boolean postCommit(boolean committed, boolean close) {
        Boolean result = saveData();
        return super.postCommit(result, close);
    }

    /**
     * Add listener for changes type payment. (hide/visible counterParty field).
     */
    private void addItemChangeListener() {
        servicesPaymentDs.addItemChangeListener(e -> checkTypePayment(e.getItem().getTypePayment().getId()));
    }

    /**
     * @param id receive for check type payment and setVisible = true/false.
     */
    private void checkTypePayment(Integer id) {
        if (id != 1) {
            counterpartyField.setVisible(true);
        } else {
         counterpartyField.setVisible(false);
        }
    }

    /**
     * Depending on the type payment create mutualSettlement or clientCredit entity;
     * And save data from ServicePayment entity to the appropriate tables (erp$MutualSettlements/erp$ClientCredit).
     */
    private Boolean saveData() {
        if (getItem().getTypePayment().getId() == 1) {
            MutualSettlements mutualSettlements = metadata.create(MutualSettlements.class);
            mutualSettlements.setValue(getItem().getValue() * -1);
            mutualSettlements.setTransportOwner(getItem().getCompany());
            mutualSettlements.setPayDate(AppBeans.get(TimeSource.class).currentTimestamp());
            try {
                dataManager.commit(mutualSettlements);
                return true;
            } catch (Exception e) {
                showNotification(messages.getMessage(getClass(), "message.error") + e.getLocalizedMessage(), Frame.NotificationType.WARNING);
                return false;
            }
        } else {
            ClientCredit clientCredit = metadata.create(ClientCredit.class);
            clientCredit.setValue(getItem().getValue() * -1);
            clientCredit.setPayDate(AppBeans.get(TimeSource.class).currentTimestamp());
            clientCredit.setTransportOwner(getItem().getCompany());
            clientCredit.setCounterparty(getItem().getCounterparty());
            try {
                dataManager.commit(clientCredit);
                return true;
            } catch (Exception e) {
                showNotification(messages.getMessage(getClass(), "message.error") + e.getLocalizedMessage(), Frame.NotificationType.WARNING);
                return false;
            }
        }
    }
}