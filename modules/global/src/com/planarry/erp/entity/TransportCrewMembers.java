/*
 * Copyright(c) 2017 Planarry
 */
package com.planarry.erp.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.validation.constraints.NotNull;

@Table(name = "ERP_TRANSPORT_CREW_MEMBERS")
@Entity(name = "erp$TransportCrewMembers")
public class TransportCrewMembers extends StandardEntity implements OrderedTableEntity {
    private static final long serialVersionUID = -2425649176964788747L;

    @NotNull
    @Column(name = "ORDER_NUMBER", nullable = false)
    private Integer order;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TRANSPORT_ID")
    private Transport transport;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EMPLOYEE_ID")
    @OnDeleteInverse(DeletePolicy.DENY)
    private Employee employee;


    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public Transport getTransport() {
        return transport;
    }


    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getOrder() {
        return order;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }

    @Override
    public void setTargetEntity(TransportEquipmentTargetEntity entity) {
        setEmployee((Employee) entity);
    }

    @Override
    public TransportEquipmentTargetEntity getTargetEntity() {
        return getEmployee();
    }
}