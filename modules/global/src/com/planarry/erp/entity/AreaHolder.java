package com.planarry.erp.entity;

import java.util.List;

public interface AreaHolder {

    List<? extends Area> getAreas();

    Integer getMinHourNumber();

    Double getTotalCostKilometer();

    Double getTotalCostSupply();

    Double getTotalCostHour();

}
