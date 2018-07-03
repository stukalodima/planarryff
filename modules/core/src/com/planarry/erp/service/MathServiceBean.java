package com.planarry.erp.service;

import com.google.common.collect.ImmutableMap;
import com.planarry.erp.utils.HttpUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.*;


@Service(MathService.NAME)
public class MathServiceBean implements MathService {

    private static final Logger logger = LoggerFactory.getLogger(MathServiceBean.class);
    private static final String MATH_URL = "http://math-all.s2.sngtrans.com.ua/task";

    @Override
    public String calculateJourney(String json, String globalId) {
        try {
            HttpUtils http = new HttpUtils();
            int codeResponse = http.post(MATH_URL, getParamsForHttpTask(json, globalId));
            boolean goodResult = codeResponse >= 200 && codeResponse < 300;
            if (goodResult) {
                JSONObject jsonObject = new JSONObject(http.getResultStr());
                String queryId = jsonObject.get("query").toString();
                if (!queryId.isEmpty()) {
                    return getCalcResult(queryId);
                }
            }
        } catch (URISyntaxException | IOException e) {
            logger.error(e.getLocalizedMessage());
        }
        return null;
    }

    public String getCalcResult(String queryId) {
        FutureTask<String> future = new FutureTask<>(() -> {
            String calcResult = null;
            //todo сделать какой-то тайм-аут, чтоб поток завершился в случае "чего"

            for (int i = 0; calcResult == null && i < 5; i++) {
                TimeUnit.SECONDS.sleep(1);
                HttpUtils http = new HttpUtils();
                int statusCodeResponse = http.post(MATH_URL, getStatusHttpParams(queryId));
                if (statusCodeResponse >= 200 && statusCodeResponse < 300) {
                    JSONObject jsonResult = new JSONObject(http.getResultStr());
                    JSONObject state = jsonResult.getJSONObject("state");
                    String desc = state.get("desc").toString();

                    int status = state.getInt("status");
                    if (desc.equals("FINISHED") || status == 3 || status == 2) {
                        calcResult = http.getResultStr();
                    }
                }
            }
            return calcResult;
        });

        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(future);
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getLocalizedMessage());
            return null;
        }
    }


    private Map<String, Object> getParamsForHttpTask(final String json, final String globalId) {
        return ImmutableMap.of(
                "action", "add",
                "globalID", globalId,
                "source", "erp",
                "task", json);
    }

    private Map<String, Object> getStatusHttpParams(final String queryId) {
        return ImmutableMap.of(
                "action", "getState",
                "queryID", queryId,
                "lastSID", 0);
    }


}