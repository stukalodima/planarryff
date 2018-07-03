
package com.planarry.erp.service;


import com.planarry.erp.entity.Currency;
import com.planarry.erp.entity.CurrencyRate;

public interface CurrencyService {
    String NAME = "erp_CurrencyService";

    CurrencyRate getCurrencyRate(Currency currency);

    Currency getBaseCurrency();
}