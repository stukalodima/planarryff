package com.planarry.erp.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;

public enum EPersonTypeItems implements EnumClass<Integer> {

    legalPerson(1),
    naturalPerson(2);

    private Integer id;

    EPersonTypeItems(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static EPersonTypeItems fromId(Integer id) {
        for (EPersonTypeItems at : EPersonTypeItems.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }

}
