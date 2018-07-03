package com.planarry.erp.service;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.planarry.erp.entity.Currency;
import com.planarry.erp.entity.CurrencyRate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

@Service(CurrencyService.NAME)
public class CurrencyServiceBean implements CurrencyService {

    @Inject
    private Persistence persistence;

    @Override
    public CurrencyRate getCurrencyRate(Currency currency) {
        CurrencyRate currencyRate;
        try (Transaction transaction = persistence.createTransaction()) {
            String queryStr = "SELECT e FROM erp$CurrencyRate e" +
                    " WHERE e.date = (SELECT MAX(r.date) FROM erp$CurrencyRate r" +
                    " WHERE r.currency.id = e.currency.id and r.currency.id = :currency)";

            TypedQuery<CurrencyRate> query = persistence.getEntityManager().createQuery(queryStr, CurrencyRate.class);
            query.setViewName("_local");
            query.setParameter("currency", currency);

            currencyRate = query.getSingleResult();
            transaction.commit();
        } catch (NoResultException | NonUniqueResultException e) {
            currencyRate = null;
        }
        return currencyRate;
    }

    @Override
    public Currency getBaseCurrency() {
        Currency currency;
        try (Transaction transaction = persistence.createTransaction()) {
            String queryStr = "SELECT e FROM erp$Currency e WHERE e.baseCurrency = true";
            TypedQuery<Currency> query = persistence.getEntityManager().createQuery(queryStr, Currency.class);
            query.setViewName("_local");
            currency = query.getSingleResult();
            transaction.commit();
        } catch (NoResultException | NonUniqueResultException e) {
            currency = null;
        }
        return currency;
    }
}
