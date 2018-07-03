package com.planarry.erp.service;

import org.springframework.stereotype.Service;

@Service("erp_OsmRouterService")
public class OsmRouterServiceBean extends AbstractRouterService implements RouterService {
    private static final String URL = "http://router.project-osrm.org/route/v1/driving/";

    @Override
    protected String getUrl() {
        return URL;
    }
}
