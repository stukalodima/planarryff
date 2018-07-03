
package com.planarry.erp.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

import java.util.Date;

@MetaClass(name = "erp$JourneyTransport")
public class JourneyTransport extends BaseUuidEntity implements ErpEntity {
    private static final long serialVersionUID = 8087176048369560069L;

    @MetaProperty
    private JourneyData journeyData;

    @MetaProperty
    private Transport transport;

    @MetaProperty
    private String track;

    @MetaProperty
    private Double distance;

    @MetaProperty
    private Integer time;

    @MetaProperty
    private Double attractingPrice;

    @MetaProperty
    private Double transportationPrice;

    @MetaProperty
    private Date startDate;

    @MetaProperty
    private Date endDate;

    @MetaProperty
    private Journey journey;


    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public Transport getTransport() {
        return transport;
    }


    public void setTrack(String track) {
        this.track = track;
    }

    public String getTrack() {
        return track;
    }


    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getDistance() {
        return isNullValue(distance);
    }

    public void setAttractingPrice(Double attractingPrice) {
        this.attractingPrice = attractingPrice;
    }

    public Double getAttractingPrice() {
        return isNullValue(attractingPrice);
    }

    public void setTransportationPrice(Double transportationPrice) {
        this.transportationPrice = transportationPrice;
    }

    public Double getTransportationPrice() {
        return isNullValue(transportationPrice);
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Journey getJourney() {
        return journey;
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public JourneyData getJourneyData() {
        return journeyData;
    }

    public void setJourneyData(JourneyData journeyData) {
        this.journeyData = journeyData;
    }
}