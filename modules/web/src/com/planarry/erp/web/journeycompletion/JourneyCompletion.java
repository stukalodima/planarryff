
package com.planarry.erp.web.journeycompletion;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.planarry.erp.entity.*;
import com.planarry.erp.service.JourneyCompletionService;

import javax.inject.Named;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JourneyCompletion extends AbstractWindow {

    @Named("journeyDS")
    private Datasource<Journey> journeyDS;

    @Named("deliveriesCompositionDs")
    private CollectionDatasource<DeliveryComposition, UUID> deliveriesCompositionDs;

    @Named("compositionsTable")
    private Table<DeliveryComposition> compositionsTable;

    @Named("completeBtn")
    private Button completeBtn;

    @Named("completePointBtn")
    private Button completePointBtn;

    @Named("clearCompletedPointBtn")
    private Button clearCompletedPointBtn;

    @Named(DataManager.NAME)
    private DataManager dataManager;

    @Named(JourneyCompletionService.NAME)
    private JourneyCompletionService journeyCompletionService;

    private List<Delivery> deliveries;

    @Override
    public void init(Map<String, Object> params) {
        addDeliveriesCompositionDsItemChangeListener();
        initWindow(params);
        super.init(params);

        loadDeliveries();
        completeBtn.setEnabled(allDeliveriesFinished());
    }

    private void loadDeliveries() {
        deliveriesCompositionDs.clear();
        deliveries = journeyCompletionService.getDeliveriesByJourney(journeyDS.getItem());
        deliveries.forEach(delivery -> delivery.getDeliveryCompositions().forEach(deliveriesCompositionDs::includeItem));
        compositionsTable.sort("deliveryDate", Table.SortDirection.ASCENDING);
    }

    private void openWindow(Action action) {
        Map<String, Object> params = ParamsMap.of("journey", journeyDS.getItem(), "action", action);
        Window window = openWindow("journeyCompletionCombined", WindowManager.OpenType.DIALOG, params);
        window.addCloseWithCommitListener(() -> close(Window.COMMIT_ACTION_ID, true));
    }

    public void completeJourney() {
        openWindow(Action.COMPLETE);
    }

    public void cancelJourney() {
        openWindow(Action.CANCEL);
    }

    public void close() {
        close(Window.CLOSE_ACTION_ID, true);
    }

    public void onCompletePoint() {
        DeliveryComposition deliveryComposition = deliveriesCompositionDs.getItem();
        deliveryComposition.setStatus(EStatusItems.done);
        checkDeliveryStatus();

        CommitContext commitContext = new CommitContext();
        commitContext.addInstanceToCommit(deliveryComposition.getDelivery());
        commitContext.addInstanceToCommit(deliveryComposition.getDelivery().getCargo());
        dataManager.commit(commitContext);

        loadDeliveries();

        //show selection item in table
        compositionsTable.setSelected(deliveryComposition);
        completePointBtn.setEnabled(false);
        clearCompletedPointBtn.setEnabled(true);
    }

    public void onClearCompletedPoint() {
        DeliveryComposition deliveryComposition = deliveriesCompositionDs.getItem();
        deliveryComposition.setStatus(EStatusItems.running);
        deliveryComposition.getDelivery().setStatus(EStatusItems.running);
        deliveryComposition.getDelivery().getCargo().setStatus(EStatusItems.running);
        dataManager.commit(deliveryComposition.getDelivery());
        loadDeliveries();

        //show selection item in table
        compositionsTable.setSelected(deliveryComposition);
        completePointBtn.setEnabled(true);
        clearCompletedPointBtn.setEnabled(false);
        completeBtn.setEnabled(false);
    }

    private void initWindow(Map<String, Object> params) {
        Journey journey = (Journey) params.get("journey");
        journey = dataManager.reload(journey, "journey-completion-view");
        journeyDS.setItem(journey);
    }

    private void addDeliveriesCompositionDsItemChangeListener() {
        deliveriesCompositionDs.addItemChangeListener(e -> {
            completePointBtn.setEnabled(e.getItem() != null && (e.getItem().getStatus() != EStatusItems.done));
            clearCompletedPointBtn.setEnabled(e.getItem() != null && (e.getItem().getStatus() == EStatusItems.done));
        });
    }

    private void checkDeliveryStatus() {
        for (Delivery delivery : deliveries) {
            boolean allCompleted = true;
            for (DeliveryComposition deliveryComposition : delivery.getDeliveryCompositions()) {
                if (deliveryComposition.getStatus() != EStatusItems.done) {
                    allCompleted = false;
                    break;
                }
            }

            if (allCompleted) {
                delivery.setStatus(EStatusItems.done);
                delivery.getCargo().setStatus(EStatusItems.done);
                if (allDeliveriesFinished()) {
                    completeBtn.setEnabled(true);
                }
            }
        }
    }

    private boolean allDeliveriesFinished() {
        for (Delivery delivery : deliveries) {
            if (delivery.getStatus() != EStatusItems.done) {
                return false;
            }
        }
        return true;
    }

    public enum Action {
        COMPLETE,
        CANCEL
    }
}