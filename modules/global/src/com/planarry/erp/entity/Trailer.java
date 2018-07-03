package com.planarry.erp.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import java.util.List;

@NamePattern("%s|name")
@Table(name = "ERP_TRAILER", indexes = {
    @Index(name = "IDX_ERP_TRAILER_IS_FREE", columnList = "IS_FREE"),
    @Index(name = "IDX_ERP_TRAILER_ACCESSIBLE_TO_ALL", columnList = "ACCESSIBLE_TO_ALL"),
    @Index(name = "IDX_ERP_TRAILER_ACCESSIBLE_TO_OWNER", columnList = "ACCESSIBLE_TO_OWNER")
})
@Entity(name = "erp$Trailer")
public class Trailer extends StandardEntity implements ErpEntity, TransportEquipmentTargetEntity, AssistantAntity {
    private static final long serialVersionUID = -6368361395499812222L;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotNull
    @Column(name = "IDENT_NUMBER", nullable = false)
    private String identNumber;

    @Column(name = "GUID")
    private String guid;

    @NotNull
    @Column(name = "TRAILER_TYPE", nullable = false)
    private Integer type;

    @NotNull
    @Column(name = "DOWNLOAD_TYPE", nullable = false)
    private Integer downloadType;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "trailer")
    private List<AccessToTrailer> accesses;

    @Column(name = "ACCESSIBLE_TO_ALL")
    private Boolean accessibleToAll;

    @Column(name = "ACCESSIBLE_TO_OWNER")
    private Boolean accessibleToOwner;

    @NotNull
    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CURRENCY_BILLING_ID")
    private Currency currency;

    @NotNull
    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FORWARDER_ID")
    private Employee employee;

    @Column(name = "EMPTY_TRAILER_WEIGHT")
    private Double emptyTrailerWeight;

    @Column(name = "LENGTH")
    private Double length;

    @Column(name = "WIDTH")
    private Double width;

    @Column(name = "HEIGHT")
    private Double height;

    @Column(name = "COST_KILOMETER")
    private Double costKilometer;

    @Column(name = "COST_TON_KILOMETER")
    private Double costTonKilometer;

    @Column(name = "COST_HOUR")
    private Double costHour;

    @Column(name = "COST_SUPPLY")
    private Double costSupply;

    @Column(name = "MAX_WEIGHT")
    private Double maxWeight;

    @Column(name = "MAX_VOLUME")
    private Double maxVolume;

    @Column(name = "MIN_WEIGHT")
    private Double minWeight;

    @Column(name = "MIN_VOLUME")
    private Double minVolume;

    @Column(name = "LIMIT_WEIGHT")
    private Double limitWeight;

    @Column(name = "SERVICE_POINTS_WITH_RAMP")
    private Boolean servicePointWithRamp;

    @Column(name = "SERVICE_POINTS_WITHOUT_RAMP")
    private Boolean servicePointWithoutRamp;

    @Column(name = "TEMPERATURE_CONDITIONS")
    private Boolean temperatureConditions;

    @Column(name = "TEMPERATURE_RETENTION_TIME")
    private Integer temperatureRetentionTime;

    @Column(name = "LOW_TEMPERATURE")
    private Integer lowTemperature;

    @Column(name = "HIGH_TEMPERATURE")
    private Integer highTemperature;

    @Column(name = "BASE_COST_ATTRACTION")
    private Double baseCostAttraction;

    @Column(name = "EXTRA_COST_ATTRACTION")
    private Double extraCostAttraction;

    @NotNull
    @Column(name = "IS_FREE", nullable = false)
    private Boolean isFree = true;


    public void setCostTonKilometer(Double costTonKilometer) {
        this.costTonKilometer = costTonKilometer;
    }

    public Double getCostTonKilometer() {
        return isNullValue(costTonKilometer);
    }


    public void setAccesses(List<AccessToTrailer> accesses) {
        this.accesses = accesses;
    }

    public List<AccessToTrailer> getAccesses() {
        return accesses;
    }

    public void setAccessibleToAll(Boolean accessibleToAll) {
        this.accessibleToAll = accessibleToAll;
    }

    public Boolean getAccessibleToAll() {
        return isNullValue(accessibleToAll);
    }

    public void setAccessibleToOwner(Boolean accessibleToOwner) {
        this.accessibleToOwner = accessibleToOwner;
    }

    public Boolean getAccessibleToOwner() {
        return isNullValue(accessibleToOwner);
    }


    public void setBaseCostAttraction(Double baseCostAttraction) {
        this.baseCostAttraction = baseCostAttraction;
    }

    public Double getBaseCostAttraction() {
        return isNullValue(baseCostAttraction);
    }

    public void setExtraCostAttraction(Double extraCostAttraction) {
        this.extraCostAttraction = extraCostAttraction;
    }

    public Double getExtraCostAttraction() {
        return isNullValue(extraCostAttraction);
    }


    public void setDownloadType(EDownloadTypeItems downloadType) {
        this.downloadType = downloadType == null ? null : downloadType.getId();
    }

    public EDownloadTypeItems getDownloadType() {
        return downloadType == null ? null : EDownloadTypeItems.fromId(downloadType);
    }


    @Override
    public Boolean getIsFree() {
        return isFree;
    }

    @Override
    public void setIsFree(Boolean free) {
        isFree = free;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentNumber() {
        return identNumber;
    }

    public void setIdentNumber(String identNumber) {
        this.identNumber = identNumber;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public ETrailerTypeItems getType() {
        return type == null ? null : ETrailerTypeItems.fromId(type);
    }

    public void setType(ETrailerTypeItems type) {
        this.type = type == null ? null : type.getId();
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Double getEmptyTrailerWeight() {
        return isNullValue(emptyTrailerWeight);
    }

    public void setEmptyTrailerWeight(Double emptyTrailerWeight) {
        this.emptyTrailerWeight = emptyTrailerWeight;
    }

    public Double getLength() {
        return isNullValue(length);
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return isNullValue(width);
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return isNullValue(height);
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getCostKilometer() {
        return isNullValue(costKilometer);
    }

    public void setCostKilometer(Double costKilometer) {
        this.costKilometer = costKilometer;
    }

    public Double getCostHour() {
        return isNullValue(costHour);
    }

    public void setCostHour(Double costHour) {
        this.costHour = costHour;
    }

    public Double getCostSupply() {
        return isNullValue(costSupply);
    }

    public void setCostSupply(Double costSupply) {
        this.costSupply = costSupply;
    }

    public Double getMaxWeight() {
        return isNullValue(maxWeight);
    }

    public void setMaxWeight(Double maxWeight) {
        this.maxWeight = maxWeight;
    }

    public Double getMaxVolume() {
        return isNullValue(maxVolume);
    }

    public void setMaxVolume(Double maxVolume) {
        this.maxVolume = maxVolume;
    }

    public Double getMinWeight() {
        return isNullValue(minWeight);
    }

    public void setMinWeight(Double minWeight) {
        this.minWeight = minWeight;
    }

    public Double getMinVolume() {
        return isNullValue(minVolume);
    }

    public void setMinVolume(Double minVolume) {
        this.minVolume = minVolume;
    }

    public Double getLimitWeight() {
        return isNullValue(limitWeight);
    }

    public void setLimitWeight(Double limitWeight) {
        this.limitWeight = limitWeight;
    }

    public Boolean getServicePointWithRamp() {
        return servicePointWithRamp;
    }

    public void setServicePointWithRamp(Boolean servicePointWithRamp) {
        this.servicePointWithRamp = servicePointWithRamp;
    }

    public Boolean getServicePointWithoutRamp() {
        return servicePointWithoutRamp;
    }

    public void setServicePointWithoutRamp(Boolean servicePointWithoutRamp) {
        this.servicePointWithoutRamp = servicePointWithoutRamp;
    }

    public Boolean getTemperatureConditions() {
        return temperatureConditions;
    }

    public void setTemperatureConditions(Boolean temperatureConditions) {
        this.temperatureConditions = temperatureConditions;
    }

    public Integer getTemperatureRetentionTime() {
        return temperatureRetentionTime;
    }

    public void setTemperatureRetentionTime(Integer temperatureRetentionTime) {
        this.temperatureRetentionTime = temperatureRetentionTime;
    }

    public Integer getLowTemperature() {
        return lowTemperature;
    }

    public void setLowTemperature(Integer lowTemperature) {
        this.lowTemperature = lowTemperature;
    }

    public Integer getHighTemperature() {
        return highTemperature;
    }

    public void setHighTemperature(Integer highTemperature) {
        this.highTemperature = highTemperature;
    }
}
