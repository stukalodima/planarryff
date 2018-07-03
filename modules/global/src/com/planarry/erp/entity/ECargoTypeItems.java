
package com.planarry.erp.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;

public enum ECargoTypeItems implements EnumClass<Integer> {

    piledUp(1),
    pourUp(2),
    pallets(3),
    boxes(4);

    private Integer id;

    ECargoTypeItems(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static ECargoTypeItems fromId(Integer id) {
        for (ECargoTypeItems at : ECargoTypeItems.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}