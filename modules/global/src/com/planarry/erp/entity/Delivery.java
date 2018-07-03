
package com.planarry.erp.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import com.haulmont.cuba.core.entity.annotation.Listeners;
import com.haulmont.chile.core.annotations.NamePattern;

@Table(name = "ERP_DELIVERY")
@Entity(name = "erp$Delivery")
public class Delivery extends StandardEntity implements ErpEntity {
    private static final long serialVersionUID = 464279363981949550L;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CARGO_ID")
    private Cargo cargo;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "JOURNEY_ID")
    private Journey journey;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TRANSPORT_ID")
    private Transport transport;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CURRENCY_ID")
    private Currency currency;

    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL)
    private List<DeliveryComposition> deliveryCompositions;

    @NotNull
    @Column(name = "STATUS", nullable = false)
    private Integer status;

    @Column(name = "DISTANCE")
    private Double distance;

    @Column(name = "TRANSPORTATION_TIME")
    private Integer transportationTime;

    @Column(name = "TRANSPORTATION_PRICE")
    private Double transportationPrice;

    @Column(name = "APPROVED")
    private Boolean approved;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_DATE")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "START_ADDRESS")
    private String startAddress;

    @Column(name = "END_ADDRESS")
    private String endAddress;

    public void setStatus(EStatusItems status) {
        this.status = status == null ? null : status.getId();
    }

    public EStatusItems getStatus() {
        return status == null ? null : EStatusItems.fromId(status);
    }


    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }


    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }


    public void setTransportationTime(Integer transportationTime) {
        this.transportationTime = transportationTime;
    }

    public Integer getTransportationTime() {
        return transportationTime;
    }


    public void setDeliveryCompositions(List<DeliveryComposition> deliveryCompositions) {
        this.deliveryCompositions = deliveryCompositions;
    }

    public List<DeliveryComposition> getDeliveryCompositions() {
        return deliveryCompositions;
    }

    public void addDeliveryComposition(DeliveryComposition deliveryComposition) {
        this.deliveryCompositions.add(deliveryComposition);
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

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getDistance() {
        return distance;
    }

    public void setTransportationPrice(Double transportationPrice) {
        this.transportationPrice = transportationPrice;
    }

    public Double getTransportationPrice() {
        return isNullValue(transportationPrice);
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Boolean getApproved() {
        return isNullValue(approved);
    }


}