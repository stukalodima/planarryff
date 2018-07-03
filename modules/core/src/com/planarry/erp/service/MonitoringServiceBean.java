
package com.planarry.erp.service;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.TimeSource;
import com.planarry.erp.entity.ExtUser;
import com.planarry.erp.entity.Journey;
import com.planarry.erp.entity.MonitoringSettings;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service(MonitoringService.NAME)
public class MonitoringServiceBean implements MonitoringService {

    @Inject
    private Persistence persistence;

    @Inject
    private Metadata metadata;

    private Logger log = LoggerFactory.getLogger(MonitoringService.class);

    @Override
    public List<Journey> getCurrentManualJourneys() {
        Date now = AppBeans.get(TimeSource.class).currentTimestamp();
        List<Journey> journeyList;
        try (Transaction transaction = persistence.createTransaction()) {
            String queryStr = "SELECT e FROM erp$Journey e " +
                    "WHERE e.endDate >= :now " +
                    "AND e.manualJourney = true " +
                    "order by e.startDate";

            TypedQuery<Journey> query = persistence.getEntityManager().createQuery(queryStr, Journey.class);
            query.setViewName("journey-monitoring-view");
            query.setParameter("now", now);

            journeyList = query.getResultList();
            transaction.commit();
        }
        return journeyList;
    }

    @Override
    public MonitoringSettings getMonitoringSettingsByUser(ExtUser user) {
        MonitoringSettings settings;
        try (Transaction transaction = persistence.createTransaction()) {
            TypedQuery<MonitoringSettings> query = persistence.getEntityManager().createQuery(
                    "SELECT e FROM erp$MonitoringSettings e WHERE e.user.id = :userId", MonitoringSettings.class
            );

            query.addViewName("_local");
            query.setParameter("userId", user);
            settings = query.getSingleResult();
            transaction.commit();
        } catch (NoResultException | NonUniqueResultException e) {
            log.warn(e.getLocalizedMessage());
            //if it is a new settings (NoResultException)
            settings = createNewSettings(user);
        }
        return settings;
    }

    private MonitoringSettings createNewSettings(ExtUser user) {
        MonitoringSettings settings = metadata.create(MonitoringSettings.class);
        try (Transaction transaction = persistence.createTransaction()) {
            settings.setUpdatePeriod(1);
            settings.setUser(user);
            settings = persistence.getEntityManager().merge(settings);
            transaction.commit();
        } catch (Exception e) {
            log.warn(e.getLocalizedMessage());
        }
        return settings;
    }
}