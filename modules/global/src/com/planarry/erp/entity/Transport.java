package com.planarry.erp.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;

@NamePattern("%s|name")
@Table(name = "ERP_TRANSPORT", indexes = {
    @Index(name = "IDX_ERP_TRANSPORT_IS_FREE", columnList = "IS_FREE"),
    @Index(name = "IDX_ERP_TRANSPORT_AVAILABLE_FOR_ALL", columnList = "AVAILABLE_FOR_ALL"),
    @Index(name = "IDX_ERP_TRANSPORT_AVAILABLE_FOR_OWNER", columnList = "AVAILABLE_FOR_OWNER")
})
@Entity(name = "erp$Transport")
public class Transport extends StandardEntity implements ErpEntity, AssistantAntity, AreaHolder {
    private static final long serialVersionUID = -3508093301503332256L;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotNull
    @Column(name = "IDENT_NUMBER", nullable = false)
    private String identNumber;

    @Column(name = "GUID")
    private String guid;

    @NotNull
    @Column(name = "TRUCK_TYPE", nullable = false)
    private Integer truckType;

    @Column(name = "DOWNLOAD_TYPE")
    private Integer downloadType;

    @Column(name = "AVAILABLE_FOR_ALL")
    private Boolean availableForAll = false;

    @Column(name = "AVAILABLE_FOR_OWNER")
    private Boolean availableForOwner = true;

    @NotNull
    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "clear"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRANSPORT_TYPE_ID")
    private TransportType transportType;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CURRENCY_BILLING_ID")
    private Currency currency;

    @NotNull
    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "clear"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FORWARDER_ID")
    private Employee employee;

