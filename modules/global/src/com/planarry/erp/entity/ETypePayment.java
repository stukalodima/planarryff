/*
 * Copyright(c) 2017 Planarry
 */
package com.planarry.erp.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;

/**
* @author Aleksandr Iushko
*/
public enum ETypePayment implements EnumClass<Integer> {

    paymentServiceCommission(1),
    paymentUseTransport(2);

    private Integer id;

    ETypePayment(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static ETypePayment fromId(Integer id) {
        for (ETypePayment at : ETypePayment.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}