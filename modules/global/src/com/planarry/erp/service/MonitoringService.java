
package com.planarry.erp.service;


import com.planarry.erp.entity.ExtUser;
import com.planarry.erp.entity.Journey;
import com.planarry.erp.entity.MonitoringSettings;
import com.planarry.erp.entity.TransportState;

import java.util.Date;
import java.util.List;

public interface MonitoringService {
    String NAME = "erp_MonitoringService";

    List<Journey>  getCurrentManualJourneys();

    MonitoringSettings getMonitoringSettingsByUser(ExtUser user);
}