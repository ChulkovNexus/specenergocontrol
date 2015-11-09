package com.specenergocontrol.comands.postrequests;

import android.content.Context;
import android.util.Log;

import com.specenergocontrol.comands.Command;
import com.specenergocontrol.http.CustomRestTamplate;
import com.specenergocontrol.parser.Parser;
import com.specenergocontrol.utils.Constants;
import com.specenergocontrol.utils.HttpUtils;

import org.json.JSONException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.io.Serializable;
import java.net.ConnectException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

/**
 * Created by Комп on 22.04.2015.
 */
public class PostJsonRequest extends Command {

    private Parser parser;
    private String params;

    public PostJsonRequest(Context context) {
        super(context);
    }

    @Override
    public Serializable execute() throws HttpClientErrorException, HttpServerErrorException, ParseException, JSONException, ConnectException, TimeoutException {
        super.execute();
        if (url==null || params==null) {
            throw new IllegalStateException("url or params wasn't set");
        }
        if (!HttpUtils.checkInternet(getContext()))
            throw new ConnectException();

        ResponseEntity<String> exchange;
        CustomRestTamplate restTemplate = new CustomRestTamplate(getContext());
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        HttpHeaders headers = getHeaders();

        Serializable dataObject = null;
        HttpEntity<String> entity = new HttpEntity<String>(params, headers);

        exchange = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        Log.d("123", exchange.getBody());
        if (parser!=null)
            dataObject = parser.parse(exchange.getBody());

        return dataObject;
    }



    public void setParser(Parser parser) {
        this.parser = parser;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
