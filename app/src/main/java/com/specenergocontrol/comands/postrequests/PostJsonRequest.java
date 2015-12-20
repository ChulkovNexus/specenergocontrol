package com.specenergocontrol.comands.postrequests;

import android.content.Context;
import android.util.Log;

import com.specenergocontrol.comands.Command;
import com.specenergocontrol.comands.HttpErrorException;
import com.specenergocontrol.parser.Parser;
import com.specenergocontrol.utils.Constants;
import com.specenergocontrol.utils.HttpUtils;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.net.ConnectException;
import java.text.ParseException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Комп on 22.04.2015.
 */
public class PostJsonRequest extends Command {

    private static final String TAG = PostJsonRequest.class.getSimpleName();
    private Parser parser;
    private String params;
    public static final MediaType MEDIA_TYPE_JSON
            = MediaType.parse("application/json; charset=utf-8");

    public PostJsonRequest(Context context) {
        super(context);
    }

    @Override
    public Serializable execute() throws IOException, ParseException, JSONException, HttpErrorException, TimeoutException {
        super.execute();
        if (url==null || params==null){
            throw new IllegalStateException("url or params wasn't set");
        }
        if (!HttpUtils.checkInternet(getContext()))
            throw new ConnectException();

        Request.Builder builder = new Request.Builder();

        Request request = addHeaders(builder).url(url)
                .post(RequestBody.create(MEDIA_TYPE_JSON, params))
                .build();
        Log.d("DEBUG", "requestUrl:" + (url));
        Log.d("DEBUG", "headers:" + request.headers().names());
        Log.d("DEBUG", "params:" + params);
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



    public void setParser(Parser parser) {
        this.parser = parser;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
