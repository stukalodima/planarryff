/*
 * Copyright(c) 2017 Planarry
 */
package com.planarry.erp.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;

/**
* @author Dima Stukalo
*/
@NamePattern("%s|name")
@Table(name = "ERP_ACTIVITIES_TYPE")
@Entity(name = "erp$ActivitiesType")
public class ActivitiesType extends StandardEntity {
    private static final long serialVersionUID = 2139333944307968371L;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}