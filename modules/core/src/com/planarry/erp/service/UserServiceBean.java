package com.planarry.erp.service;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.planarry.erp.entity.Company;
import com.planarry.erp.entity.ExtUser;
import org.springframework.stereotype.Service;

/**
 * @author Aleksandr Iushko
 */
@Service(UserService.NAME)
public class UserServiceBean implements UserService {

    private Company userCompany;

    private ExtUser currentUser;

    /**
     * @return current user company.
     */
    @Override
    public Company getUserCompany() {
        UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.class);
        DataManager dataManager = AppBeans.get(DataManager.class);
        currentUser = dataManager.reload((ExtUser) userSessionSource.getUserSession().getUser(), "extUser-with-company-view");
        userCompany = currentUser.getCompany();
        return userCompany;
    }

    /**
     * @return converted currency ratio.
     */
    @Override
    public double calcConvertCurrencyRatio(double coefficientFrom, double currencyRateFrom, double coefficientTo, double currencyRateTo) {
        return (coefficientFrom / currencyRateFrom) * (currencyRateTo / coefficientTo);
    }


}
