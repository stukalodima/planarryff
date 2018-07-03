
package com.planarry.erp.entity;

import javax.persistence.*;

import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.StandardEntity;

import java.util.Date;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import java.util.List;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s|journeyNumber")
@Table(name = "ERP_JOURNEY")
@Entity(name = "erp$Journey")
public class Journey extends StandardEntity implements ErpEntity, MonitoringEntity, UpdatableMonitoringEntity {
    private static final long serialVersionUID = 8539781194604029239L;

    @Column(name = "JOURNEY_NUMBER", length = 15)
    private String journeyNumber;

    @Column(name = "FINAL_PRICE")
    private Double finalPrice;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_DATE")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "COMMENT_")
    private String comment;

    @Column(name = "START_ADDRESS")
    private String startAddress;

    @Column(name = "END_ADDRESS")
    private String endAddress;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "journey")
    private List<Delivery> deliveries;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "journey")
    private List<JourneyCargo> journeyCargoes;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "journey")
    private List<JourneyComposition> journeyComposition;

    @NotNull
    @OnDeleteInverse(DeletePolicy.DENY)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TRANSPORT_ID")
    private Transport transport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID")
    private Employee employee;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CURRENCY_ID")
    private Currency currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FREIGHTER_ID")
    private Company freighter;

    @Column(name = "TRANSPORTATION_DISTANCE")
    private Double transportationDistance;

    @Column(name = "TRANSPORTATION_TIME")
    private Integer transportationTime;

    @Lob
    @Column(name = "TRACK")
    private String track;

    @Column(name = "ATTRACTING_PRICE")
    private Double attractingPrice;

    @Column(name = "TRANSPORTATION_PRICE")
    private Double transportationPrice;

    @NotNull
    @Column(name = "RESIDUAL_WEIGHT", nullable = false)
    private Double residualWeight;

    @NotNull
    @Column(name = "RESIDUAL_VOLUME", nullable = false)
    private Double residualVolume;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "RATING")
    private Integer rating;

    @NotNull
    @Column(name = "MANUAL_JOURNEY", nullable = false)
    private Boolean manualJourney = false;

    @NotNull
    @Column(name = "APPROVED", nullable = false)
    private Boolean approved = false;

    @Transient
    @MetaProperty
    private Boolean flag = false;


    public void setFinalPrice(Double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public Double getFinalPrice() {
        return finalPrice == null ? getTransportationPrice() : finalPrice;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }


    public Integer getTransportationTime() {
        return isNullValue(transportationTime);
    }

    public void setTransportationTime(Integer transportationTime) {
        this.transportationTime = transportationTime;
    }


    public void setFreighter(Company freighter) {
        this.freighter = freighter;
    }

    public Company getFreighter() {
        return freighter;
    }


    public void setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    public List<Delivery> getDeliveries() {
        return deliveries;
    }


    public Boolean getApproved() {
        return isNullValue(approved);
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public void setResidualWeight(Double residualWeight) {
        this.residualWeight = residualWeight;
    }

    public Double getResidualWeight() {
        return residualWeight;
    }

    public void setResidualVolume(Double residualVolume) {
        this.residualVolume = residualVolume;
    }

    public Double getResidualVolume() {
        return residualVolume;
    }


    public void setJourneyNumber(String journey_number) {
        this.journeyNumber = journey_number;
    }

    public String getJourneyNumber() {
        return journeyNumber;
    }


    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }


    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getRating() {
        return rating;
    }


    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Boolean getFlag() {
        return flag;
    }


    public void setManualJourney(Boolean manualJourney) {
        this.manualJourney = manualJourney;
    }

    public Boolean getManualJourney() {
        return manualJourney;
    }


    public void setTrack(String track) {
        this.track = track;
    }

    public String getTrack() {
        return track;
    }

    public void setTransportationDistance(Double transportationDistance) {
        this.transportationDistance = transportationDistance;
    }

    public Double getTransportationDistance() {
        return isNullValue(transportationDistance);
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public Transport getTransport() {
        return transport;
    }


    public Double getTransportationPrice() {
        return isNullValue(transportationPrice);
    }

    public void setTransportationPrice(Double transportationPrice) {
        this.transportationPrice = transportationPrice;
    }


    public void setJourneyCargoes(List<JourneyCargo> journeyCargoes) {
        this.journeyCargoes = journeyCargoes;
    }

    public List<JourneyCargo> getJourneyCargoes() {
        return journeyCargoes;
    }


    public void setJourneyComposition(List<JourneyComposition> journeyComposition) {
        this.journeyComposition = journeyComposition;
    }

    public List<JourneyComposition> getJourneyComposition() {
        return journeyComposition;
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

    public void setStatus(EStatusItems status) {
        this.status = status == null ? null : status.getId();
    }

    public EStatusItems getStatus() {
        return status == null ? null : EStatusItems.fromId(status);
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

    public void setAttractingPrice(Double attractingPrice) {
        this.attractingPrice = attractingPrice;
    }

    public Double getAttractingPrice() {
        return attractingPrice;
    }



    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Currency getCurrency() {
        return currency;
    }


}