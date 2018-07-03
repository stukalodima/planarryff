/*
 * Copyright(c) 2017 Planarry
 */

package com.planarry.erp.web.utils;

import java.util.StringJoiner;

public class AddressClassForGoogle {
    private String address;
    private String city;
    private String country;
    private String street;
    private String building;
    private Double lat;
    private Double lon;

    public AddressClassForGoogle() {
        setDefaultValues();
    }

    public void fillAddressClassForGoogle(AddressClassForGoogle copyElement) {
        this.address = copyElement.getAddress();
        this.city = copyElement.getCity();
        this.street = copyElement.getStreet();
        this.building = copyElement.getBuilding();
        this.country = copyElement.getCountry();
        this.lat = copyElement.getLat();
        this.lon = copyElement.getLon();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void buildFullAddress() {
        StringJoiner addressBuilder = new StringJoiner(", ");
        joinAddressPart(addressBuilder, country);
        joinAddressPart(addressBuilder, city);
        joinAddressPart(addressBuilder, street);
        joinAddressPart(addressBuilder, building);
        address = addressBuilder.toString();
    }

    private void joinAddressPart(StringJoiner addressBuilder, String field) {
        if (field != null && !field.isEmpty()) {
            addressBuilder.add(field);
        }
    }

    public void setDefaultValues(){
        this.address = "";
        this.city = "";
        this.country = "";
        this.lat = 0.;
        this.lon = 0.;
    }
}
