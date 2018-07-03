package com.planarry.erp.web.category;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.planarry.erp.entity.Category;
import com.planarry.erp.entity.CategoryArea;
import com.planarry.erp.entity.Company;
import com.planarry.erp.entity.PolygonMap;
import com.planarry.erp.web.utils.ControllerAssistant;

import javax.inject.Named;
import java.util.UUID;

public class CategoryEdit extends AbstractEditor<Category> {

    @Named("categoryAreasDs")
    private CollectionDatasource<CategoryArea, UUID> categoryAreasDs;

    @Named("categoryDs")
    private Datasource<Category> categoryDs;

    @Named("pickerField")
    private PickerField pickerField;

    @Named("fieldGroup.company")
    private PickerField companyField;

    @Named("hourTextField")
    private TextField hourTextField;

    @Named("kilometerTextField")
    private TextField kilometerTextField;

    @Named("supplyTextField")
    private TextField supplyTextField;

    @Named("areaEntranceTextField")
    private TextField areaEntranceTextField;

    @Named("areaExitTextField")
    private TextField areaExitTextField;

    @Named("addAreaBtn")
    private Button addAreaBtn;

    @Named("saveAreaBtn")
    private Button saveAreaBtn;

    @Named("areasTable")
    private Table<CategoryArea> areasTable;

    @Named("areasTable.remove")
    private RemoveAction areasTableRemove;

    @Named(Metadata.NAME)
    private Metadata metadata;

    @Named(ControllerAssistant.NAME)
    private ControllerAssistant controllerAssistant;

    private CategoryArea currentCategoryArea;

    @Override
    protected void postValidate(ValidationErrors errors) {
        super.postValidate(errors);
        if (currentCategoryArea != null && pickerField.getValue() == null){
            errors.add(messages.getMessage(getClass(), "requiredPickerFieldMessage"));
        }
    }

    @Override
    protected void postInit() {
        super.postInit();
        pickerField.addClearAction();
        areasTable.setItemClickAction(new BaseAction("doubleClick").withHandler(e -> {
            currentCategoryArea = areasTable.getSingleSelected();
            pickerField.setRequired(true);
            pickerField.setValue(currentCategoryArea.getPolygon());
            hourTextField.setValue(currentCategoryArea.getCostHour());
            kilometerTextField.setValue(currentCategoryArea.getCostKilometer());
            supplyTextField.setValue(currentCategoryArea.getCostSupply());
            areaEntranceTextField.setValue(currentCategoryArea.getCostEntrancePenalty());
            areaExitTextField.setValue(currentCategoryArea.getCostExitPenalty());
            setButtonsVisibility(false);
        }));

        pickerField.addValueChangeListener(e -> {
            setButtonsEnabled(e.getValue() != null);
        });
        Company userCompany = controllerAssistant.getUserCompany();
        if (userCompany != null) {
            controllerAssistant.addLookupAction(companyField, "erp$Company.browse", ParamsMap.of("company", userCompany));
        }

    }

    public void onSaveArea() {
        if (areasNoneMatch(pickerField.getValue()) || pickerField.getValue().equals(currentCategoryArea.getPolygon())) {
            updateCategoryArea(currentCategoryArea);
            setButtonsVisibility(true);
            clearCategoryFields();
            currentCategoryArea = null;
            pickerField.setRequired(false);
        } else {
            showNotification(messages.getMessage(getClass(), "message.areasExist"));
        }
    }

    public void onAddArea() {
        if (areasNoneMatch(pickerField.getValue())) {
            CategoryArea categoryArea = metadata.create(CategoryArea.class);
            categoryArea = updateCategoryArea(categoryArea);
            categoryAreasDs.addItem(categoryArea);
            clearCategoryFields();
        } else {
            showNotification(messages.getMessage(getClass(), "message.areasExist"));
        }
    }

    private CategoryArea updateCategoryArea(CategoryArea categoryArea) {
        categoryArea.setCategory(getItem());
        categoryArea.setPolygon(pickerField.getValue());
        categoryArea.setCostHour(hourTextField.getValue());
        categoryArea.setCostKilometer(kilometerTextField.getValue());
        categoryArea.setCostSupply(supplyTextField.getValue());
        categoryArea.setCostEntrancePenalty(areaEntranceTextField.getValue());
        categoryArea.setCostExitPenalty(areaExitTextField.getValue());
        return categoryArea;
    }

    private boolean areasNoneMatch(PolygonMap polygonMap){
        return categoryAreasDs.getItems().stream().noneMatch(categoryArea -> categoryArea.getPolygon().equals(polygonMap));
    }

    private void clearCategoryFields() {
        hourTextField.setValue(null);
        pickerField.setValue(null);
        kilometerTextField.setValue(null);
        supplyTextField.setValue(null);
        areaEntranceTextField.setValue(null);
        areaExitTextField.setValue(null);
    }

    private void setButtonsEnabled(boolean enabled) {
        addAreaBtn.setEnabled(enabled);
        saveAreaBtn.setEnabled(enabled);
    }

    private void setButtonsVisibility(boolean isSaveAction) {
        addAreaBtn.setVisible(isSaveAction);
        saveAreaBtn.setVisible(!isSaveAction);
    }
}