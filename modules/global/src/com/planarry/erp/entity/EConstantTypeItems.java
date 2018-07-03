
package com.planarry.erp.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;

public enum EConstantTypeItems implements EnumClass<Integer> {

    integer(1),
    fractional(2),
    string(3),
    logical(4);

    private Integer id;

    EConstantTypeItems(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static EConstantTypeItems fromId(Integer id) {
        for (EConstantTypeItems at : EConstantTypeItems.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}