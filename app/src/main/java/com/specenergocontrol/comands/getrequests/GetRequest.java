package com.specenergocontrol.comands.getrequests;

import android.content.Context;
import android.util.Log;


import com.specenergocontrol.comands.Command;
import com.specenergocontrol.comands.HttpErrorException;
import com.specenergocontrol.utils.HttpUtils;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.IOException;
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
    public Serializable execute() throws IOException, ParseException, JSONException, TimeoutException, HttpErrorException {
        super.execute();
        if (url==null){
            throw new IllegalStateException("url wasn't set");
        }
        if (!HttpUtils.checkInternet(getContext()))
            throw new ConnectException();

        Request.Builder builder = new Request.Builder();
        Request request = addHeaders(builder).url(url)
                .build();
        Log.d("DEBUG", "requestUrl:" + url);
        Log.d("DEBUG", "headers:" + request.headers().names());
        Response response = getOkHttpClient().newCall(request).execute();
        if (response.code() == 200) {
            if (parser != null) {
                return parser.parse(response.body().string());
            }
        } else {
            Log.d("DEBUG", response.message());
            throw new HttpErrorException(response.code(), response.message());
        }
        return null;
    }

}
