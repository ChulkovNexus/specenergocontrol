package com.specenergocontrol.utils;

import java.text.SimpleDateFormat;

/**
 * Created by Комп on 30.06.2015.
 */
public class Constants {

    public static final String BASE_URL = "https://89204401440.ru:9150/api/external";
    public static final String USER_AGENT = "android";

    public static SimpleDateFormat getVisitDateFormatter() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss.SSS");
        return format;
    }
}
