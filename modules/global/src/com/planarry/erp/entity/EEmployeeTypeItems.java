package com.planarry.erp.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;

public enum EEmployeeTypeItems implements EnumClass<Integer> {

    driver(1),
    forwarder(2),
    dispatcher(3),
    crewMember(4),
    other(5),
    transportManager(6),
    cargoManager(7);

    private Integer id;

    EEmployeeTypeItems(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static EEmployeeTypeItems fromId(Integer id) {
        for (EEmployeeTypeItems at : EEmployeeTypeItems.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
