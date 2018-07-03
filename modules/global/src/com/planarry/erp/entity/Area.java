package com.planarry.erp.entity;

public interface Area {

    PolygonMap getPolygon();
    Double getCostKilometer();
    Double getCostSupply();
    Double getCostHour();
    Double getCostEntrancePenalty();
    Double getCostExitPenalty();
}
