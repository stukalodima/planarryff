package com.planarry.erp.entity;

import java.util.Date;

public interface ErpEntity {
    default Double isNullValue(Double value){
        return value == null ? 0. : value;
    }

    default Integer isNullValue(Integer value){
        return value == null ? 0 : value;
    }

    default Long isNullValue(Long value){
        return value == null ? 0 : value;
    }

    default String isNullValue(String value){
        return value == null ? "" : value;
    }

    default Date isNullValue(Date value){
        return value == null ? new Date(0L) : value;
    }

    default Boolean isNullValue(Boolean value){
        return value == null ? false : value;
    }
}
