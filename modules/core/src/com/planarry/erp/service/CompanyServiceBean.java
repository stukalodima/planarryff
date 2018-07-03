
package com.planarry.erp.service;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.planarry.erp.entity.Company;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service(CompanyService.NAME)
public class CompanyServiceBean implements CompanyService {

    private Logger log = LoggerFactory.getLogger(CompanyService.class);

    @Inject
    private Persistence persistence;

    @Override
    public List<Company> getCompaniesBySubName(String name) {
        List<Company> companies;
        try (Transaction transaction = persistence.createTransaction()) {
            String queryString = "SELECT * \n" +
                    " FROM erp_company \n" +
                    " WHERE activities_type_id = #activitiesTypeId" +
                    " AND levenshtein(name, #name) <= 3\n" +
                    " ORDER BY levenshtein(name, #name)\n" +
                    " LIMIT 10";

            TypedQuery<Company> query = persistence.getEntityManager().createNativeQuery(queryString, Company.class);
            query.setParameter("activitiesTypeId", UUID.fromString("30c91df6-ce18-4e8b-a693-19a8a19821ba"));
            query.setParameter("name", name);
            companies = query.getResultList();
            transaction.commit();
        } catch (Exception e) {
            log.warn(e.getLocalizedMessage());
            companies = new ArrayList<>();
        }
        return companies;
    }

    @Override
    public String createPrefix() {
        String newPrefix;
        String lastPrefix = getLastPrefix();
        if (lastPrefix == null){
            newPrefix = "AAA";
        } else {
            char[] chars = lastPrefix.toCharArray();

            for (int i = 2; i >= 0;) {
                if (chars[i] < 90) {
                    chars[i] = ((char) (chars[i] + 1)); //set next char
                    break;
                }
                i--;
            }
            newPrefix = String.valueOf(chars);
        }
        return newPrefix;
    }

    private String getLastPrefix() {
        String prefix;
        try (Transaction transaction = persistence.createTransaction()) {
            String queryStr = "SELECT e.prefix FROM erp_company e ORDER BY e.prefix DESC LIMIT 1";
            Query query = persistence.getEntityManager().createNativeQuery(queryStr);
            Object result = query.getSingleResult();
            prefix = (String) result;
            transaction.commit();
        } catch (NoResultException | NonUniqueResultException e) {
            prefix = null;
        }
        return prefix;
    }
}