package com.planarry.erp.service;

import com.haulmont.bali.util.ParamsMap;
import com.planarry.erp.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Map;

@Service(GpsService.NAME)
public class GpsServiceBean implements GpsService {

    private final Logger log = LoggerFactory.getLogger(GpsServiceBean.class);

    @Override
    public String getGpsData(Integer sensorCode, Date from, Date to) {
        String url = "http://gps.s2.sngtrans.com.ua/messages?login=admin&pass=admin321";
        Map<String, Object> params = ParamsMap.of(
                "gid", sensorCode,
                "from", from.getTime() / 1000,
                "to", to.getTime() / 1000
        );

        try {
            return doRequest(url, params);
        } catch (IOException | URISyntaxException e) {
            log.error(e.getLocalizedMessage());
            return null;
        }
    }

    private String doRequest(String url, Map<String, Object> params) throws IOException, URISyntaxException {
        HttpUtils http = new HttpUtils();
        int codeResponse = http.get(url, params);
        boolean goodResult = codeResponse >= 200 && codeResponse < 300;

        if (!goodResult) {
            log.error(getClass() + "Answer from gps server " + String.valueOf(codeResponse));
        } else if (http.getResultStr().isEmpty()) {
            log.error(getClass() + "Loss connection to gps server");
        } else {
            return http.getResultStr();
        }
        return null;
    }
}