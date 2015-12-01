package com.specenergocontrol.parser;

import com.specenergocontrol.comands.BussinessException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;

/**
 * Created by Комп on 01.12.2015.
 */
public class StatusParser implements Parser {

    @Override
    public Serializable parse(String string) throws JSONException, ParseException {
        JSONObject jsonObject = new JSONObject(string);
        String status = jsonObject.getString("Status");
        if (status.equals("Error")) {
            String message = jsonObject.getString("Message");
            throw new BussinessException(message);
        } else {
            return true;
        }
    }
}
