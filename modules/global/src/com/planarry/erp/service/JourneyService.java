
package com.planarry.erp.service;


import com.planarry.erp.entity.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface JourneyService {
    String NAME = "erp_JourneyService";

    List<TransportState> getActualTransportStates(Date date, Double weight, Double volume, ETransportStateItems state);

    List<TransportState> getTransportStatesByCategory(Category category, Date date);

    List<TransportState> getTransportStatesByJourney(Journey journey);

    TransportState getTransportStateBeforeDateByTransport(Transport transport, ETransportStateItems state, Date date);

    TransportState getTransportStateAfterDateByTransport(Transport transport, ETransportStateItems state, Date date);

    boolean journeyNotExistOnDates(Transport transport, Date startDate, Date endDate);

    JourneyState getJourneyState(Journey journey, String viewName);

    List<Journey> getActualJourneys(Date lowerBound, Date upperBound, Double weight, Double volume);

    boolean isTransportInJourney(Transport transport);

    void removeMutualSettlementByJourney(Journey journey);

    void removeClientCreditByJourney(Journey journey);

    Map<String, Boolean> saveDataTransactionOnApprove(Journey journey);
}