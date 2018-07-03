package com.planarry.erp.service;

import com.planarry.erp.entity.Delivery;
import com.planarry.erp.entity.Journey;

import java.util.List;

public interface JourneyCompletionService {
    String NAME = "erp_JourneyCompletionService";

    List<Delivery> getDeliveriesByJourney(Journey journey);
}