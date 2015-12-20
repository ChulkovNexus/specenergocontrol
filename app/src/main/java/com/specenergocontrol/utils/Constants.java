package com.specenergocontrol.utils;

import android.content.Context;

import java.text.SimpleDateFormat;

/**
 * Created by Комп on 30.06.2015.
 */
public class Constants {

    private static final String BASE_URL_PROD = "https://89204401440.ru:9061/api/external";
    private static final String BASE_URL_TEST = "https://89204401440.ru:9150/api/external";
    public static final String USER_AGENT = "android";
    public static final String OPTIONS_PIN = "1862";

    public static SimpleDateFormat getVisitDateFormatter() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        return format;
    }


    public static String getBaseURL(Context context) {
        boolean test = StoreUtils.getInstance(context).getTestApi();
        return test ? BASE_URL_TEST : BASE_URL_PROD;
    }

    public static String getBaseURL(boolean test) {
        return test ? BASE_URL_TEST : BASE_URL_PROD;
    }
}
