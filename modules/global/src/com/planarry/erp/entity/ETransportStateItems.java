
package com.planarry.erp.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;

public enum ETransportStateItems implements EnumClass<Integer> {

    engaged(1),
    free(2);

    private Integer id;

    ETransportStateItems(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static ETransportStateItems fromId(Integer id) {
        for (ETransportStateItems at : ETransportStateItems.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}