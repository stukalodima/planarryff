
package com.planarry.erp.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Column;

@Table(name = "ERP_JOURNEY_CARGO")
@Entity(name = "erp$JourneyCargo")
public class JourneyCargo extends StandardEntity {
    private static final long serialVersionUID = -2991862785318670220L;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "JOURNEY_ID")
    private Journey journey;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CARGO_ID")
    private Cargo cargo;

    @Column(name = "DRIVER_COMMENT")
    private String driverComment;

    public void setDriverComment(String driverComment) {
        this.driverComment = driverComment;
    }

    public String getDriverComment() {
        return driverComment;
    }


    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Cargo getCargo() {
        return cargo;
    }


    public void setJourney(Journey journey) {
        this.journey = journey;
    }

    public Journey getJourney() {
        return journey;
    }


}