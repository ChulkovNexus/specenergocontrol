package com.specenergocontrol.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by alexander on 09.04.15.
 */
public class HttpUtils {


    public static boolean checkInternet(Context context)
    {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return wifi.isConnected() || mobile.isConnected();
    }
}
