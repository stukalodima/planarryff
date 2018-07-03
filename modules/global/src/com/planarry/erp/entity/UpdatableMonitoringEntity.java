package com.planarry.erp.entity;

public interface UpdatableMonitoringEntity extends MonitoringEntity {

    Transport getTransport();

    void setTransport(Transport transport);
}
