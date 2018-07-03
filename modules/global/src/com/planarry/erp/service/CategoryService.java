package com.planarry.erp.service;

import com.planarry.erp.entity.Category;

public interface CategoryService {
    String NAME = "erp_CategoryService";

    Category getActualCategory(Double weight, Double volume, Integer numberOfPallets);
}