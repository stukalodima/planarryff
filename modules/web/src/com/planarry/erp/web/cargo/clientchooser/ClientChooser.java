package com.planarry.erp.web.cargo.clientchooser;

import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.OptionsList;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.planarry.erp.entity.Company;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ClientChooser extends AbstractWindow {

    @Inject
    private OptionsList optionsList;

    @Inject
    private CollectionDatasource<Company, UUID> clientsDs;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        if (params.containsKey("clients")){
            List<Company> companyList = (List) params.get("clients");
            companyList.forEach(clientsDs::includeItem);
        }

        optionsList.setNullOptionVisible(false);
        optionsList.addValueChangeListener(e -> {
            clientsDs.setItem((Company) e.getValue());
            close(Window.CLOSE_ACTION_ID);
        });
    }

    public Company getChosenCompany(){
        return clientsDs.getItem();
    }

    public void cancel() {
        close(Window.CLOSE_ACTION_ID);
    }
}