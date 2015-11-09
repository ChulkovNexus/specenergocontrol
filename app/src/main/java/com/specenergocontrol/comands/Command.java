package com.specenergocontrol.comands;

import android.content.Context;
import android.util.Log;


import com.specenergocontrol.parser.Parser;
import com.specenergocontrol.utils.Constants;
import com.specenergocontrol.utils.StoreUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.io.Serializable;
import java.net.ConnectException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

/**
 * Created by Комп on 16.12.2014.
 */
public abstract class Command implements Serializable {
    public static final String AUTHORIZATION = "Authorization";

//    public static final String ACCESS_TOKEN = "access-token=";

    private Context context;
    protected String url;
    protected Parser parser;
    private String originalUrl;
    private String loadUrl = "";
    private int page;
    private int pageCount;
    private int entityPerPage;
    private int entityCount;
    private boolean havePaganation;
    private boolean needToken = true;

    protected void setNeedToken(boolean needToken) {
        this.needToken = needToken;
    }

    protected Command(Context context) {
        this.context = context;
    }

    public Serializable execute() throws HttpServerErrorException, HttpClientErrorException, ParseException, JSONException, ConnectException, TimeoutException {
        url = loadUrl;
//        if (needToken) {
//            String token = StoreUtils.getInstance(context).getToken();
//            if (loadUrl.charAt(loadUrl.length()-1) != '?')
//                url += '&';
//            url += token;
//        }
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

    protected void fillPagenationData(ResponseEntity<String> exchange) {
        if (exchange.getHeaders().getFirst("X-Pagination-Page-Count") == null) {
            havePaganation=false;
            return;
        }

        pageCount = Integer.parseInt(exchange.getHeaders().getFirst("X-Pagination-Page-Count"));
//        entityCount = Integer.parseInt(exchange.getHeaders().getFirst("X-Pagination-Total-Count"));
        page = Integer.parseInt(exchange.getHeaders().getFirst("X-Pagination-Current-Page"));
//        entityPerPage = Integer.parseInt(exchange.getHeaders().getFirst("X-Pagination-Per-Page"));

        havePaganation = page<pageCount;
    }

    public void loadNextPage() {
        if (havePaganation) {
            loadUrl = originalUrl + "&page=" + (page+1);
        } else {
            throw new IllegalStateException("Command havePaganation false");
        }
    }

    protected HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ArrayList<MediaType> acceptList = new ArrayList<>();
        acceptList.add(MediaType.APPLICATION_JSON);
        headers.setAccept(acceptList);
        headers.setUserAgent(Constants.USER_AGENT);
        if (needToken) {
            String token = StoreUtils.getInstance(context).getToken();
            headers.add(AUTHORIZATION, "Token" + token);
        }
        return headers;
    }

    public void loadOrigin() {
        loadUrl = originalUrl;
    }

    public void setPage(int page) {
        havePaganation = true;
        this.page = page;
    }


    public boolean haveNextPage() {
        return havePaganation;
    }

}
