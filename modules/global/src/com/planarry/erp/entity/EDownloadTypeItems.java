package com.planarry.erp.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;

public enum EDownloadTypeItems implements EnumClass<Integer> {
    top(1),
    side(2),
    back(3),
    withFullAwningRemoval(4),
    withRemovalCrosswise(5),
    withRemovalOfRacks(6);

    private Integer id;

    EDownloadTypeItems(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static EDownloadTypeItems fromId(Integer id) {
        for (EDownloadTypeItems at : EDownloadTypeItems.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
