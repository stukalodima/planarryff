package com.planarry.erp.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@MetaClass(name = "erp$JourneyData")
public class JourneyData extends BaseUuidEntity {
    private static final long serialVersionUID = 5756628274110580427L;

    private boolean isCorrect;

    @MetaProperty
    private Integer minHourNumber = 0; //in minutes

    @MetaProperty
    private Double baseCostKilometer = 0.;

    @MetaProperty
    private Double baseCostHour = 0.;

    @MetaProperty
    private Double baseCostSupply = 0.;


    private Double areaSupplyPrice = 0.;

    @MetaProperty
    private Double roadDist = 0.;

    @MetaProperty
    private Integer roadTime = 0;

    @MetaProperty
    private Integer loadingTime = 0;

    @MetaProperty
    private Integer unloadingTime = 0;

    @MetaProperty
    private Integer totalTime = 0;

    @MetaProperty
    private Integer additionalTime = 0;

    @MetaProperty
    private Double returnDistance  = 0.;

    @MetaProperty
    private Double returnPrice = 0.;

    @MetaProperty
    private Double totalSupplyPrice = 0.;

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
    private Double additionalTimePrice = 0.;

    @MetaProperty
    private Double roadTimePrice = 0.;

    @MetaProperty
    private Double roadDistPrice = 0.;

    @MetaProperty
    private Double totalPrice = 0.;

    @MetaProperty
    private List<String> routeCoordinates = new ArrayList<>(); //look like List<"lat,lon">

    @MetaProperty
    private List<AreaData> areaData = new ArrayList<>();

    @MetaProperty
    private String track;

    public void calcAllPrices(double ratio){
        areaData.forEach(item -> item.calcAllPrices(ratio));
        roadDist = areaData.stream().mapToDouble(AreaData::getRoadDist).sum();
        roadTime = areaData.stream().mapToInt(AreaData::getRoadTime).sum();
        loadingTime = areaData.stream().mapToInt(AreaData::getLoadingTime).sum();
        unloadingTime = areaData.stream().mapToInt(AreaData::getUnloadingTime).sum();
        totalTime = loadingTime + unloadingTime + roadTime;

        roadDistPrice = areaData.stream().mapToDouble(AreaData::getRoadDistPrice).sum();
        roadTimePrice = areaData.stream().mapToDouble(AreaData::getRoadTimePrice).sum();
        loadingPrice = areaData.stream().mapToDouble(AreaData::getLoadingPrice).sum();
        unloadingPrice = areaData.stream().mapToDouble(AreaData::getUnloadingPrice).sum();
        entrancePrice = areaData.stream().mapToDouble(AreaData::getEntrancePrice).sum();
        exitPrice = areaData.stream().mapToDouble(AreaData::getExitPrice).sum();
        returnPrice = returnDistance * baseCostKilometer  * ratio;

        totalTimePrice = loadingPrice + unloadingPrice + roadTimePrice;
        totalSupplyPrice = areaSupplyPrice * ratio;

        if (totalTime < minHourNumber){
            additionalTime = minHourNumber - totalTime;
            additionalTimePrice = additionalTime * baseCostHour * ratio;
        }

        totalPrice = additionalTimePrice + roadDistPrice + totalSupplyPrice + returnPrice + totalTimePrice + entrancePrice + exitPrice;
    }

    public AreaData getActualAreaData(Area area){
        AreaData areaData;

        Optional<AreaData> optional = this.areaData.stream().filter(
                item -> (area == null && item.getArea() == null) || item.getArea() != null && item.getArea().equals(area)
        ).findFirst();

        if (optional.isPresent()){
            areaData = optional.get();
        } else {
            areaData = new AreaData();
            areaData.setJourneyData(this);
            areaData.setArea(area);
            this.areaData.add(areaData);
        }
        return areaData;
    }

    public AreaData getZeroAreaData(){
        AreaData areaData;

        Optional<AreaData> optional = this.areaData.stream().filter(item ->  item.getArea() == null).findFirst();
        if (optional.isPresent()){
            areaData = optional.get();
        } else {
            areaData = new AreaData();
            areaData.setJourneyData(this);
            this.areaData.add(areaData);
        }
        return areaData;
    }

    public void setBaseCostValues(AreaHolder priceHolder){
        this.baseCostSupply = priceHolder.getTotalCostSupply();
        this.baseCostHour = priceHolder.getTotalCostHour();
        this.baseCostKilometer = priceHolder.getTotalCostKilometer();
        this.minHourNumber = priceHolder.getMinHourNumber() * 60; //in minutes
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public Double getRoadDist() {
        return roadDist;
    }

    public Integer getRoadTime() {
        return roadTime;
    }

    public Integer getLoadingTime() {
        return loadingTime;
    }

    public Integer getUnloadingTime() {
        return unloadingTime;
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

    public Double getTotalPrice() {
        return totalPrice;
    }

    public Double getRoadDistPrice() {
        return roadDistPrice;
    }

    public Double getReturnDistance() {
        return returnDistance;
    }

    public Double getReturnPrice() {
        return returnPrice;
    }

    public Double getRoadTimePrice() {
        return roadTimePrice;
    }

    public Double getTotalTimePrice() {
        return totalTimePrice;
    }

    public Double getTotalSupplyPrice() {
        return totalSupplyPrice;
    }

    public void setSupplyPrice(Double supplyPrice) {
        this.areaSupplyPrice = supplyPrice;
    }

    public void setReturnDistance(Double returnDistance) {
        this.returnDistance = returnDistance;
    }

    public void setReturnPrice(Double returnPrice) {
        this.returnPrice = returnPrice;
    }

    public List<String> getRouteCoordinates() {
        return routeCoordinates;
    }

    public void addRouteCoordinates(String routeCoordinates) {
        this.routeCoordinates.add(routeCoordinates);
    }

    public List<AreaData> getAreaData() {
        return areaData;
    }

    public Double getBaseCostKilometer() {
        return baseCostKilometer;
    }

    public void setBaseCostKilometer(Double baseCostKilometer) {
        this.baseCostKilometer = baseCostKilometer;
    }

    public Double getBaseCostHour() {
        return baseCostHour;
    }

    public void setBaseCostHour(Double baseCostHour) {
        this.baseCostHour = baseCostHour;
    }

    public Double getBaseCostSupply() {
        return baseCostSupply;
    }

    public void setBaseCostSupply(Double baseCostSupply) {
        this.baseCostSupply = baseCostSupply;
    }

    public Integer getMinHourNumber() {
        return minHourNumber;
    }

    public void setMinHourNumber(Integer minHourNumber) {
        this.minHourNumber = minHourNumber * 60;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public Integer getAdditionalTime() {
        return additionalTime;
    }

    public Double getAdditionalTimePrice() {
        return additionalTimePrice;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }
}