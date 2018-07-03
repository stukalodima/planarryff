package com.planarry.erp.web.utils;

import com.haulmont.cuba.gui.components.Formatter;

public class TimeDurationFormatter implements Formatter<Integer> {

    @Override
    public String format(Integer value) {
        if (value == null) {
            return "";
        }
        String hours = String.valueOf(value / 60);
        String minutes = String.valueOf(value % 60);
        if (hours.length() == 1){
            hours = "0" + hours;
        }
        if (minutes.length() == 1){
            minutes = "0" + minutes;
        }
        return hours + ":" + minutes ;
    }
}