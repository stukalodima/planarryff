/*
 * Copyright(c) 2017 Planarry
 */
package com.planarry.erp.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

/**
* @author Aleksandr Iushko
*/
@Table(name = "ERP_MUTUAL_SETTLEMENTS")
@Entity(name = "erp$MutualSettlements")
public class MutualSettlements extends BaseUuidEntity {
    private static final long serialVersionUID = 2973558892469004496L;

    @NotNull
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TRANSPORT_OWNER_ID")
    private Company transportOwner;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(name = "PAY_DATE", nullable = false)
    private Date payDate;

    @NotNull
    @Column(name = "VALUE_", nullable = false)
    private Double value;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "JOURNEY_ID")
    private Journey journey;

    public void setTransportOwner(Company transportOwner) {
        this.transportOwner = transportOwner;
    }

    public Company getTransportOwner() {
        return transportOwner;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }

    public Journey getJourney() {
        return journey;
    }


}