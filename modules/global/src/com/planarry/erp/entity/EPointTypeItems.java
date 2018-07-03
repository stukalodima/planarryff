/*
 * Copyright(c) 2017 Planarry
 */
package com.planarry.erp.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;

/**
* @author Dima Stukalo
*/
public enum EPointTypeItems implements EnumClass<Integer> {

    startPoint(1),
    finishPoint(2);

    private Integer id;

    EPointTypeItems(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static EPointTypeItems fromId(Integer id) {
        for (EPointTypeItems at : EPointTypeItems.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}