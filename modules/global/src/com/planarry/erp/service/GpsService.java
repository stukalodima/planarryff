package com.planarry.erp.service;

import java.util.Date;

public interface GpsService {
    String NAME = "erp_GpsService";

    String getGpsData(Integer sensorCode, Date from, Date to);
}