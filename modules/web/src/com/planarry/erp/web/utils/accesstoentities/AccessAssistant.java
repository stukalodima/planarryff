package com.planarry.erp.web.utils.accesstoentities;

import com.haulmont.cuba.gui.components.Table;
import com.planarry.erp.entity.AccessEntity;
import com.planarry.erp.entity.Company;

public class AccessAssistant {

    public interface Companion {
       <T extends AccessEntity> void initDragAndDrop(Table<T> accessesTable, Table<Company> companiesTable, TableAddAction addAction,
                                                     TableRemoveAction removeAction, Class<T> accessEntityClass);
    }

    public interface TableAddAction {
        void addEntity(Company company);
    }

    public interface TableRemoveAction {
        <T extends AccessEntity> void removeEntity(T accessEntity);
    }
}
