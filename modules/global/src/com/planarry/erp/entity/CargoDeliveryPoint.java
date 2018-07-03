package com.planarry.erp.entity;

import javax.persistence.*;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import java.util.Date;
import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.StandardEntity;

@Table(name = "ERP_CARGO_DELIVERY_POINT")
@Entity(name = "erp$CargoDeliveryPoint")
public class CargoDeliveryPoint extends StandardEntity implements ErpEntity {
    private static final long serialVersionUID = 1225758518237706631L;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(name = "DELIVERY_DATE", nullable = false)
    private Date deliveryDate;

    @Column(name = "DELIVERY_ORDER")
    private Integer order;

    @Column(name = "DELIVERY_DATE_DELTA")
    private Integer deliveryDateDelta;

    @NotNull
    @OnDelete(DeletePolicy.CASCADE)
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, optional = false)
    @JoinColumn(name = "POINT_ID")
    private Point point;

    @Column(name = "WEIGHT")
    private Double weight;

    @Column(name = "VOLUME")
    private Double volume;

    @Column(name = "NUMBER_OF_PALLETS")
    private Integer numberOfPallets;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CARGO_ID")
    private Cargo cargo;

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getOrder() {
        return order == null ? 1 : order;
    }


    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setNumberOfPallets(Integer numberOfPallets) {
        this.numberOfPallets = numberOfPallets;
    }

    public Integer getNumberOfPallets() {
        return isNullValue(numberOfPallets);
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDateDelta(Integer deliveryDateDelta) {
        this.deliveryDateDelta = deliveryDateDelta;
    }

    public Integer getDeliveryDateDelta() {
        return isNullValue(deliveryDateDelta);
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Point getPoint() {
        return point;
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


}