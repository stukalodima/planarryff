
package com.planarry.erp.service;

import com.planarry.erp.entity.Company;

import java.util.List;

public interface CompanyService {
    String NAME = "erp_CompanyService";

    String createPrefix();

    List<Company> getCompaniesBySubName(String name);

}