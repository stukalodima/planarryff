/*
 * Copyright(c) 2017 Planarry
 */
package com.planarry.erp.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import java.util.List;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
* @author Dima Stukalo
*/
@NamePattern("%s|name")
@Table(name = "ERP_CATEGORY")
@Entity(name = "erp$Category")
public class Category extends StandardEntity implements ErpEntity, AreaHolder {
    private static final long serialVersionUID = 1978644714578710744L;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COMPANY_ID")
    private Company company;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "category")
    private List<CategoryArea> areas;

    @Column(name = "MIN_HOUR_NUMBER")
    private Integer minHourNumber;

    @Column(name = "WEIGHT")
    private Double weight;

    @Column(name = "VOLUME")
    private Double volume;

    @Column(name = "NUMBER_OF_PALLETS")
    private Integer numberOfPallets;

    @Column(name = "COST_KILOMETER")
    private Double costKilometer;

    @Column(name = "COST_HOUR")
    private Double costHour;

    @Column(name = "COST_SUPPLY")
    private Double costSupply;

    public void setCompany(Company company) {
        this.company = company;
    }

    public Company getCompany() {
        return company;
    }


    public void setMinHourNumber(Integer minHourNumber) {
        this.minHourNumber = minHourNumber;
    }

    public Integer getMinHourNumber() {
        return isNullValue(minHourNumber);
    }

    public void setAreas(List<CategoryArea> areas) {
        this.areas = areas;
    }

    public List<CategoryArea> getAreas() {
        return areas;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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

    public void setCostKilometer(Double costKilometer) {
        this.costKilometer = costKilometer;
    }

    public Double getCostKilometer() {
        return isNullValue(costKilometer);
    }

    public void setCostHour(Double costHour) {
        this.costHour = costHour;
    }

    public Double getCostHour() {
        return isNullValue(costHour);
    }

    public void setCostSupply(Double costSupply) {
        this.costSupply = costSupply;
    }

    public Double getCostSupply() {
        return isNullValue(costSupply);
    }

    @Override
    public Double getTotalCostKilometer() {
        return getCostKilometer();
    }

    @Override
    public Double getTotalCostSupply() {
        return getCostSupply();
    }

    @Override
    public Double getTotalCostHour() {
        return getCostHour();
    }

}