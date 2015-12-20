package com.specenergocontrol.comands.postrequests;

import android.content.Context;

import com.specenergocontrol.parser.SignInParser;
import com.specenergocontrol.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Комп on 30.06.2015.
 */
public class SignInCommand extends PostJsonRequest {

    private static final String PASSWORD = "password";
    private final String USER_NAME = "login";
    protected String url = "/login";

    public SignInCommand(Context context, String loginText, String passwordText, boolean test) {
        super(context);
        setUrl(Constants.getBaseURL(test) + url);
        setParams(generateJson(loginText, passwordText));
        setParser(new SignInParser(context));
        setNeedToken(false);
    }

    private String generateJson(String loginText, String passwordText) {
        JSONObject result = new JSONObject();
        try {
            result.put(USER_NAME, loginText);
            result.put(PASSWORD, passwordText);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
