package com.planarry.erp.service;

import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.planarry.erp.entity.Category;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service(CategoryService.NAME)
public class CategoryServiceBean implements CategoryService {

    @Inject
    private Persistence persistence;

    @Override
    public Category getActualCategory(Double weight, Double volume, Integer numberOfPallets) {
        Category category;
        try(Transaction transaction = persistence.createTransaction()) {
            String queryStr = "select e from erp$Category e where e.weight >= :weight";
            if (volume != 0){
                queryStr += " AND e.volume >= :volume";
            } else if (numberOfPallets != 0){
                queryStr += " AND e.numberOfPallets >= :numberOfPallets";
            }
            queryStr += " ORDER BY e.weight ASC";


            TypedQuery<Category> query = persistence.getEntityManager().createQuery(queryStr, Category.class);
            query.setViewName("category-with-polygon-view");
            query.setParameter("weight", weight);
            if (volume != 0){
                query.setParameter("volume", volume);
            } else if (numberOfPallets != 0){
                query.setParameter("numberOfPallets", numberOfPallets);
            }
            query.setMaxResults(1);
            category = query.getFirstResult();
            transaction.commit();
        }
        return category;
    }
}