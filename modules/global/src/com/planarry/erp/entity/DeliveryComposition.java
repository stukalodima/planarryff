package com.planarry.erp.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.StandardEntity;

@Table(name = "ERP_DELIVERY_COMPOSITION")
@Entity(name = "erp$DeliveryComposition")
public class DeliveryComposition extends StandardEntity {
    private static final long serialVersionUID = -6798071589893515455L;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DELIVERY_ID")
    private Delivery delivery;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "POINT_ID")
    private Point point;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Column(name = "DELIVERY_DATE", nullable = false)
    private Date deliveryDate;

    @Column(name = "SERVICE_TIME")
    private Integer serviceTime;

    @Column(name = "DOWN_TIME")
    private Integer downTime;

    @Column(name = "DISTANCE")
    private Double distance;

    @Column(name = "DURATION")
    private Integer duration;

    @NotNull
    @Column(name = "STATUS", nullable = false)
    private Integer status;

    public void setStatus(EStatusItems status) {
        this.status = status == null ? null : status.getId();
    }

    public EStatusItems getStatus() {
        return status == null ? null : EStatusItems.fromId(status);
    }


    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setServiceTime(Integer serviceTime) {
        this.serviceTime = serviceTime;
    }

    public Integer getServiceTime() {
        return serviceTime;
    }

    public void setDownTime(Integer downTime) {
        this.downTime = downTime;
    }

    public Integer getDownTime() {
        return downTime;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getDuration() {
        return duration;
    }


}