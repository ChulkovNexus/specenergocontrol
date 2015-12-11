package com.specenergocontrol.parser;

import android.content.Context;

import com.specenergocontrol.model.User;
import com.specenergocontrol.utils.StoreUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;

/**
 * Created by Комп on 30.06.2015.
 */
public class SignInParser implements Parser {

    private Context context;

    public SignInParser(Context context){
        this.context = context;
    }

    @Override
    public Serializable parse(String string) throws JSONException, ParseException {
        JSONObject mainJsonObject = new JSONObject(string);
        String token = mainJsonObject.getString(TOKEN);
        String id = mainJsonObject.getString(ID);
        String name = mainJsonObject.getString(USER_NAME);
        User user = new User();
        user.setId(id);
        user.setName(name);
        StoreUtils.getInstance(context).setUser(user);
        StoreUtils.getInstance(context).setToken(token);
        return true;
    }
}