    @OnDelete(DeletePolicy.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PHOTO_ID")
    private FileDescriptor photo;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "clear"})
    @OnDeleteInverse(DeletePolicy.DENY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "transport")
    private List<TransportTrailers> transportTrailers;

    @NotNull
    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "transport")
    private List<TransportDrivers> transportDrivers;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "transport")
    private List<TransportForwarders> transportForwarders;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "transport")
    private List<TransportCrewMembers> transportCrewMembers;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "transport")
    private List<TransportClassADR> classADR;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "transport")
    private List<TransportsAccess> transportsAccesses;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "transport")
    private List<TransportArea> areas;

    @Column(name = "VIN_CODE")
    private String vinCode;

    @Column(name = "EMPTY_TRUCK_WEIGHT")
    private Double emptyTruckWeight;

    @Column(name = "BODY_LENGTH")
    private Double bodyLength;

    @Column(name = "BODY_WIDTH")
    private Double bodyWidth;

    @Column(name = "BODY_HEIGHT")
    private Double bodyHeight;

    @Column(name = "TRANSPORT_HEIGHT")
    private Double transportHeight;

    @Column(name = "TRANSPORT_LENGTH")
    private Double transportLength;

    @Column(name = "COST_KILOMETER")
    private Double costKilometer;

    @Column(name = "COST_HOUR")
    private Double costHour;

    @Column(name = "COST_SUPPLY")
    private Double costSupply;

    @Column(name = "COST_TON_KILOMETER")
    private Double costTonKilometer;

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

    @Column(name = "SENSOR_CODE")
    private Integer sensorCode;

    @Column(name = "SERVICE_POINTS_WITH_RAMP")
    private Boolean servicePointWithRamp;

    @Column(name = "SERVICE_POINTS_WITHOUT_RAMP")
    private Boolean servicePointWithoutRamp;

    @Column(name = "MAX_SPEED")
    private Double maxSpeed;

    @Column(name = "DANGEROUS_CARGO_PERMISSION")
    private Boolean dangerousCargoPermission;

    @Column(name = "TEMPERATURE_CONDITIONS")
    private Boolean temperatureConditions;

    @Column(name = "TEMPERATURE_RETENTION_TIME")
    private Integer temperatureRetentionTime;

    @Column(name = "LOW_TEMPERATURE")
    private Integer lowTemperature;

    @Column(name = "HIGH_TEMPERATURE")
    private Integer highTemperature;

    @NotNull
    @Column(name = "IS_FREE", nullable = false)
    private Boolean isFree = true;

    @Column(name = "TOTAL_EMPTY_TRANSPORT_WEIGHT")
    private Double totalEmptyTransportWeight;

    @Column(name = "TOTAL_TRANSPORT_LENGTH")
    private Double totalTransportLength;

    @Column(name = "TOTAL_TRANSPORT_HEIGHT")
    private Double totalTransportHeight;

    @Column(name = "TOTAL_TRANSPORT_WIDTH")
    private Double totalTransportWidth;

    @Column(name = "TOTAL_MAX_CARGO_VOLUME")
    private Double totalMaxCargoVolume;

    @Column(name = "TOTAL_MAX_CARGO_WEIGHT")
    private Double totalMaxCargoWeight;

    @Column(name = "TOTAL_COST_KILOMETER")
    private Double totalCostKilometer;

    @Column(name = "TOTAL_COST_SUPPLY")
    private Double totalCostSupply;

    @Column(name = "TOTAL_COST_HOUR")
    private Double totalCostHour;

    @Column(name = "TOTAL_COST_TON_KILOMETER")
    private Double totalCostTonKilometer;

    @Column(name = "BASE_COST_ATTRACTION")
    private Double baseCostAttraction;

    @Column(name = "EXTRA_COST_ATTRACTION")
    private Double extraCostAttraction;

    @Column(name = "TOTAL_BASE_COST_ATTRACTION")
    private Double totalBaseCostAttraction;

    @Column(name = "TOTAL_EXTRA_COST_ATTRACTION")
    private Double totalExtraCostAttraction;

    @Column(name = "MIN_HOUR_NUMBER")
    private Integer minHourNumber;

    @Override
    public Integer getMinHourNumber() {
        return isNullValue(minHourNumber);
    }

    public void setMinHourNumber(Integer minHourNumber) {
        this.minHourNumber = minHourNumber;
    }

    public void setAreas(List<TransportArea> areas) {
        this.areas = areas;
    }

    public List<TransportArea> getAreas() {
        return areas;
    }


    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }


    public void setCostTonKilometer(Double costTonKilometer) {
        this.costTonKilometer = costTonKilometer;
    }

    public Double getCostTonKilometer() {
        return isNullValue(costTonKilometer);
    }

    public void setTotalCostTonKilometer(Double totalCostTonKilometer) {
        this.totalCostTonKilometer = totalCostTonKilometer;
    }

    public Double getTotalCostTonKilometer() {
        return isNullValue(totalCostTonKilometer);
    }


    public void setTotalBaseCostAttraction(Double totalBaseCostAttraction) {
        this.totalBaseCostAttraction = totalBaseCostAttraction;
    }

    public Double getTotalBaseCostAttraction() {
        return isNullValue(totalBaseCostAttraction);
    }

    public void setTotalExtraCostAttraction(Double totalExtraCostAttraction) {
        this.totalExtraCostAttraction = totalExtraCostAttraction;
    }

    public Double getTotalExtraCostAttraction() {
        return isNullValue(totalExtraCostAttraction);
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


    public List<TransportClassADR> getClassADR() {
        return classADR;
    }

    public void setClassADR(List<TransportClassADR> classADR) {
        this.classADR = classADR;
    }

    public List<TransportsAccess> getTransportsAccesses() {
        return transportsAccesses;
    }

    public void setTransportsAccesses(List<TransportsAccess> transportsAccesses) {
        this.transportsAccesses = transportsAccesses;
    }

    public void setTotalCostHour(Double totalCostHour) {
        this.totalCostHour = totalCostHour;
    }

    public Double getTotalCostHour() {
        return totalCostHour;
    }


    public void setTotalTransportHeight(Double totalTransportHeight) {
        this.totalTransportHeight = totalTransportHeight;
    }

    public Double getTotalTransportHeight() {
        return totalTransportHeight;
    }

    public void setTotalTransportWidth(Double totalTransportWidth) {
        this.totalTransportWidth = totalTransportWidth;
    }

    public Double getTotalTransportWidth() {
        return totalTransportWidth;
    }

    public void setTotalMaxCargoVolume(Double totalMaxCargoVolume) {
        this.totalMaxCargoVolume = totalMaxCargoVolume;
    }

    public Double getTotalMaxCargoVolume() {
        return totalMaxCargoVolume;
    }

    public void setTotalMaxCargoWeight(Double totalMaxCargoWeight) {
        this.totalMaxCargoWeight = totalMaxCargoWeight;
    }

    public Double getTotalMaxCargoWeight() {
        return totalMaxCargoWeight;
    }

    public void setTotalCostKilometer(Double totalCostKilometer) {
        this.totalCostKilometer = totalCostKilometer;
    }

    public Double getTotalCostKilometer() {
        return totalCostKilometer;
    }

    public void setTotalCostSupply(Double totalCostSupply) {
        this.totalCostSupply = totalCostSupply;
    }

    public Double getTotalCostSupply() {
        return totalCostSupply;
    }


    public void setTotalEmptyTransportWeight(Double totalEmptyTransportWeight) {
        this.totalEmptyTransportWeight = totalEmptyTransportWeight;
    }

    public Double getTotalEmptyTransportWeight() {
        return totalEmptyTransportWeight;
    }

    public void setTotalTransportLength(Double totalTransportLength) {
        this.totalTransportLength = totalTransportLength;
    }

    public Double getTotalTransportLength() {
        return totalTransportLength;
    }


    public List<TransportTrailers> getTransportTrailers() {
        return transportTrailers;
    }

    public void setTransportTrailers(List<TransportTrailers> transportTrailers) {
        this.transportTrailers = transportTrailers;
    }

    public List<TransportDrivers> getTransportDrivers() {
        return transportDrivers;
    }

    public void setTransportDrivers(List<TransportDrivers> transportDrivers) {
        this.transportDrivers = transportDrivers;
    }

    public List<TransportForwarders> getTransportForwarders() {
        return transportForwarders;
    }

    public void setTransportForwarders(List<TransportForwarders> transportForwarders) {
        this.transportForwarders = transportForwarders;
    }

    public List<TransportCrewMembers> getTransportCrewMembers() {
        return transportCrewMembers;
    }

    public void setTransportCrewMembers(List<TransportCrewMembers> transportCrewMembers) {
        this.transportCrewMembers = transportCrewMembers;
    }


    public Boolean getIsFree() {
        return isFree;
    }

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

    public ETruckTypeItems getTruckType() {
        return truckType == null ? null : ETruckTypeItems.fromId(truckType);
    }

    public void setTruckType(ETruckTypeItems truckType) {
        this.truckType = truckType == null ? null : truckType.getId();
    }

    public EDownloadTypeItems getDownloadType() {
        return downloadType == null ? null : EDownloadTypeItems.fromId(downloadType);
    }

    public void setDownloadType(EDownloadTypeItems downloadType) {
        this.downloadType = downloadType == null ? null : downloadType.getId();
    }

    public Boolean getAvailableForAll() {
        return isNullValue(availableForAll);
    }

    public void setAvailableForAll(Boolean availableForAll) {
        this.availableForAll = availableForAll;
    }

    public Boolean getAvailableForOwner() {
        return isNullValue(availableForOwner);
    }

    public void setAvailableForOwner(Boolean availableForOwner) {
        this.availableForOwner = availableForOwner;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public void setTransportType(TransportType transportType) {
        this.transportType = transportType;
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

    public String getVinCode() {
        return vinCode;
    }

    public void setVinCode(String vinCode) {
        this.vinCode = vinCode;
    }

    public FileDescriptor getPhoto() {
        return photo;
    }

    public void setPhoto(FileDescriptor photo) {
        this.photo = photo;
    }

    public Double getEmptyTruckWeight() {
        return isNullValue(emptyTruckWeight);
    }

    public void setEmptyTruckWeight(Double emptyTruckWeight) {
        this.emptyTruckWeight = emptyTruckWeight;
    }

    public Double getBodyLength() {
        return isNullValue(bodyLength);
    }

    public void setBodyLength(Double bodyLength) {
        this.bodyLength = bodyLength;
    }

    public Double getBodyWidth() {
        return isNullValue(bodyWidth);
    }

    public void setBodyWidth(Double bodyWidth) {
        this.bodyWidth = bodyWidth;
    }

    public Double getBodyHeight() {
        return isNullValue(bodyHeight);
    }

    public void setBodyHeight(Double bodyHeight) {
        this.bodyHeight = bodyHeight;
    }

    public Double getTransportHeight() {
        return isNullValue(transportHeight);
    }

    public void setTransportHeight(Double transportHeight) {
        this.transportHeight = transportHeight;
    }

    public Double getTransportLength() {
        return isNullValue(transportLength);
    }

    public void setTransportLength(Double transportLength) {
        this.transportLength = transportLength;
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

    public Integer getSensorCode() {
        return sensorCode;
    }

    public void setSensorCode(Integer sensorCode) {
        this.sensorCode = sensorCode;
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

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Boolean getDangerousCargoPermission() {
        return dangerousCargoPermission;
    }

    public void setDangerousCargoPermission(Boolean dangerousCargoPermission) {
        this.dangerousCargoPermission = dangerousCargoPermission;
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
