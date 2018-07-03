/*
 * Copyright(c) 2017 Planarry
 */
package com.planarry.erp.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.StandardEntity;
import javax.persistence.Column;

/**
* @author Dima Stukalo
*/
@Table(name = "ERP_TRANSPORT_AREA")
@Entity(name = "erp$TransportArea")
public class TransportArea extends StandardEntity implements ErpEntity, Area {
    private static final long serialVersionUID = -4454239874354228616L;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TRANSPORT_ID")
    private Transport transport;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "POLYGON_ID")
    private PolygonMap polygon;

    @Column(name = "COST_KILOMETER")
    private Double costKilometer;

    @Column(name = "COST_HOUR")
    private Double costHour;

    @Column(name = "COST_SUPPLY")
    private Double costSupply;

    @Column(name = "COST_ENTRANCE_PENALTY")
    private Double costEntrancePenalty;

    @Column(name = "COST_EXIT_PENALTY")
    private Double costExitPenalty;


    public Double getCostEntrancePenalty() {
        return isNullValue(costEntrancePenalty);
    }

    public void setCostEntrancePenalty(Double costEntrancePenalty) {
        this.costEntrancePenalty = costEntrancePenalty;
    }

    public Double getCostExitPenalty() {
        return isNullValue(costExitPenalty);
    }

    public void setCostExitPenalty(Double costExitPenalty) {
        this.costExitPenalty = costExitPenalty;
    }

    public void setPolygon(PolygonMap polygon) {
        this.polygon = polygon;
    }

    public PolygonMap getPolygon() {
        return polygon;
    }


    public void setCostHour(Double costHour) {
        this.costHour = costHour;
    }

    public Double getCostHour() {
        return isNullValue(costHour);
    }

    public void setCostSupply(Double costSupply) {
        this.costSupply = costSupply;
    }

    public Double getCostSupply() {
        return isNullValue(costSupply);
    }


    public void setCostKilometer(Double costKilometer) {
        this.costKilometer = costKilometer;
    }

    public Double getCostKilometer() {
        return isNullValue(costKilometer);
    }


    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public Transport getTransport() {
        return transport;
    }


}