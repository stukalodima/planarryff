
package com.planarry.erp.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

@Table(name = "ERP_POLYGON_SEGMENT")
@Entity(name = "erp$PolygonSegment")
public class PolygonSegment extends BaseUuidEntity implements ErpEntity {
    private static final long serialVersionUID = 2045297333129369671L;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "POLYGON_ID")
    private PolygonMap polygon;

    @OnDeleteInverse(DeletePolicy.CASCADE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "START_ID")
    private PolygonPoint start;

    @NotNull
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "END_ID")
    private PolygonPoint end;

    @MetaProperty(datatype = "geocoordinate")
    @Column(name = "MIN_LAT")
    private Double minLat;

    @MetaProperty(datatype = "geocoordinate")
    @Column(name = "MIN_LON")
    private Double minLon;

    @MetaProperty(datatype = "geocoordinate")
    @Column(name = "MAX_LAT")
    private Double maxLat;

    @MetaProperty(datatype = "geocoordinate")
    @Column(name = "MAX_LON")
    private Double maxLon;


    public void setMaxLat(Double maxLat) {
        this.maxLat = maxLat;
    }

    public Double getMaxLat() {
        return isNullValue(maxLat);
    }

    public void setMaxLon(Double maxLon) {
        this.maxLon = maxLon;
    }

    public Double getMaxLon() {
        return isNullValue(maxLon);
    }

    public void setMinLon(Double minLon) {
        this.minLon = minLon;
    }

    public Double getMinLon() {
        return isNullValue(minLon);
    }

    public void setMinLat(Double minLat) {
        this.minLat = minLat;
    }

    public Double getMinLat() {
        return isNullValue(minLat);
    }

    public void setEnd(PolygonPoint end) {
        this.end = end;
    }

    public PolygonPoint getEnd() {
        return end;
    }


    public void setPolygon(PolygonMap polygon) {
        this.polygon = polygon;
    }

    public PolygonMap getPolygon() {
        return polygon;
    }

    public void setStart(PolygonPoint start) {
        this.start = start;
    }

    public PolygonPoint getStart() {
        return start;
    }

    public void calculateRectangle(){
        if (start != null && end != null) {

            if (start.getLat() < end.getLat()) {
                minLat = start.getLat();
                maxLat = end.getLat();
            } else {
                minLat = end.getLat();
                maxLat = start.getLat();
            }

            if (start.getLon() < end.getLon()) {
                minLon = start.getLon();
                maxLon = end.getLon();
            } else {
                minLon = end.getLon();
                maxLon = start.getLon();
            }
        }
    }
}