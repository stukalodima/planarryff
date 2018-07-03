package com.planarry.erp.web.transporttype;

import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.PickerField;
import com.planarry.erp.entity.TransportType;

import javax.inject.Named;


public class TransportTypeEdit extends AbstractEditor<TransportType> {

    @Named("fieldGroup.category")
    private PickerField categoryField;

    @Override
    protected void postInit() {
        categoryField.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                TransportType category = (TransportType) e.getValue();
                if (category.equals(getItem())){
                    showNotification(messages.getMessage(getClass(), "message.notEqualsTypeRequired"), NotificationType.WARNING);
                    categoryField.setValue(null);
                } else if (!category.isRoot()){
                    showNotification(messages.getMessage(getClass(), "message.baseTypeRequired"), NotificationType.WARNING);
                    categoryField.setValue(null);
                }
            }
        });
    }
}
