package com.planarry.erp.web.cargo.access;

import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.planarry.erp.entity.AccessEntity;
import com.planarry.erp.entity.AccessToCargo;
import com.planarry.erp.entity.Cargo;
import com.planarry.erp.entity.Company;
import com.planarry.erp.web.utils.accesstoentities.AccessAssistant;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

public class CargoAccessible extends AbstractWindow {

    @Named("cargoDs")
    private Datasource<Cargo> cargoDs;

    @Named("accessesDs")
    private CollectionDatasource<AccessToCargo, UUID> accessesDs;

    @Named("companiesDS")
    private CollectionDatasource<Company, UUID> companiesDS;

    @Named("accessesTable")
    private Table<AccessToCargo> accessesTable;

    @Named("companiesTable")
    private Table<Company> companiesTable;
    @Inject
    private Metadata metadata;

    private List<AccessToCargo> newAccesses;


    @Override
    public void init(Map<String, Object> params) {
        if (params.containsKey("cargo")){
            Cargo cargo = (Cargo) params.get("cargo");
            cargoDs.setItem(cargo);
        }

        if (params.containsKey("accesses")){
            List<AccessToCargo> accesses = (ArrayList) params.get("accesses");
            accesses.forEach(accessesDs::includeItem);
        }
        super.init(params);

        AccessAssistant.Companion companion = getCompanion();
        if (companion != null) {
            companion.initDragAndDrop(accessesTable, companiesTable, this::includeCompany, this::excludeCompany, AccessToCargo.class);
        }
    }

    private  <T extends AccessEntity> void excludeCompany(T access){
        companiesDS.includeItem(access.getCompany());
        accessesDs.excludeItem((AccessToCargo) access);
    }

    private void includeCompany(Company company) {
        AccessToCargo accessToCargo = metadata.create(AccessToCargo.class);
        accessToCargo.setCargo(cargoDs.getItem());
        accessToCargo.setCompany(company);
        accessesDs.includeItem(accessToCargo);
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

    public List<AccessToCargo> getNewAccesses() {
        return newAccesses;
    }
}