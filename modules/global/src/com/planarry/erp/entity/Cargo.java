
package com.planarry.erp.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import com.haulmont.cuba.core.entity.annotation.Listeners;

@Listeners("erp_CargoEntityListener")
@Table(name = "ERP_CARGO", indexes = {
    @Index(name = "IDX_ERP_CARGO_ACCESSIBLE_TO_ALL", columnList = "ACCESSIBLE_TO_ALL"),
    @Index(name = "IDX_ERP_CARGO_ACCESSIBLE_TO_OWNER", columnList = "ACCESSIBLE_TO_OWNER")
})
@Entity(name = "erp$Cargo")
public class Cargo extends StandardEntity implements ErpEntity, MonitoringEntity {
    private static final long serialVersionUID = -7013677019433521803L;

    @Column(name = "STATUS")
    private Integer status;

    @Lob
    @Column(name = "DOC_COMMENT")
    private String comment;

    @NotNull
    @Column(name = "CARGO_TYPE", nullable = false)
    private Integer cargoType;

    @Column(name = "PALLETS_TYPE")
    private Integer palletsType;

    @Column(name = "WEIGHT")
    private Double weight;

    @Column(name = "VOLUME")
    private Double volume;

    @Column(name = "NUMBER_OF_PALLETS")
    private Integer numberOfPallets;

    @Column(name = "TEMPERATURE_CARGO")
    private Boolean temperatureCargo;

    @Column(name = "DANGEROUS_CARGO")
    private Boolean dangerousCargo;

    @Column(name = "CLASS_ADR")
    private Integer classADR;

    @Column(name = "DESIRED_PRICE")
    private Double desiredPrice;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "SENT_DATE")
    private Date sentDate;

    @Column(name = "SENT_DATE_DELTA")
    private Integer sentDateDelta;

    @NotNull
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "POINT_ID")
    private Point sendPoint;

    @OrderBy("order")
    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "cargo")
    private List<CargoDeliveryPoint> deliveryPoints;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "cargo")
    private List<AccessToCargo> accesses;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CURRENCY_ID")
    private Currency currency;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRANSPORT_TYPE_ID")
    private TransportType transportType;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "clear"})
    @NotNull
    @OnDeleteInverse(DeletePolicy.DENY)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    @NotNull
    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MANAGER_ID")
    private Employee manager;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "clear"})
    @NotNull
    @OnDeleteInverse(DeletePolicy.DENY)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CLIENT_ID")
    private Company client;

    @NotNull
    @Column(name = "CLIENT_CAPTION", nullable = false)
    private String clientCaption;

    @NotNull
    @Column(name = "PHONE", nullable = false)
    private String phone;

    @Column(name = "ACCESSIBLE_TO_ALL")
    private Boolean accessibleToAll;

    @Column(name = "ACCESSIBLE_TO_OWNER")
    private Boolean accessibleToOwner;

    @Transient
    @MetaProperty
    private Boolean flag = false;


    public void setSendPoint(Point sendPoint) {
        this.sendPoint = sendPoint;
    }

    public Point getSendPoint() {
        return sendPoint;
    }


    public void setDeliveryPoints(List<CargoDeliveryPoint> deliveryPoints) {
        this.deliveryPoints = deliveryPoints;
    }

    public List<CargoDeliveryPoint> getDeliveryPoints() {
        return deliveryPoints;
    }


    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public Employee getManager() {
        return manager;
    }


    public void setAccesses(List<AccessToCargo> accesses) {
        this.accesses = accesses;
    }

    public List<AccessToCargo> getAccesses() {
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

    public void setClient(Company client) {
        this.client = client;
    }

    public Company getClient() {
        return client;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }


    public void setSentDateDelta(Integer sentDateDelta) {
        this.sentDateDelta = sentDateDelta;
    }

    public Integer getSentDateDelta() {
        return isNullValue(sentDateDelta);
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Boolean getFlag() {
        return flag;
    }


    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setDesiredPrice(Double desiredPrice) {
        this.desiredPrice = desiredPrice;
    }

    public Double getDesiredPrice() {
        return isNullValue(desiredPrice);
    }

    public void setClassADR(ECargoADRTypeItems classADR) {
        this.classADR = classADR == null ? null : classADR.getId();
    }

    public ECargoADRTypeItems getClassADR() {
        return classADR == null ? null : ECargoADRTypeItems.fromId(classADR);
    }

    public void setDangerousCargo(Boolean dangerousCargo) {
        this.dangerousCargo = dangerousCargo;
    }

    public Boolean getDangerousCargo() {
        return dangerousCargo;
    }

    public EStatusItems getStatus() {
        return status == null ? null : EStatusItems.fromId(status);
    }

    public void setStatus(EStatusItems status) {
        this.status = status == null ? null : status.getId();
    }

    public void setClientCaption(String clientCaption) {
        this.clientCaption = clientCaption;
    }

    public String getClientCaption() {
        return clientCaption;
    }

    public void setPalletsType(EPalletsType palletsType) {
        this.palletsType = palletsType == null ? null : palletsType.getId();
    }

    public EPalletsType getPalletsType() {
        return palletsType == null ? null : EPalletsType.fromId(palletsType);
    }

    public void setTemperatureCargo(Boolean temperatureCargo) {
        this.temperatureCargo = temperatureCargo;
    }

    public Boolean getTemperatureCargo() {
        return temperatureCargo;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setTransportType(TransportType transportType) {
        this.transportType = transportType;
    }

    public TransportType getTransportType() {
        return transportType;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setCargoType(ECargoTypeItems cargoType) {
        this.cargoType = cargoType == null ? null : cargoType.getId();
    }
    public ECargoTypeItems getCargoType() {
        return cargoType == null ? null : ECargoTypeItems.fromId(cargoType);
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getWeight() {
        return isNullValue(weight);
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getVolume() {
        return isNullValue(volume);
    }

    public void setNumberOfPallets(Integer numberOfPallets) {
        this.numberOfPallets = numberOfPallets;
    }

    public Integer getNumberOfPallets() {
        return isNullValue(numberOfPallets);
    }
}