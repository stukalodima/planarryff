
package com.planarry.erp.service;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.TimeSource;
import com.planarry.erp.entity.EStatusItems;
import com.planarry.erp.entity.JourneyState;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;


@Service(JourneyStateCheckerService.NAME)
public class JourneyStateCheckerServiceBean implements JourneyStateCheckerService {

    @Inject
    private Persistence persistence;

    @Override
    public void checkJourneyState() {
        try (Transaction transaction = persistence.createTransaction()) {
            String queryStr = "SELECT e FROM erp$JourneyState e WHERE e.status = :status" +
                    " AND e.journey.startDate <= :date";

            TypedQuery<JourneyState> query = persistence.getEntityManager().createQuery(queryStr, JourneyState.class);
            query.setViewName("journeyState-scheduler-view");
            query.setParameter("status", EStatusItems.approved);
            query.setParameter("date", AppBeans.get(TimeSource.class).currentTimestamp());

            List<JourneyState> journeyStateList = query.getResultList();

            if (!journeyStateList.isEmpty()) {
                journeyStateList.forEach(item -> {
                    item.getJourney().getJourneyCargoes().forEach(journeyCargo -> journeyCargo.getCargo().setStatus(EStatusItems.running));
                    item.setStatus(EStatusItems.running);
                    item.getJourney().setStatus(EStatusItems.running);
                });
            }
            transaction.commit();
        }
    }
}