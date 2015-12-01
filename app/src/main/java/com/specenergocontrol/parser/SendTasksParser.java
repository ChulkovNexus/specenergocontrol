package com.specenergocontrol.parser;

import android.content.Context;

import com.specenergocontrol.model.TaskModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Комп on 01.12.2015.
 */
public class SendTasksParser implements Parser {

    private final Context context;
    private final ArrayList<TaskModel> taskModels;

    public SendTasksParser(Context context, ArrayList<TaskModel> taskModels) {
        this.context = context;
        this.taskModels = taskModels;
    }


    @Override
    public Serializable parse(String string) throws JSONException, ParseException {
        new StatusParser().parse(string);
        JSONObject response = new JSONObject(string);
        JSONArray data = response.getJSONArray("Data");
        for (int i = 0; i < data.length(); i++) {
            String photoUrl = data.getString(i);
            taskModels.get(i).setPhotoUrl(photoUrl);
        }
        return taskModels;
    }
}
