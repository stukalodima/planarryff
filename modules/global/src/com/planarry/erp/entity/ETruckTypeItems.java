package com.planarry.erp.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;

public enum ETruckTypeItems implements EnumClass<Integer> {
    truck(1),
    tractorTruck(2);

    private Integer id;

    ETruckTypeItems(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static ETruckTypeItems fromId(Integer id) {
        for (ETruckTypeItems at : ETruckTypeItems.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
