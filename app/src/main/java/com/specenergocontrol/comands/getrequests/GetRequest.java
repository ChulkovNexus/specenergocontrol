package com.specenergocontrol.comands.getrequests;

import android.content.Context;


import com.specenergocontrol.comands.Command;
import com.specenergocontrol.http.CustomRestTamplate;
import com.specenergocontrol.utils.HttpUtils;

import org.json.JSONException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.io.Serializable;
import java.net.ConnectException;
import java.text.ParseException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Комп on 30.03.2015.
 */
public class GetRequest extends Command {

    public GetRequest(Context context) {
        super(context);
    }

    @Override
    public Serializable execute() throws HttpClientErrorException, HttpServerErrorException, ParseException, JSONException, ConnectException, TimeoutException {
        super.execute();
        if (url==null){
            throw new IllegalStateException("url wasn't set");
        }
        if (!HttpUtils.checkInternet(getContext()))
            throw new ConnectException();

        ResponseEntity<String> exchange;
        CustomRestTamplate restTemplate = new CustomRestTamplate(getContext());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        HttpHeaders headers = getHeaders();
        Serializable dataObject = null;
        exchange = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), String.class);
//        fillPagenationData(exchange);
        if (parser!=null)
            dataObject = parser.parse(exchange.getBody());

        return dataObject;
    }

}
