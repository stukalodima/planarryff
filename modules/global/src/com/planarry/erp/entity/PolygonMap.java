package com.planarry.erp.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import java.util.List;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.planarry.erp.datatype.GeoCoordinate;

@NamePattern("%s|name")
@Table(name = "ERP_POLYGON_MAP")
@Entity(name = "erp$PolygonMap")
public class PolygonMap extends StandardEntity implements ErpEntity {
    private static final long serialVersionUID = -8632677278726229670L;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "polygonMap")
    private List<EntryPoint> entryPoint;

    @Composition
    @OrderBy("order")
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "polygon")
    private List<PolygonPoint> polygonPoint;

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

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "polygon")
    private List<PolygonSegment> segments;

    public void setSegments(List<PolygonSegment> segments) {
        this.segments = segments;
    }

    public List<PolygonSegment> getSegments() {
        return segments;
    }


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


    public void setEntryPoint(List<EntryPoint> entryPoint) {
        this.entryPoint = entryPoint;
    }

    public List<EntryPoint> getEntryPoint() {
        return entryPoint;
    }

    public void setPolygonPoint(List<PolygonPoint> polygonPoint) {
        this.polygonPoint = polygonPoint;
    }

    public List<PolygonPoint> getPolygonPoint() {
        return polygonPoint;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}