
package com.planarry.erp.service;

import com.planarry.erp.entity.Company;

public interface NumerationService {
    String NAME = "erp_NumerationService";

    String getNewDocNumber(final Company company);
}