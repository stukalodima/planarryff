package com.planarry.erp.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

@MetaClass(name = "erp$AreaData")
public class AreaData extends BaseUuidEntity {
    private static final long serialVersionUID = 7668103379455572731L;

    //@MetaProperty
    private Area area;

    @MetaProperty
    private JourneyData journeyData;

    @MetaProperty
    private Double roadDist = 0.;

    @MetaProperty
    private Integer roadTime = 0;

    @MetaProperty
    private Integer loadingTime = 0;

    @MetaProperty
    private Integer unloadingTime = 0;

    @MetaProperty
    private Integer entranceNumber = 0;

    @MetaProperty
    private Integer exitNumber = 0;

    @MetaProperty
    private Integer totalTime = 0;

    @MetaProperty
    private Double roadDistPrice = 0.;

    @MetaProperty
    private Double roadTimePrice = 0.;

    @MetaProperty
    private Double loadingPrice = 0.;

    @MetaProperty
    private Double unloadingPrice = 0.;

    @MetaProperty
    private Double entrancePrice = 0.;

    @MetaProperty
    private Double exitPrice = 0.;

    @MetaProperty
    private Double totalTimePrice = 0.;

    @MetaProperty
    private Double totalPrice = 0.;


    void calcAllPrices(double ratio){
        if (area == null){
            roadDistPrice = journeyData.getBaseCostKilometer() * roadDist * ratio;
            roadTimePrice = journeyData.getBaseCostHour() * roadTime * ratio;
            loadingPrice = journeyData.getBaseCostHour() * loadingTime * ratio;
            unloadingPrice = journeyData.getBaseCostHour() * unloadingTime * ratio;
        } else {
            roadDistPrice = area.getCostKilometer() * roadDist * ratio;
            roadTimePrice = area.getCostHour() * roadTime * ratio;
            loadingPrice = area.getCostHour() * loadingTime * ratio;
            unloadingPrice = area.getCostHour() * unloadingTime * ratio;
            entrancePrice = area.getCostEntrancePenalty() * entranceNumber * ratio;
            exitPrice = area.getCostExitPenalty() * exitNumber * ratio;
        }
        totalTime = loadingTime + unloadingTime + roadTime;
        totalTimePrice = loadingPrice + unloadingPrice + roadTimePrice;
        totalPrice = roadDistPrice + totalTimePrice + entrancePrice + exitPrice;
    }

    public void addEntranceNumber() {
        this.entranceNumber ++;
    }

    public void addExitNumber() {
        this.exitNumber ++;
    }

    public void addRoadDist(Double dist) {
        this.roadDist += dist;
    }

    public void addRoadTime(Integer time) {
        this.roadTime += time;
    }

    public void addLoadingTime(Integer loadingTime) {
        this.loadingTime += loadingTime;
    }

    public void addUnloadingTime(Integer unloadingTime) {
        this.unloadingTime += unloadingTime;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public Double getRoadDist() {
        return roadDist;
    }

    public Integer getRoadTime() {
        return roadTime;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public Integer getLoadingTime() {
        return loadingTime;
    }

    public Integer getUnloadingTime() {
        return unloadingTime;
    }

    public Double getRoadDistPrice() {
        return roadDistPrice;
    }

    public Double getRoadTimePrice() {
        return roadTimePrice;
    }

    public Double getLoadingPrice() {
        return loadingPrice;
    }

    public Double getUnloadingPrice() {
        return unloadingPrice;
    }

    public Double getEntrancePrice() {
        return entrancePrice;
    }

    public Double getExitPrice() {
        return exitPrice;
    }

    public Integer getEntranceNumber() {
        return entranceNumber;
    }

    public Integer getExitNumber() {
        return exitNumber;
    }

    public Double getTotalTimePrice() {
        return totalTimePrice;
    }

    public JourneyData getJourneyData() {
        return journeyData;
    }

    void setJourneyData(JourneyData journeyData) {
        this.journeyData = journeyData;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }
}
