package com.planarry.erp.service;

import com.planarry.erp.entity.Company;

/**
 * @author Aleksandr Iushko
 */
public interface UserService {
    String NAME = "erp_UserService";

    Company getUserCompany();

    double calcConvertCurrencyRatio(double coefficientFrom, double currencyRateFrom, double coefficientTo, double currencyRateTo);
}
