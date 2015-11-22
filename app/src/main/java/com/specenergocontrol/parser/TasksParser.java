package com.specenergocontrol.parser;

import com.specenergocontrol.model.TaskModel;
import com.specenergocontrol.model.Zone;

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
        JSONObject rootObject = new JSONObject(string);
        JSONArray tasksArray = rootObject.getJSONArray(DATA);
        ArrayList<TaskModel> taskModels = new ArrayList<>();
        for (int i = 0; i < tasksArray.length(); i++) {
            JSONObject taskJson = tasksArray.getJSONObject(i);
            TaskModel taskModel = new TaskModel();
            taskModel.setAccount(taskJson.getString(ACCOUNT));
            taskModel.setId(taskModel.getAccount());
            taskModel.setCity(taskJson.getString(CITY));
            taskModel.setStreet(taskJson.getString(STREET));
            String housing = null;
            if (!taskJson.isNull(HOUSING)){
                housing = taskJson.getString(HOUSING);
            }
            taskModel.setBuilding(housing==null ? taskJson.getString(BUILDING) : taskJson.getString(BUILDING) + housing);
            if (!taskJson.isNull(APARTMENT))
                taskModel.setApartment(taskJson.getInt(APARTMENT));
            if (!taskJson.isNull(METERING_DEVICE_SCALE))
                taskModel.setApartment(taskJson.getInt(METERING_DEVICE_SCALE));
            taskModel.setMeteringDeviceModel(taskJson.getString(METERING_DEVICE_MODEL));
            taskModel.setMeteringDeviceNumber(taskJson.getString(METERING_DEVICE_NUMBER));
            parseZones(taskModel, taskJson);
            taskModels.add(taskModel);
        }
        return taskModels;
    }

    private void parseZones(TaskModel taskModel, JSONObject taskJson) throws JSONException {
        ArrayList<Zone> zones = new ArrayList<>();
        JSONArray zonesJson = taskJson.getJSONArray(ZONES);
        for (int i = 0; i < zonesJson.length(); i++) {
            Zone zone = new Zone();
            JSONObject zoneJson = zonesJson.getJSONObject(i);
            zone.setPeriod(zoneJson.getInt(TYPE));
            zone.setName(zoneJson.getString(NAME));
            zones.add(zone);
        }
        taskModel.setZones(zones);
    }
}
