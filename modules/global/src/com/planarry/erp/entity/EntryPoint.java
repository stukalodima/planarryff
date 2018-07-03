/*
 * Copyright(c) 2017 Planarry
 */
package com.planarry.erp.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.planarry.erp.datatype.GeoCoordinate;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.StandardEntity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
* @author Dima Stukalo
*/
@Table(name = "ERP_ENTRY_POINT")
@Entity(name = "erp$EntryPoint")
public class EntryPoint extends StandardEntity {
    private static final long serialVersionUID = -6827537146374474472L;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotNull
    @MetaProperty(datatype = "geocoordinate", mandatory = true)
    @Column(name = "LAT", nullable = false)
    private Double lat;

    @NotNull
    @MetaProperty(datatype = "geocoordinate", mandatory = true)
    @Column(name = "LON", nullable = false)
    private Double lon;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "POLYGON_MAP_ID")
    private PolygonMap polygonMap;

    public void setPolygonMap(PolygonMap polygonMap) {
        this.polygonMap = polygonMap;
    }

    public PolygonMap getPolygonMap() {
        return polygonMap;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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


}