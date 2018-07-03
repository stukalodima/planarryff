
package com.planarry.erp.service;

import com.haulmont.bpm.entity.ProcActor;
import com.haulmont.bpm.entity.ProcDefinition;
import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.bpm.service.ProcessRuntimeService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.planarry.erp.entity.Cargo;
import com.planarry.erp.entity.Delivery;
import com.planarry.erp.entity.EStatusItems;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

@Service(CargoService.NAME)
public class CargoServiceBean implements CargoService {

    private static final String PROCESS_CODE = "deliveryStatusChangedNotification";

    @Inject
    private DataManager dataManager;

    @Inject
    private Metadata metadata;

    @Inject
    private Messages messages;

    @Inject
    private ProcessRuntimeService processRuntimeService;

    @Override
    public void startBpmNotification(Cargo editedCargo) {
        Cargo oldCargo = dataManager.load(LoadContext.create(Cargo.class)
                .setId(editedCargo.getId())
                .setView("cargo-with-status-view"));

        //do it only when status changed on "approved" or "done"
        if (oldCargo != null && oldCargo.getStatus() != editedCargo.getStatus()) {
            ProcDefinition procDefinition = dataManager.load(
                    LoadContext.create(ProcDefinition.class)
                            .setQuery(new LoadContext.Query("select pd from bpm$ProcDefinition pd where pd.code = :code")
                                    .setParameter("code", PROCESS_CODE))
                            .setView("procDefinition-procInstanceEdit")
            );

            if (procDefinition != null) {
                ProcInstance procInstance = metadata.create(ProcInstance.class);
                procInstance.setProcDefinition(procDefinition);
                procInstance.setObjectEntityId(editedCargo.getId());
                procInstance.setEntityName(editedCargo.getMetaClass().getName());

                Set<ProcActor> procActors = new HashSet<>();

                ProcActor manager = metadata.create(ProcActor.class);
                manager.setUser(oldCargo.getManager().getUser());
                manager.setOrder(0);
                manager.setProcInstance(procInstance);
                manager.setProcRole(procDefinition.getProcRoles().get(0));

                procActors.add(manager);
                procInstance.setProcActors(procActors);

                Set<Entity> committed = dataManager.commit(new CommitContext()
                        .addInstanceToCommit(manager)
                        .addInstanceToCommit(procInstance));

                ProcInstance committedProcInstance = (ProcInstance) committed.stream()
                        .filter(p -> p.equals(procInstance))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Error"));

                processRuntimeService.startProcess(committedProcInstance, getMessage(oldCargo, editedCargo.getStatus()), new HashMap<>());
            }
        }
    }

    private String getMessage(Cargo cargo, EStatusItems newStatus) {
        String s = newStatus == EStatusItems.done
                ? messages.getMessage("com.planarry.erp.entity", "message.done")
                : messages.getMessage("com.planarry.erp.entity", "message.approved");

        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(messages.getMessage("com.planarry.erp.entity", "message.delivery"))
                .add(cargo.getClientCaption())
                .add(s)
                .add(messages.getMessage("com.planarry.erp.entity", "message.call"))
                .add(cargo.getPhone());

        return joiner.toString();
    }
}