package com.specenergocontrol.comands;

import android.content.Context;
import android.util.Log;


import com.specenergocontrol.parser.Parser;
import com.specenergocontrol.utils.Constants;
import com.specenergocontrol.utils.StoreUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.Serializable;
import java.net.ConnectException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Комп on 16.12.2014.
 */
public abstract class Command implements Serializable {
    public static final String AUTHORIZATION = "Authorization";
    protected static final String USER_AGENT = "User-Agent";
    public static final String ACCEPT = "Accept";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String APPLICATION_JSON = "application/json";

    private Context context;
    protected String url;
    protected Parser parser;
    private String originalUrl;
    private String loadUrl = "";
    private boolean needToken = true;
    public static OkHttpClient okHttpClient;

    protected void setNeedToken(boolean needToken) {
        this.needToken = needToken;
    }

    protected Command(Context context) {
        this.context = context;
    }

    public Serializable execute() throws ParseException, JSONException, ConnectException, TimeoutException, IOException {
        url = loadUrl;
        return null;
    }

    public Context getContext() {
        return context;
    }

    public void setParser(Parser parser) {
        this.parser = parser;
    }

    public void setUrl(String url) {
        this.originalUrl = url;
        this.loadUrl = url;
    }

    protected static OkHttpClient getOkHttpClient(){
        if (okHttpClient == null) {
            okHttpClient = getUnsafeOkHttpClient();
        }
        return okHttpClient;
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setSslSocketFactory(sslSocketFactory);
            okHttpClient.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected Request.Builder addHeaders(Request.Builder builder) {
        if (needToken) {
            String token = StoreUtils.getInstance(context).getToken();
            builder.addHeader(AUTHORIZATION, "Token" + token);
        }
        return builder.addHeader(USER_AGENT, Constants.USER_AGENT)
                .addHeader(ACCEPT, APPLICATION_JSON)
                .addHeader(CONTENT_TYPE, APPLICATION_JSON);
    }

}
