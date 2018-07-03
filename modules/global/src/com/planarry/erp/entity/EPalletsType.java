
package com.planarry.erp.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;

public enum EPalletsType implements EnumClass<Integer> {

    euro(1),
    usual(2);

    private Integer id;

    EPalletsType(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static EPalletsType fromId(Integer id) {
        for (EPalletsType at : EPalletsType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}