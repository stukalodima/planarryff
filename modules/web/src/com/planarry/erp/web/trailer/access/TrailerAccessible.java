package com.planarry.erp.web.trailer.access;

import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.planarry.erp.entity.AccessEntity;
import com.planarry.erp.entity.AccessToTrailer;
import com.planarry.erp.entity.Company;
import com.planarry.erp.entity.Trailer;
import com.planarry.erp.web.utils.accesstoentities.AccessAssistant;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TrailerAccessible extends AbstractWindow {

    @Named("trailerDs")
    private Datasource<Trailer> trailerDs;

    @Named("accessesDs")
    private CollectionDatasource<AccessToTrailer, UUID> accessesDs;

    @Named("companiesDS")
    private CollectionDatasource<Company, UUID> companiesDS;

    @Named("accessesTable")
    private Table<AccessToTrailer> accessesTable;

    @Named("companiesTable")
    private Table<Company> companiesTable;

    @Named("fieldGroup.company")
    private PickerField companyField;

    @Named(Metadata.NAME)
    private Metadata metadata;

    private List<AccessToTrailer> newAccesses;

    @Override
    public void init(Map<String, Object> params) {
        if (params.containsKey("trailer")){
            Trailer trailer = (Trailer) params.get("trailer");
            trailerDs.setItem(trailer);
        }

        if (params.containsKey("accesses")){
            List<AccessToTrailer> accesses = (ArrayList) params.get("accesses");
            accesses.forEach(accessesDs::includeItem);
        }
        super.init(params);

        AccessAssistant.Companion companion = getCompanion();
        if (companion != null) {
            companion.initDragAndDrop(accessesTable, companiesTable, this::includeCompany, this::excludeCompany, AccessToTrailer.class);
        }

        companyField.removeAllActions();
    }

    private <T extends AccessEntity> void excludeCompany(T access){
        companiesDS.includeItem(access.getCompany());
        accessesDs.excludeItem((AccessToTrailer) access);
    }

    private void includeCompany(Company company) {
        AccessToTrailer accessToTrailer = metadata.create(AccessToTrailer.class);
        accessToTrailer.setTrailer(trailerDs.getItem());
        accessToTrailer.setCompany(company);
        accessesDs.includeItem(accessToTrailer);
        companiesDS.excludeItem(company);
    }

    public void onExcludeCompany() {
        excludeCompany(accessesDs.getItem());
    }

    public void onIncludeCompany() {
        includeCompany(companiesDS.getItem());
    }

    public void onSave() {
        newAccesses = new ArrayList<>(accessesDs.getItems());
        close(Window.COMMIT_ACTION_ID, true);
    }

    public void onCancel() {
        close(Window.CLOSE_ACTION_ID, true);
    }

    public List<AccessToTrailer> getNewAccesses() {
        return newAccesses;
    }
}