package com.planarry.erp.entity;

import com.haulmont.cuba.core.entity.Entity;

import java.util.UUID;

public interface OrderedTableEntity extends Entity<UUID> {

    void setOrder(Integer order);

    Integer getOrder();

    void setTargetEntity(TransportEquipmentTargetEntity entity);

    TransportEquipmentTargetEntity getTargetEntity();

    void setTransport(Transport transport);

}
