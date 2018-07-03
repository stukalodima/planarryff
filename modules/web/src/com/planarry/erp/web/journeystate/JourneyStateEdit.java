
package com.planarry.erp.web.journeystate;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.planarry.erp.entity.JourneyState;

import javax.inject.Named;

public class JourneyStateEdit extends AbstractEditor<JourneyState> {

    @Named(DataManager.NAME)
    private DataManager dataManager;

    @Override
    protected boolean postCommit(boolean committed, boolean close) {
        if (committed){
            getItem().getJourney().setStatus(getItem().getStatus());
            dataManager.commit(getItem().getJourney());
        }
        return super.postCommit(committed, close);
    }
}