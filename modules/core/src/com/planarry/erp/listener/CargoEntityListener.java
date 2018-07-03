
package com.planarry.erp.listener;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;
import com.planarry.erp.entity.Cargo;
import com.planarry.erp.entity.EStatusItems;
import com.planarry.erp.service.CargoService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component("erp_CargoEntityListener")
public class CargoEntityListener implements BeforeUpdateEntityListener<Cargo> {

    @Inject
    private CargoService cargoService;

    @Override
    public void onBeforeUpdate(Cargo entity, EntityManager entityManager) {
        EStatusItems status = entity.getStatus();
        if (status != null && (status.equals(EStatusItems.approved) || status.equals(EStatusItems.done))){
            cargoService.startBpmNotification(entity);
        }
    }


}