package com.planarry.erp.entity;

import com.haulmont.cuba.core.entity.Entity;

import java.util.UUID;

public interface MonitoringEntity extends Entity<UUID> {

    void setFlag(Boolean flag);

    Boolean getFlag();
}
