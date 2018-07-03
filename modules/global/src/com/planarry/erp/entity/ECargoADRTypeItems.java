
package com.planarry.erp.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ECargoADRTypeItems implements EnumClass<Integer> {

    explosive(1),
    gasses(2),
    flammableLiquids(3),
    flammableSolids(4),
    oxidizingSubstances(5),
    toxicSubstances(6),
    radioactiveMaterials(7),
    corrosiveSubstances(8),
    other(9);

    private Integer id;

    ECargoADRTypeItems(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static ECargoADRTypeItems fromId(Integer id) {
        for (ECargoADRTypeItems at : ECargoADRTypeItems.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}