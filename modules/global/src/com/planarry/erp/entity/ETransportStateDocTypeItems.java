
package com.planarry.erp.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ETransportStateDocTypeItems implements EnumClass<Integer> {

    journey(1),
    location(2);

    private Integer id;

    ETransportStateDocTypeItems(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static ETransportStateDocTypeItems fromId(Integer id) {
        for (ETransportStateDocTypeItems at : ETransportStateDocTypeItems.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}