package com.planarry.erp.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@NamePattern("%s|name")
@Table(name = "ERP_CURRENCY")
@Entity(name = "erp$Currency")
public class Currency extends StandardEntity {
    private static final long serialVersionUID = 7184514401050999890L;

    @NotNull
    @Column(name = "CODE", nullable = false, length = 3)
    private String code;

    @NotNull
    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @NotNull
    @Column(name = "SHIRT_NAME", nullable = false, length = 3)
    private String shirtName;

    @Column(name = "BASE_CURRENCY")
    private Boolean baseCurrency;

    @Column(name = "COEFFICIENT")
    private Double coefficient;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShirtName() {
        return shirtName;
    }

    public void setShirtName(String shirtName) {
        this.shirtName = shirtName;
    }

    public Boolean getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Boolean baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Double getCoefficient() {
        return coefficient == null ? 1D : coefficient;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }
}
