package com.planarry.erp.service;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.global.*;
import com.planarry.erp.entity.*;

import org.springframework.stereotype.Service;


import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(JourneyService.NAME)
public class JourneyServiceBean implements JourneyService {

    @Inject
    private Persistence persistence;

    @Inject
    private ConstantService constantService;

    @Inject
    private CurrencyService currencyService;

    @Inject
    private UserService userService;

    @Inject
    private Metadata metadata;

    @Override
    public List<TransportState> getActualTransportStates(Date date, Double weight, Double volume, ETransportStateItems state) {
        List<TransportState> states;
        try (Transaction transaction = persistence.createTransaction()) {
            String queryStr = "SELECT e FROM erp$TransportState e " +
                    "WHERE e.transport.totalMaxCargoWeight >= :weight " +
                    "AND e.transport.totalMaxCargoVolume >= :volume " +
                    "AND e.stateDate = " +
                    "(SELECT MAX(t.stateDate) FROM erp$TransportState t WHERE t.transport.id = e.transport.id " +
                    "AND t.stateDate < :date " +
                    "AND t.state = :state)";

            TypedQuery<TransportState> query = persistence.getEntityManager().createQuery(queryStr, TransportState.class);
            query.setViewName("transportState-view");
            query.setParameter("date", date);
            query.setParameter("state", state);
            query.setParameter("weight", weight);
            query.setParameter("volume", volume);

            states = query.getResultList();
            transaction.commit();
        }
        return states;
    }

    @Override
    public List<TransportState> getTransportStatesByCategory(Category category, Date date) {
        List<TransportState> states;
        try (Transaction transaction = persistence.createTransaction()) {
            String queryStr = "SELECT e FROM erp$TransportState e " +
                    "WHERE e.transport.category.id >= :category " +
                    "AND e.stateDate = " +
                    "(SELECT MAX(t.stateDate) FROM erp$TransportState t WHERE t.transport.id = e.transport.id " +
                    "AND t.stateDate < :date " +
                    "AND t.state = :state)";

            TypedQuery<TransportState> query = persistence.getEntityManager().createQuery(queryStr, TransportState.class);
            query.setViewName("transportState-view");
            query.setParameter("date", date);
            query.setParameter("state", ETransportStateItems.free);
            query.setParameter("category", category);

            states = query.getResultList();
            transaction.commit();
        }
        return states;
    }

