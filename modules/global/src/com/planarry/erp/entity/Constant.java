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
@Table(name = "ERP_CONSTANT")
@Entity(name = "erp$Constant")
public class Constant extends StandardEntity {
    private static final long serialVersionUID = 4505869057344680791L;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotNull
    @Column(name = "VALUE_TYPE", nullable = false)
    private Integer valueType;

    @NotNull
    @Column(name = "CONSTANT_VALUE", nullable = false)
    private String value;

    @NotNull
    @Column(name = "CONSTANT_KEY", nullable = false, unique = true)
    private String key;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setValueType(EConstantTypeItems valueType) {
        this.valueType = valueType == null ? null : valueType.getId();
    }

    public EConstantTypeItems getValueType() {
        return valueType == null ? null : EConstantTypeItems.fromId(valueType);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }


}