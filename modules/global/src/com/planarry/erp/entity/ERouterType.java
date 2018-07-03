/*
 * Copyright(c) 2017 Planarry
 */
package com.planarry.erp.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;

public enum ERouterType implements EnumClass<String> {

    standard("standard"),
    osm("osm");

    private String id;

    ERouterType(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static ERouterType fromId(String id) {
        for (ERouterType at : ERouterType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}