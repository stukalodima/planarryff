package com.planarry.erp.entity;

/**
 * @author Aleksandr Iushko
 */
public interface AssistantAntity {

    Currency getCurrency();

    void setBaseCostAttraction(Double baseCostAttraction);

    void setExtraCostAttraction(Double extraCostAttraction);

    Double getExtraCostAttraction();
}
