
package com.planarry.erp.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;

public enum EStatusItems implements EnumClass<Integer> {

    created(1),
    planned(2),
    running(3),
    done(4),
    canceled(5),
    approved(6),
    preApproved(7);

    private Integer id;

    EStatusItems(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static EStatusItems fromId(Integer id) {
        for (EStatusItems at : EStatusItems.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}