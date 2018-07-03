
package com.planarry.erp.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.haulmont.cuba.core.entity.StandardEntity;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.validation.constraints.NotNull;
import javax.persistence.OneToOne;

@Table(name = "ERP_JOURNEY_COMPOSITION")
@Entity(name = "erp$JourneyComposition")
public class JourneyComposition extends StandardEntity {
    private static final long serialVersionUID = 949111935931289098L;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TRANSPORT_ID")
    private Transport transport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "JOURNEY_ID")
    private Journey journey;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "POINT_ID")
    private Point point;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOCATION_DATE")
    private Date locationDate;


    public void setPoint(Point point) {
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setLocationDate(Date locationDate) {
        this.locationDate = locationDate;
    }

    public Date getLocationDate() {
        return locationDate;
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }

    public Journey getJourney() {
        return journey;
    }



}