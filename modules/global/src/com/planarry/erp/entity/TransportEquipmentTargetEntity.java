package com.planarry.erp.entity;

import com.haulmont.cuba.core.entity.Entity;

import java.util.UUID;

public interface TransportEquipmentTargetEntity extends Entity<UUID> {

    Boolean getIsFree();

    void setIsFree(Boolean free);

}
