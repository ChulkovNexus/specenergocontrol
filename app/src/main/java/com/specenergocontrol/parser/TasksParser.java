package com.specenergocontrol.parser;

import com.specenergocontrol.model.TaskModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Комп on 07.08.2015.
 */
public class TasksParser implements Parser {

    @Override
    public Serializable parse(String string) throws JSONException, ParseException {
        JSONArray tasksArray = new JSONArray(string);
        ArrayList<TaskModel> taskModels = new ArrayList<>();
        for (int i = 0; i < tasksArray.length(); i++) {
            JSONObject taskJson = tasksArray.getJSONObject(i);
            TaskModel taskModel = new TaskModel();
            taskModel.setId(taskJson.getString(ID));
            taskModel.setCity(taskJson.getString(CITY));
            taskModel.setStreet(taskJson.getString(STREET));
            taskModel.setBuilding(taskJson.getString(BUILDING));
            taskModel.setApartment(taskJson.getInt(APARTMENT));
            taskModel.setUserName(taskJson.getString(USER_NAME));
            taskModel.setMeteringDeviceModel(taskJson.getString(METERING_DEVICE_MODEL));
            taskModel.setMeteringDeviceNumber(taskJson.getString(METERING_DEVICE_NUMBER));
            taskModel.setEnergyValueDate(taskJson.getString(ENERGY_VALUE_DATE));
            taskModel.setEnergyValue(taskJson.getInt(ENERGY_VALUE));
            taskModels.add(taskModel);
        }

        return taskModels;
    }
}
