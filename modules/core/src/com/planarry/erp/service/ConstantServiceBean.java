package com.planarry.erp.service;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.planarry.erp.entity.Constant;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

@Service(ConstantService.NAME)
public class ConstantServiceBean implements ConstantService {

    @Inject
    private Persistence persistence;

    @Override
    public Constant getConstant(String key) {
        Constant constant;
        try (Transaction transaction = persistence.createTransaction()) {
            String queryStr = "SELECT e FROM erp$Constant e WHERE e.key = :key";
            TypedQuery<Constant> query = persistence.getEntityManager().createQuery(queryStr, Constant.class);
            query.setViewName("_local");
            query.setParameter("key", key);
            constant = query.getSingleResult();
            transaction.commit();
        } catch (NoResultException | NonUniqueResultException e) {
            constant = null;
        }
        return constant;
    }
}