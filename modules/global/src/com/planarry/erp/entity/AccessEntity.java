package com.planarry.erp.entity;

import com.haulmont.cuba.core.entity.Entity;

import java.util.UUID;

public interface AccessEntity extends Entity<UUID> {

    Company getCompany();

    void setCompany(Company company);
}
