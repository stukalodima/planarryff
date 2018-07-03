package com.planarry.erp.service;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.planarry.erp.entity.Delivery;
import com.planarry.erp.entity.Journey;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service(JourneyCompletionService.NAME)
public class JourneyCompletionServiceBean implements JourneyCompletionService {


    @Inject
    private Persistence persistence;

    @Override
    public List<Delivery> getDeliveriesByJourney(Journey journey){
        List<Delivery> deliveries;
        try (Transaction transaction = persistence.createTransaction()) {
            String queryStr = "SELECT e FROM erp$Delivery e WHERE e.journey.id = :journey";

            TypedQuery<Delivery> query = persistence.getEntityManager().createQuery(queryStr, Delivery.class);
            query.setViewName("delivery-view");
            query.setParameter("journey", journey);

            deliveries = query.getResultList();
            transaction.commit();
        }
        return deliveries;
    }
}