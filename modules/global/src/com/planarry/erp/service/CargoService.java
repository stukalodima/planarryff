
package com.planarry.erp.service;

import com.planarry.erp.entity.Cargo;

public interface CargoService {
    String NAME = "erp_CargoService";

    void startBpmNotification(Cargo editedCargo);
}