    @Override
    public boolean journeyNotExistOnDates(Transport transport, Date startDate, Date endDate) {
        List<Journey> journeyList;
        try (Transaction transaction = persistence.createTransaction()) {
            String queryStr = "SELECT e FROM erp$Journey e " +
                    "WHERE e.transport.id = :transport " +
                    "AND ((e.startDate BETWEEN :startDate AND :endDate) " +
                    "OR (e.endDate BETWEEN :startDate AND :endDate) " +
                    "OR (e.startDate <= :startDate AND e.endDate >= :endDate))";

            TypedQuery<Journey> query = persistence.getEntityManager().createQuery(queryStr, Journey.class);
            query.setViewName("journey-browse-view");
            query.setParameter("transport", transport);
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);

            journeyList = query.getResultList();
            transaction.commit();
        }
        return journeyList.isEmpty();
    }

    @Override
    public TransportState getTransportStateBeforeDateByTransport(Transport transport, ETransportStateItems state, Date date) {
        TransportState transportState;
        try (Transaction transaction = persistence.createTransaction()) {
            String queryStr = "SELECT e FROM erp$TransportState e " +
                    "WHERE e.transport.id = :transport " +
                    "AND e.stateDate = " +
                    "(SELECT MAX(t.stateDate) FROM erp$TransportState t WHERE t.transport.id = e.transport.id " +
                    "AND t.state = :state " +
                    "AND t.stateDate <= :date)";

            TypedQuery<TransportState> query = persistence.getEntityManager().createQuery(queryStr, TransportState.class);
            query.setViewName("transportState-view");
            query.setParameter("date", date);
            query.setParameter("state", state);
            query.setParameter("transport", transport);

            transportState = query.getSingleResult();
            transaction.commit();
        } catch (NonUniqueResultException | NoResultException e) {
            transportState = null;
        }
        return transportState;
    }

    @Override
    public TransportState getTransportStateAfterDateByTransport(Transport transport, ETransportStateItems state, Date date) {
        TransportState transportState;
        try (Transaction transaction = persistence.createTransaction()) {
            String queryStr = "SELECT e FROM erp$TransportState e " +
                    "WHERE e.transport.id = :transport " +
                    "AND e.stateDate = " +
                    "(SELECT MIN(t.stateDate) FROM erp$TransportState t WHERE t.transport.id = e.transport.id " +
                    "AND t.state = :state " +
                    "AND t.stateDate >= :date)";

            TypedQuery<TransportState> query = persistence.getEntityManager().createQuery(queryStr, TransportState.class);
            query.setViewName("_local");
            query.setParameter("date", date);
            query.setParameter("state", state);
            query.setParameter("transport", transport);

            transportState = query.getSingleResult();
            transaction.commit();
        } catch (NonUniqueResultException | NoResultException e) {
            transportState = null;
        }
        return transportState;
    }

    @Override
    public List<TransportState> getTransportStatesByJourney(Journey journey) {
        List<TransportState> states;
        try (Transaction transaction = persistence.createTransaction()) {
            String queryStr = "SELECT e FROM erp$TransportState e WHERE e.journey.id = :journey";

            TypedQuery<TransportState> query = persistence.getEntityManager().createQuery(queryStr, TransportState.class);
            query.setViewName("transportState-view");
            query.setParameter("journey", journey);

            states = query.getResultList();
            transaction.commit();
        }
        return states;
    }

    @Override
    public JourneyState getJourneyState(Journey journey, String viewName) {
        JourneyState state;
        try (Transaction transaction = persistence.createTransaction()) {
            String queryStr = "SELECT e FROM erp$JourneyState e WHERE e.journey.id = :journey";

            TypedQuery<JourneyState> query = persistence.getEntityManager().createQuery(queryStr, JourneyState.class);
            query.setViewName(viewName);
            query.setParameter("journey", journey);

            state = query.getSingleResult();
            transaction.commit();
        } catch (NonUniqueResultException | NoResultException e) {
            state = null;
        }
        return state;
    }

    @Override
    public List<Journey> getActualJourneys(Date lowerBound, Date upperBound, Double weight, Double volume) {
        List<Journey> journeyList;
        try (Transaction transaction = persistence.createTransaction()) {
            String queryStr = "SELECT e FROM erp$Journey e " +
                    "WHERE e.status = 2 " +
                    "AND e.manualJourney = false " +
                    "AND e.startDate >= :lowerBound " +
                    "AND e.startDate <= :upperBound " +
                    "AND e.residualWeight >= :weight " +
                    "AND e.residualVolume >= :volume";

            TypedQuery<Journey> query = persistence.getEntityManager().createQuery(queryStr, Journey.class);
            query.setViewName("journey-creation-view");
            query.setParameter("weight", weight);
            query.setParameter("volume", volume);
            query.setParameter("lowerBound", lowerBound);
            query.setParameter("upperBound", upperBound);

            journeyList = query.getResultList();
            transaction.commit();
        }
        return journeyList;
    }

    @Override
    public boolean isTransportInJourney(Transport transport) {
        List<Journey> journeyList;
        try (Transaction transaction = persistence.createTransaction()) {
            String queryStr = "SELECT e FROM erp$Journey e " +
                    "WHERE e.transport.id = :transport " +
                    "AND (e.status = :status_planned " +
                    "OR e.status = :status_running)";

            TypedQuery<Journey> query = persistence.getEntityManager().createQuery(queryStr, Journey.class);
            query.setViewName("journey-browse-view");
            query.setParameter("transport", transport);
            query.setParameter("status_planned", EStatusItems.planned);
            query.setParameter("status_running", EStatusItems.running);

            journeyList = query.getResultList();
            transaction.commit();
        }
        return !journeyList.isEmpty();
    }

    /**
     * @param journey for delete by this value entity from erp$MutualSettlements table.
     */
    @Override
    public void removeMutualSettlementByJourney(Journey journey) {
        try (Transaction transaction = persistence.getTransaction()) {
            String queryStr = "DELETE FROM erp$MutualSettlements e WHERE e.journey.id = :journey";

            Query query = persistence.getEntityManager().createQuery(queryStr);
            query.setParameter("journey", journey);
            query.executeUpdate();

            transaction.commit();
        }
    }

    /**
     * @param journey for delete by this value entity from erp$ClientCredit table.
     */
    @Override
    public void removeClientCreditByJourney(Journey journey) {
        try (Transaction transaction = persistence.getTransaction()) {
            String queryStr = "DELETE FROM erp$ClientCredit e WHERE e.journey.id = :journey";

            Query query = persistence.getEntityManager().createQuery(queryStr);
            query.setParameter("journey", journey);
            query.executeUpdate();

            transaction.commit();
        }
    }

    /**
     * @param jor for find Journey with entityManager by id.
     *            commitResult HashMap for save exception message what can be if transaction will be broken.
     * @return commitResult with exception or empty if all commits was good.
     */
    @Override
    public Map<String, Boolean> saveDataTransactionOnApprove(Journey jor) {
        HashMap<String, Boolean> commitResult = new HashMap<>();
        try (Transaction transaction = persistence.createTransaction()) {
            JourneyState journeyState = persistence.getEntityManager()
                    .createQuery("SELECT e FROM erp$JourneyState e WHERE e.journey.id = :journey", JourneyState.class)
                    .setViewName("journey-state-approve-action-view")
                    .setParameter("journey", jor)
                    .getFirstResult();

            Journey journey = journeyState.getJourney();
            if (clientCreditManager(journey, commitResult).isEmpty()) {
                if (mutualSettlementManager(journey, commitResult).isEmpty()) {
                    journey.setApproved(!journey.getApproved());
                    EStatusItems status = journey.getApproved()
                            ? EStatusItems.approved
                            : EStatusItems.preApproved;

                    journeyState.setStatus(status);
                    journey.setStatus(status);
                    journey.getJourneyCargoes().forEach(journeyCargo -> journeyCargo.getCargo().setStatus(status));
                }
            }
            transaction.commit();
        } catch (Exception e) {
            commitResult.put(e.getLocalizedMessage(), false);
        }
        return commitResult;
    }

    /**
     * @param journey for save new mutualSettlement or remove by it.
     *                <p>
     *                create new mutual settlement if journey is not approved and remove if approved.
     */
    private Map<String, Boolean> mutualSettlementManager(Journey journey, HashMap<String, Boolean> commitResult) {
        if (!journey.getApproved()) {
            try (Transaction transaction = persistence.getTransaction()) {
                MutualSettlements mutualSettlements = metadata.create(MutualSettlements.class);
                mutualSettlements.setJourney(journey);
                mutualSettlements.setPayDate(AppBeans.get(TimeSource.class).currentTimestamp());
                mutualSettlements.setTransportOwner(journey.getTransport().getCompany());
                mutualSettlements.setValue(Double.valueOf(constantService.getConstant("serviceCommission").getValue()));
                persistence.getEntityManager().persist(mutualSettlements);
                transaction.commit();
            } catch (Exception e) {
                commitResult.put(e.getLocalizedMessage(), false);
            }
        } else {
            removeMutualSettlementByJourney(journey);
        }
        return commitResult;
    }

    /**
     * @param journey for getting some params and set into clientCredit entity.
     *                <p>
     *                If approved=false and user company and transport company is different create new ClientCredit
     *                fill in all fields and save into the table;
     *                <p>
     *                if approved=true and user company and transport company is different - remove by journey
     *                ClientCredit entity from table.
     */
    private Map<String, Boolean> clientCreditManager(Journey journey, HashMap<String, Boolean> commitResult) {
        if (userService.getUserCompany() != null && journey.getTransport() != null && journey.getTransport().getCompany() != null) {
            Company ownerCompany = journey.getTransport().getCompany();
            Company couterpartyCompany = userService.getUserCompany();

            if (!journey.getApproved() && !ownerCompany.equals(couterpartyCompany)) {
                try (Transaction transaction = persistence.getTransaction()) {
                    ClientCredit clientCredit = metadata.create(ClientCredit.class);
                    clientCredit.setJourney(journey);
                    clientCredit.setTransportOwner(ownerCompany);
                    clientCredit.setCounterparty(couterpartyCompany);
                    clientCredit.setPayDate(AppBeans.get(TimeSource.class).currentTimestamp());
                    clientCredit.setValue(calcBaseCostClientCredit(journey));
                    persistence.getEntityManager().persist(clientCredit);
                    transaction.commit();
                } catch (Exception e) {
                    commitResult.put(e.getLocalizedMessage(), false);
                }
            } else if (journey.getApproved() && !ownerCompany.equals(couterpartyCompany)) {
                removeClientCreditByJourney(journey);
            }
        }
        return commitResult;
    }

    /**
     * @param journey for getting some params and count value for clientCredit.
     * @return Double value (sum of "baseCostOfAttraction" and "extraCostAttraction: from Transport entity).
     */
    private Double calcBaseCostClientCredit(Journey journey) {
        Double baseCostOfAttraction = Double.valueOf(constantService.getConstant("baseCostOfAttraction").getValue());
        if (journey.getTransport() != null && journey.getTransport().getCurrency() != null) {
            if (currencyService.getBaseCurrency() != null && journey.getTransport().getTotalExtraCostAttraction() != null) {
                Double totalExtraCostAttraction = journey.getTransport().getTotalExtraCostAttraction();
                if (currencyService.getCurrencyRate(journey.getTransport().getCurrency()).getRate() != null) {

                    Double currencyRatio = userService.calcConvertCurrencyRatio(journey.getTransport().getCurrency().getCoefficient(),
                            currencyService.getCurrencyRate(journey.getTransport().getCurrency()).getRate(), 1, 1);
                    return baseCostOfAttraction + (totalExtraCostAttraction * currencyRatio);
                }
            }
        }
        return 0.0;
    }


}
