/*
 * Copyright(c) 2017 Planarry
 */
package com.planarry.erp.web.journey;

import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.actions.CreateAction;
import com.haulmont.cuba.gui.components.actions.EditAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.planarry.erp.entity.Cargo;
import com.planarry.erp.entity.EStatusItems;
import com.planarry.erp.entity.Journey;
import com.planarry.erp.service.ConstantService;
import com.planarry.erp.service.CurrencyService;
import com.planarry.erp.service.JourneyService;
import com.planarry.erp.web.utils.ControllerAssistant;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dima Stukalo
 * @author Aleksandr Iushko
 */
public class JourneyBrowse extends AbstractLookup {

    @Named("journeysDs")
    private GroupDatasource<Journey, UUID> journeysDs;

    @Named("approvedBtn")
    private Button approvedBtn;

    @Named("priorApprovalBtn")
    private Button priorApprovalBtn;

    @Named("journeysTable.create")
    private CreateAction journeysTableCreate;

    @Named("journeysTable.edit")
    private EditAction journeysTableEdit;

    @Named("journeysTable.remove")
    private RemoveAction journeysTableRemove;

    @Named(DataManager.NAME)
    private DataManager dataManager;

    @Named(JourneyService.NAME)
    private JourneyService journeyService;

    @Named(ConstantService.NAME)
    private ConstantService constantService;

    @Named(Metadata.NAME)
    private Metadata metadata;

    @Named(ControllerAssistant.NAME)
    private ControllerAssistant controllerAssistant;

    @Inject
    private CurrencyService currencyService;


    @Override
    public void init(Map<String, Object> params) {

        addJourneyDsItemChangeListener();
        super.init(params);

        journeysTableEdit.setWindowId("erp$Journey.edit");
        journeysTableCreate.setWindowId("erp$Journey.create");

        journeysTableRemove.setAfterRemoveHandler(removedItems -> {
            for (Object removedItem : removedItems) {
                Journey journey = (Journey) removedItem;
                CommitContext commitContext = new CommitContext();

                //change cargo status
                journey.getJourneyCargoes().forEach(item -> {
                    item.getCargo().setStatus(EStatusItems.created);
                    commitContext.addInstanceToCommit(item.getCargo());
                });

                dataManager.commit(commitContext);
            }
        });
    }

    /**
     * Add listener for control changes item journeyDs.
     */
    private void addJourneyDsItemChangeListener() {
        journeysDs.addItemChangeListener(e -> {
            if (e.getItem() != null) {
                changeApprovedBtnCaption();
                changePriorApprovedBtnCaption();
            }
        });
    }

    /**
     * Check item field "approved" and change caption button.
     */
    private void changeApprovedBtnCaption() {
        String captionApprove = journeysDs.getItem().getApproved() ? "cancelApproval" : "approve";
        approvedBtn.setCaption(messages.getMessage(getClass(), captionApprove));
    }

    private void changePriorApprovedBtnCaption() {
        EStatusItems status = journeysDs.getItem().getStatus();
        String captionPriorApprove = (status.equals(EStatusItems.preApproved) || journeysDs.getItem().getApproved())
                ? "CancelPriorApprove"
                : "priorApprove";
        priorApprovalBtn.setCaption(messages.getMessage(getClass(), captionPriorApprove));
    }


    /**
     * Get item from journeyDs, change it`s field "approved" on the reverse, change caption button;
     * Commit to dataManager this journey and refresh journeyDs for correct views.
     */
    public void onApprovedAction() {
        EStatusItems status = journeysDs.getItem().getStatus();
        if (status.equals(EStatusItems.preApproved) || status.equals(EStatusItems.approved)) {
            Map<String, Boolean> commitResult = journeyService.saveDataTransactionOnApprove(journeysDs.getItem());
            if (commitResult.isEmpty()) {
                journeysDs.refresh();
                changeApprovedBtnCaption();
            } else {
                showNotification(messages.getMessage(getClass(), "message.error") + "\n" + commitResult.keySet().stream().findFirst().get(), NotificationType.WARNING);
            }
        } else {
            showNotification(messages.getMessage(getClass(),"message.falsePriorApproval"));
        }
    }

    /**
     * Change status "priorApproval" on true is journey "status" is "planned",
     * if not will show notification.
     */
    public void onPriorApprovalAction() {
        Journey journey = journeysDs.getItem();
        EStatusItems status = journey.getStatus();
        if (status.equals(EStatusItems.planned) || status.equals(EStatusItems.preApproved)) {
            CommitContext commitContext = new CommitContext();
            EStatusItems newStatus = status.equals(EStatusItems.planned) ? EStatusItems.preApproved : EStatusItems.planned;
            journey.setStatus(newStatus);
            journey.getJourneyCargoes().forEach(journeyCargo -> {
                Cargo cargo = journeyCargo.getCargo();
                cargo.setStatus(newStatus);
                commitContext.addInstanceToCommit(cargo);
            });
            commitContext.addInstanceToCommit(journey);
            dataManager.commit(commitContext);
            journeysDs.refresh();
            changePriorApprovedBtnCaption();
        } else if (status.equals(EStatusItems.approved)) {
            showNotification(messages.getMessage(getClass(), "message.isApproved"));

        } else {
            showNotification(messages.getMessage(getClass(), "message.notApprovedStatusJourney"));
        }
    }
}