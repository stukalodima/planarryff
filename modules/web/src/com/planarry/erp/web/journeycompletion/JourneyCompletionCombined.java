
package com.planarry.erp.web.journeycompletion;

import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.planarry.erp.entity.EStatusItems;
import com.planarry.erp.entity.Journey;
import com.planarry.erp.entity.JourneyState;
import com.planarry.erp.service.JourneyService;
import com.planarry.erp.web.toolkit.ui.RatingFieldComponent;
import com.vaadin.ui.Layout;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

public class JourneyCompletionCombined extends AbstractWindow {

    @Named("cancelBtn")
    private Button cancelBtn;

    @Named("completeBtn")
    private Button completeBtn;

    @Named("finalPrice")
    private TextField finalPrice;

    @Named("rtaComment")
    private RichTextArea rtaComment;

    @Named("logisticianComment")
    private RichTextArea logisticianComment;

    @Named("ratingLbl")
    private Label ratingLbl;

    @Named("container")
    private HBoxLayout container;

    @Named(DataManager.NAME)
    private DataManager dataManager;

    @Named(JourneyService.NAME)
    private JourneyService journeyService;

    private Journey journey;
    private JourneyState journeyState;

    @Override
    public void init(Map<String, Object> params) {
        journeyState = journeyService.getJourneyState((Journey) params.get("journey"), "journeyState-view");
        journey = journeyState.getJourney();

        changeVisibilityComponents((JourneyCompletion.Action) params.get("action"));
        super.init(params);
    }

    private void saveAndClose(EStatusItems status){
        CommitContext commitContext = new CommitContext();
        journey.setStatus(status);
        journey.setComment(logisticianComment.getValue());
        journey.setFinalPrice(finalPrice.getValue());
        journeyState.setStatus(status);
        journeyState.setComment(rtaComment.getValue());

        journey.getJourneyCargoes().forEach(journeyCargo -> {
            journeyCargo.getCargo().setStatus(status == EStatusItems.done ? status :EStatusItems.created);
            commitContext.addInstanceToCommit(journeyCargo.getCargo());
        });

        commitContext.addInstanceToCommit(journey);
        commitContext.addInstanceToCommit(journeyState);
        dataManager.commit(commitContext);
        close(Window.COMMIT_ACTION_ID);
    }

    public void completeJourney() {
        saveAndClose(EStatusItems.done);
    }

    public void cancelJourney() {
        saveAndClose(EStatusItems.canceled);
    }

    public void close() {
        close(Window.CLOSE_ACTION_ID);
    }

    private void changeVisibilityComponents(JourneyCompletion.Action action){
        switch (action){
            case CANCEL:
                ratingLbl.setVisible(false);
                completeBtn.setVisible(false);
                break;
            case COMPLETE:
                logisticianComment.setVisible(true);
                finalPrice.setVisible(true);
                createRatingField();
                cancelBtn.setVisible(false);
                break;
        }
    }

    private void createRatingField(){
        Layout containerLayout = (Layout) WebComponentsHelper.unwrap(container);
        RatingFieldComponent ratingField = new RatingFieldComponent();
        ratingField.setRatingListener(e -> journey.setRating((Integer) e.getValue()));
        containerLayout.addComponent(ratingField);
    }
}