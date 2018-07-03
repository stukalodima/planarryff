
package com.planarry.erp.web.constant;

import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.TextField;
import com.planarry.erp.entity.Constant;
import com.planarry.erp.entity.EConstantTypeItems;

import javax.inject.Named;
import java.util.Arrays;
import java.util.List;

public class ConstantEdit extends AbstractEditor<Constant> {

    @Named("fieldGroup.valueType")
    private LookupField valueTypeField;
    @Named("logicalField")
    private LookupField logicalField;
    @Named("doubleTextField")
    private TextField doubleTextField;
    @Named("intTextField")
    private TextField intTextField;
    @Named("stringTextField")
    private TextField stringTextField;

    private List<Field> fieldList;


    @Override
    protected void initNewItem(Constant item) {
        super.initNewItem(item);
        intTextField.setRequired(true);
        intTextField.setVisible(true);
    }

    @Override
    protected void postInit() {
        super.postInit();
        logicalField.setOptionsList(Arrays.asList(true, false));
        fieldList = Arrays.asList(doubleTextField, intTextField, logicalField, stringTextField);
        if (!PersistenceHelper.isNew(getItem())){
            initValueField(getItem().getValueType());
        }
        addFieldValueChangeListener(intTextField);
        addFieldValueChangeListener(doubleTextField);
        addFieldValueChangeListener(logicalField);
        addFieldValueChangeListener(stringTextField);
        addValueTypeFieldValueChangeListener();
    }

    private void initValueField(EConstantTypeItems constant){
        switch (constant){
            case integer:
                changeFieldStates(intTextField);
                intTextField.setValue(Integer.valueOf(getItem().getValue()));
                break;
            case fractional:
                changeFieldStates(doubleTextField);
                doubleTextField.setValue(Double.valueOf(getItem().getValue()));
                break;
            case string:
                changeFieldStates(stringTextField);
                stringTextField.setValue(getItem().getValue());
                break;
            case logical:
                changeFieldStates(logicalField);
                logicalField.setValue(Boolean.valueOf(getItem().getValue()));
                break;
        }
    }

    private void addValueTypeFieldValueChangeListener(){
        valueTypeField.addValueChangeListener(e -> {
            if (e.getValue() != null){
                switch ((EConstantTypeItems) e.getValue()){
                    case integer:
                        changeFieldStates(intTextField);
                        break;
                    case fractional:
                        changeFieldStates(doubleTextField);
                        break;
                    case string:
                        changeFieldStates(stringTextField);
                        break;
                    case logical:
                        changeFieldStates(logicalField);
                        break;
                }
            }
        });
    }

    private void addFieldValueChangeListener(Field field){
        field.addValueChangeListener(e ->{
           if (e.getValue() != null) {
               getItem().setValue(String.valueOf(e.getValue()));
           } else {
               getItem().setValue(null);
           }
        });
    }

    private void changeFieldStates(Field field){
        field.setValue(null);
        field.setVisible(true);
        field.setRequired(true);
        for (Field f : fieldList) {
            if (f != field){
                f.setRequired(false);
                f.setVisible(false);
            }
        }
    }
}