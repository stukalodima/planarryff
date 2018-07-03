package com.planarry.erp.service;

public interface MathService {
    String NAME = "erp_MathService";

    String calculateJourney(String json, String globalId);

    String getCalcResult(String queryId);
}