package com.planarry.erp.web.transport;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.planarry.erp.entity.OrderedTableEntity;
import com.planarry.erp.entity.TransportEquipmentTargetEntity;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.And;
import com.vaadin.ui.AbstractSelect;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class TransportCompanion implements TransportEdit.Companion {

    @Override
    public <T extends TransportEquipmentTargetEntity, K extends OrderedTableEntity> void initDragAndDrop(Table<K> targetTable,
                                                                                                         Table<T> sourceTable,
                                                                                                         Class<T> entityClass,
                                                                                                         Class<K> wrapperClass,
                                                                                                         TransportEdit.TableAddAction addAction,
                                                                                                         TransportEdit.TableRemoveAction removeAction) {

        com.vaadin.ui.Table vSourceTable = sourceTable.unwrap(com.vaadin.ui.Table.class);
        vSourceTable.setDragMode(com.vaadin.ui.Table.TableDragMode.ROW);
        vSourceTable.setDropHandler(new DropHandler() {
            @Override
            public void drop(DragAndDropEvent dropEvent) {
                DataBoundTransferable transferable = (DataBoundTransferable) dropEvent.getTransferable();

                Object itemId = transferable.getItemId();
                Container sourceContainer = transferable.getSourceContainer();
                K entity = convertToEntity(sourceContainer.getItem(itemId), wrapperClass);
                if (entity == null) {
                    return;
                }
                Set<K> removedItems = new HashSet<>();
                removedItems.add(entity);
                removeAction.removeEntity(removedItems, targetTable.getDatasource(), sourceTable.getDatasource());
            }

            @Override
            public AcceptCriterion getAcceptCriterion() {
                return new And(AbstractSelect.AcceptItem.ALL);
            }
        });

        com.vaadin.ui.Table vTargetTable = targetTable.unwrap(com.vaadin.ui.Table.class);
        vTargetTable.setDragMode(com.vaadin.ui.Table.TableDragMode.ROW);
        vTargetTable.setDropHandler(new DropHandler() {
            @Override
            public void drop(DragAndDropEvent dropEvent) {
                DataBoundTransferable transferable = (DataBoundTransferable) dropEvent.getTransferable();

                Object itemId = transferable.getItemId();
                Container sourceContainer = transferable.getSourceContainer();
                T entity = convertToEntity(sourceContainer.getItem(itemId), entityClass);
                if (entity == null) {
                    return;
                }

                addAction.addEntity(targetTable.getDatasource(), sourceTable.getDatasource(), entity, wrapperClass);
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
