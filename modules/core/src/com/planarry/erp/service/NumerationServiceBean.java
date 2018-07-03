
package com.planarry.erp.service;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.planarry.erp.entity.Company;
import org.springframework.stereotype.Service;

import javax.inject.Inject;


@Service(NumerationService.NAME)
public class NumerationServiceBean implements NumerationService {

    @Inject
    private Persistence persistence;

    @Override
    public String getNewDocNumber(final Company company) {
        String prefix = "$$$";
        if (company != null){
            prefix = company.getPrefix();
        }
        String query = "select max(cast(substring(j.journey_number from '.....$') as INTEGER)) " +
                "from erp_journey j where j.journey_number like '" + prefix + "%'" ;

        int doc_number;

        final int START_DOC_NUMBER = 1;
        final int STEP_DOC_NUMBER = 1;

        try (Transaction transaction = persistence.createTransaction()) {
            Query nativeQuery = persistence.getEntityManager().createNativeQuery(query);
            Object result = nativeQuery.getFirstResult();
            if (result != null) {
                doc_number = (int) result + STEP_DOC_NUMBER;
            } else {
                doc_number = START_DOC_NUMBER;
            }
            transaction.commit();
        } catch (Exception e) {
            doc_number = START_DOC_NUMBER;
        }

        return String.format("%010d", doc_number);
    }
}