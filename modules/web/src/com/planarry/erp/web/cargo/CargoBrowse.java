
package com.planarry.erp.web.cargo;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.BaseAction;
import com.haulmont.cuba.gui.components.actions.CreateAction;
import com.haulmont.cuba.gui.components.actions.EditAction;
import com.haulmont.cuba.gui.data.GroupDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.planarry.erp.entity.AccessToCargo;
import com.planarry.erp.entity.Cargo;
import com.planarry.erp.entity.Company;
import com.planarry.erp.web.cargo.access.CargoAccessible;
import com.planarry.erp.web.utils.ControllerAssistant;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.stream.Collectors;

public class CargoBrowse extends AbstractLookup {

    @Inject
    private GroupDatasource<Cargo, UUID> cargoesDs;

    @Named("cargoesTable.edit")
    private EditAction cargoesTableEdit;

    @Named("cargoesTable.create")
    private CreateAction cargoesTableCreate;

    @Named("shareToAllBtn")
    private Button shareToAllBtn;

    @Named("shareToCompaniesBtn")
    private Button shareToCompaniesBtn;

    @Named(DataManager.NAME)
    private DataManager dataManager;

    @Named(ControllerAssistant.NAME)
    private ControllerAssistant controllerAssistant;

    @Named(ComponentsFactory.NAME)
    private ComponentsFactory componentsFactory;

    @Override
    public void init(Map<String, Object> params) {
        if (controllerAssistant.checkUserStatusAndCompany(params)) {
            showNotification(messages.getMessage(getClass(), "message.cantLoadCargo"), NotificationType.WARNING);
        }
        super.init(params);

        addCargoesDsItemChangeListener();
        cargoesTableEdit.setWindowId("erp$Cargo.create");
        cargoesTableCreate.setWindowId("erp$Cargo.create");
    }

    private void addCargoesDsItemChangeListener() {
        cargoesDs.addItemChangeListener(e -> {
            if (e.getItem() != null) {
                if (!controllerAssistant.getCurrentUser().getIsAdmin() && !e.getItem().getCompany().equals(controllerAssistant.getUserCompany())) {
                    shareToCompaniesBtn.setEnabled(false);
                    shareToAllBtn.setEnabled(false);
                } else {
                    shareToCompaniesBtn.setEnabled(true);
                    shareToAllBtn.setEnabled(true);
                }
                defineShareBtnCaption();
            }
        });
    }

    public void onShareToAll() {
        Cargo cargo = cargoesDs.getItem();
        clearAccessDS(cargo);
        cargo.setAccessibleToAll(!cargo.getAccessibleToAll());
        cargo.setAccessibleToOwner(!cargo.getAccessibleToAll());
        defineShareBtnCaption();
        dataManager.commit(cargo);
        cargoesDs.refresh();
    }

    public void onShareToCompanies() {
        Cargo cargo = dataManager.reload(cargoesDs.getItem(), "cargo-view");
        List<AccessToCargo> accesses = new ArrayList<>(cargo.getAccesses());
        List<Company> companies = accesses.stream().map(AccessToCargo::getCompany).collect(Collectors.toList());
        Map<String, Object> params = ParamsMap.of("cargo", cargo, "accesses", accesses, "companies", companies);
        CargoAccessible window = (CargoAccessible) openWindow("cargoAccessible", WindowManager.OpenType.DIALOG, params);
        window.addCloseWithCommitListener(() -> {
            List<AccessToCargo> accessToCargoes = cargo.getAccesses();
            CommitContext commitContext = new CommitContext();
            accessToCargoes.forEach(item -> {
                if (!window.getNewAccesses().contains(item)) {
                    commitContext.addInstanceToRemove(item);
                    accesses.remove(item);
                }
            });

            window.getNewAccesses().forEach(item -> {
                if (accesses.stream().noneMatch(accessToCargo -> accessToCargo.getCompany().equals(item.getCompany()))) {
                    commitContext.addInstanceToCommit(item);
                    accesses.add(item);
                }
            });

            cargo.setAccessibleToOwner(accesses.size() == 0);
            cargo.setAccessibleToAll(false);
            defineShareBtnCaption();

            commitContext.addInstanceToCommit(cargo);
            dataManager.commit(commitContext);
            cargoesDs.refresh();
        });
    }

    private void defineShareBtnCaption() {
        String captionMsg = cargoesDs.getItem().getAccessibleToAll() ? "unshare" : "shareToAll";
        shareToAllBtn.setCaption(messages.getMessage(getClass(), captionMsg));
    }

    private void clearAccessDS(Cargo cargo) {
        cargo = dataManager.reload(cargo, "cargo-view");

        if (cargo.getAccesses().size() > 0) {
            CommitContext commitContext = new CommitContext();
            cargo.getAccesses().forEach(commitContext::addInstanceToRemove);
            dataManager.commit(commitContext);
        }
    }

    public Component generateDeliveryDateColumn(Entity entity) {
        Label label = componentsFactory.createComponent(Label.class);
        Cargo cargo = (Cargo) entity;
        if (cargo.getDeliveryPoints().size() > 0) {
            label.setValue(cargo.getDeliveryPoints().get(cargo.getDeliveryPoints().size() - 1).getDeliveryDate());
        }
        return label;
    }

    public Component generateDeliveryAddressColumn(Entity entity) {
        Label label = componentsFactory.createComponent(Label.class);
        Cargo cargo = (Cargo) entity;
        if (cargo.getDeliveryPoints().size() > 0) {
            label.setValue(cargo.getDeliveryPoints().get(cargo.getDeliveryPoints().size() - 1).getPoint().getAddress());
        }
        return label;
    }

    public Component generateAccessColumn(Entity entity) {
        Cargo cargo = (Cargo) entity;
        String ownerMessage = messages.getMessage(getClass(), "ownerAccessMessageFull_1") + " " + cargo.getCompany().getName()
                + messages.getMessage(getClass(), "ownerAccessMessageFull_2");
        return controllerAssistant.generateAccessColumn(cargo.getAccessibleToOwner(), cargo.getAccessibleToAll(), cargo.getAccesses(), getFrame(),
                ownerMessage, messages.getMessage(getClass(), "allAccessMessageFull"));
    }
}