package com.planarry.erp.web.transporttype;

import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.planarry.erp.entity.TransportType;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;
import java.util.UUID;

public class TransportTypeBrowse extends AbstractLookup {

    @Inject
    private HierarchicalDatasource<TransportType, UUID> transportTypesDs;

    @Named("createBtn")
    private Button createBtn;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        transportTypesDs.addItemChangeListener(e -> {
            if (e.getItem() != null && !e.getItem().isRoot()) {
                createBtn.setEnabled(false);
            }else {
                createBtn.setEnabled(true);
            }
        });
    }
}
