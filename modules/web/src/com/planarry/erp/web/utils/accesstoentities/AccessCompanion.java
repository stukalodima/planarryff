package com.planarry.erp.web.utils.accesstoentities;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.planarry.erp.entity.AccessEntity;
import com.planarry.erp.entity.Company;
import com.planarry.erp.entity.Trailer;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.And;
import com.vaadin.ui.AbstractSelect;

import javax.annotation.Nullable;

public class AccessCompanion implements AccessAssistant.Companion {

    @Override
    public <T extends AccessEntity> void initDragAndDrop(Table<T> accessesTable, Table<Company> companiesTable, AccessAssistant.TableAddAction addAction,
                                                          AccessAssistant.TableRemoveAction removeAction, Class<T> accessEntityClass) {

        com.vaadin.ui.Table vSourceTable = companiesTable.unwrap(com.vaadin.ui.Table.class);
        vSourceTable.setDragMode(com.vaadin.ui.Table.TableDragMode.ROW);
        vSourceTable.setDropHandler(new DropHandler() {
            @Override
            public void drop(DragAndDropEvent dropEvent) {
                DataBoundTransferable transferable = (DataBoundTransferable) dropEvent.getTransferable();

                Object itemId = transferable.getItemId();
                Container sourceContainer = transferable.getSourceContainer();
                T entity = convertToEntity(sourceContainer.getItem(itemId), accessEntityClass);
                if (entity == null) {
                    return;
                }

                removeAction.removeEntity(entity);
            }

            @Override
            public AcceptCriterion getAcceptCriterion() {
                return new And(AbstractSelect.AcceptItem.ALL);
            }
        });

        com.vaadin.ui.Table vTargetTable = accessesTable.unwrap(com.vaadin.ui.Table.class);
        vTargetTable.setDragMode(com.vaadin.ui.Table.TableDragMode.ROW);
        vTargetTable.setDropHandler(new DropHandler() {
            @Override
            public void drop(DragAndDropEvent dropEvent) {
                DataBoundTransferable transferable = (DataBoundTransferable) dropEvent.getTransferable();

                Object itemId = transferable.getItemId();
                Container sourceContainer = transferable.getSourceContainer();
                Company entity = convertToEntity(sourceContainer.getItem(itemId), Company.class);
                if (entity == null) {
                    return;
                }

                addAction.addEntity(entity);
            }

            @Override
            public AcceptCriterion getAcceptCriterion() {
                return new And(AbstractSelect.AcceptItem.ALL);
            }
        });
    }

    @Nullable
    private <T extends Entity> T convertToEntity(@Nullable Item item, Class<T> entityClass) {
        if (!(item instanceof ItemWrapper)) {
            return null;
        }
        Entity entity = ((ItemWrapper) item).getItem();
        if (!entityClass.isAssignableFrom(entity.getClass())) {
            return null;
        }
        return (T) entity;
    }
}
