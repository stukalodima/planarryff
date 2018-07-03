
package com.planarry.erp.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.planarry.erp.datatype.GeoCoordinate;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

@Table(name = "ERP_POLYGON_POINT")
@Entity(name = "erp$PolygonPoint")
public class PolygonPoint extends BaseUuidEntity {
    private static final long serialVersionUID = 1067229898632060132L;

    @NotNull
    @MetaProperty(datatype = "geocoordinate", mandatory = true)
    @Column(name = "LAT", nullable = false)
    private Double lat;

    @NotNull
    @MetaProperty(datatype = "geocoordinate", mandatory = true)
    @Column(name = "LON", nullable = false)
    private Double lon;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "POLYGON_ID")
    private PolygonMap polygon;

    @Column(name = "POINT_ORDER")
    private Integer order;

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getOrder() {
        return order;
    }


    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLat() {
        return lat;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLon() {
        return lon;
    }

    public void setPolygon(PolygonMap polygon) {
        this.polygon = polygon;
    }

    public PolygonMap getPolygon() {
        return polygon;
    }


}