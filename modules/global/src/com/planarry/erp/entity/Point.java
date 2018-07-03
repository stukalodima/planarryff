package com.planarry.erp.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.planarry.erp.datatype.GeoCoordinate;
import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.StandardEntity;

@Table(name = "ERP_POINT")
@Entity(name = "erp$Point")
public class Point extends StandardEntity implements ErpEntity {
    private static final long serialVersionUID = 6347292228258634167L;

    @Column(name = "CODE")
    private Integer code;

    @NotNull
    @Column(name = "ADDRESS", nullable = false)
    private String address;

    @NotNull
    @Column(name = "COUNTRY", nullable = false)
    private String country;

    @NotNull
    @Column(name = "CITY", nullable = false)
    private String city;

    @Column(name = "STREET")
    private String street;

    @Column(name = "BUILDING")
    private String building;

    @NotNull
    @MetaProperty(datatype = "geocoordinate", mandatory = true)
    @Column(name = "LATITUDE", nullable = false)
    private Double latitude;

    @NotNull
    @MetaProperty(datatype = "geocoordinate", mandatory = true)
    @Column(name = "LONGITUDE", nullable = false)
    private Double longitude;

    @Column(name = "SERVICE_TIME")
    private Integer serviceTime;

    @Transient
    @MetaProperty
    private Integer positionInRoute;

    public void setServiceTime(Integer serviceTime) {
        this.serviceTime = serviceTime;
    }

    public Integer getServiceTime() {
        return isNullValue(serviceTime);
    }


    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getBuilding() {
        return building;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Integer getPositionInRoute() {
        return isNullValue(positionInRoute);
    }

    public void setPositionInRoute(Integer positionInRoute) {
        this.positionInRoute = positionInRoute;
    }
}