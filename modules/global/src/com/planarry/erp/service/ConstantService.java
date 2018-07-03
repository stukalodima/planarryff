package com.planarry.erp.service;

import com.planarry.erp.entity.Constant;

public interface ConstantService {
    String NAME = "erp_ConstantService";

    Constant getConstant(String key);
}