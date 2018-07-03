package com.planarry.erp.web.category;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.planarry.erp.entity.Category;
import com.planarry.erp.entity.CategoryArea;
import com.planarry.erp.entity.Company;
import com.planarry.erp.entity.ExtUser;
import com.planarry.erp.web.utils.ControllerAssistant;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

public class CategoryBrowse extends AbstractLookup {

    @Named("categoriesDs")
    private GroupDatasource<Category, UUID> categoriesDs;

    @Named(ControllerAssistant.NAME)
    private ControllerAssistant controllerAssistant;

    @Named("categoriesTable.copy")
    private Action categoriesTableCopy;

    @Named(Metadata.NAME)
    private Metadata metadata;

    @Named(DataManager.NAME)
    private DataManager dataManager;

    @Override
    public void init(Map<String, Object> params) {
        Company userCompany = controllerAssistant.getUserCompany();
        if (userCompany != null) {
            params.put("company", userCompany);
        }
        //show copy btn to admin
        ExtUser user = controllerAssistant.getCurrentUser();
        if (user.getIsAdmin()){
            categoriesTableCopy.setVisible(true);
        }
        super.init(params);
    }

    public void onCopy() {
        Category category = dataManager.reload(categoriesDs.getItem(), "category-view");
        Category copy = metadata.create(Category.class);
        copy.setCostHour(category.getCostHour());
        copy.setCostKilometer(category.getCostKilometer());
        copy.setCostSupply(category.getCostSupply());
        copy.setMinHourNumber(category.getMinHourNumber());
        copy.setNumberOfPallets(category.getNumberOfPallets());
        copy.setName(category.getName());
        copy.setVolume(category.getVolume());
        copy.setWeight(category.getWeight());

        if (category.getAreas() != null){
            List<CategoryArea> areasCopy = new ArrayList<>();
            category.getAreas().forEach(item ->{
                CategoryArea area = metadata.create(CategoryArea.class);
                area.setCostExitPenalty(item.getCostExitPenalty());
                area.setCostEntrancePenalty(item.getCostEntrancePenalty());
                area.setCostKilometer(item.getCostKilometer());
                area.setCostHour(item.getCostHour());
                area.setCostSupply(item.getCostSupply());
                area.setPolygon(item.getPolygon());
                area.setCategory(copy);
                areasCopy.add(area);
                copy.setAreas(areasCopy);
            });
        }
        AbstractEditor abstractEditor = openEditor(copy, WindowManager.OpenType.DIALOG);
        abstractEditor.addCloseWithCommitListener(categoriesDs::refresh);
    }
}