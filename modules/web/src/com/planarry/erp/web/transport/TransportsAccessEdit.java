package com.planarry.erp.web.transport;

import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.planarry.erp.entity.AccessEntity;
import com.planarry.erp.entity.Company;
import com.planarry.erp.entity.Transport;
import com.planarry.erp.entity.TransportsAccess;
import com.planarry.erp.web.utils.accesstoentities.AccessAssistant;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TransportsAccessEdit extends AbstractWindow {

    @Inject
    private Datasource<Transport> transportDs;

    @Inject
    private CollectionDatasource<TransportsAccess, UUID> transportsAccessDs;

    @Inject
    private CollectionDatasource<Company, UUID> companiesDs;

    @Named("companiesTable")
    private Table<TransportsAccess> companiesTable;

    @Named("companiesSearchTable")
    private Table<Company> companiesSearchTable;

    @Named(Metadata.NAME)
    private Metadata metadata;

    private Transport transport;

    private List<TransportsAccess> transportsAccesses;

    @Override
    public void init(Map<String, Object> params) {
        transport = (Transport) params.get("transport");
        transportDs.setItem(transport);
        transportsAccesses = (List<TransportsAccess>) params.get("transportsAccesses");
        transportsAccesses.forEach(transportsAccessDs::includeItem);
        super.init(params);

        AccessAssistant.Companion companion = getCompanion();
        if (companion != null) {
            companion.initDragAndDrop(companiesTable, companiesSearchTable, this::includeCompanyToTransport, this::excludeCompanyFromTransport, TransportsAccess.class);
        }
    }

    public void onIncludeCompanyToTransport() {
        includeCompanyToTransport(companiesDs.getItem());
    }

    public void onExcludeCompanyToTransport() {
        excludeCompanyFromTransport(transportsAccessDs.getItem());
    }

    public void onClose() {
        close(Window.CLOSE_ACTION_ID);
    }

    public void onSave() {
        close(Window.COMMIT_ACTION_ID);
    }

    private void includeCompanyToTransport(Company company) {
        TransportsAccess transportsAccess = metadata.create(TransportsAccess.class);
        transportsAccess.setTransport(transport);
        transportsAccess.setCompany(company);
        transportsAccessDs.includeItem(transportsAccess);
        transportsAccesses.add(transportsAccess);
        companiesDs.excludeItem(company);
    }

    private <T extends AccessEntity> void excludeCompanyFromTransport(T transportsAccess) {
        transportsAccesses.remove(transportsAccess);
        transportsAccessDs.excludeItem((TransportsAccess) transportsAccess);
        companiesDs.includeItem(transportsAccess.getCompany());
    }

    public List<TransportsAccess> getTransportsAccesses() {
        return transportsAccesses;
    }
}
