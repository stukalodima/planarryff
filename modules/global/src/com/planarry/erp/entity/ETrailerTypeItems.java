package com.planarry.erp.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;

public enum ETrailerTypeItems implements EnumClass<Integer> {
    trailer(1),
    semitrailer(2);

    private Integer id;

    ETrailerTypeItems(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static ETrailerTypeItems fromId(Integer id) {
        for (ETrailerTypeItems at : ETrailerTypeItems.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
