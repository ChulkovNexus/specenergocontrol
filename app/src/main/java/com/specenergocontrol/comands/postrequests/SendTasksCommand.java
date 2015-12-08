package com.specenergocontrol.comands.postrequests;

import android.content.Context;

import com.specenergocontrol.model.TaskModel;
import com.specenergocontrol.model.Zone;
import com.specenergocontrol.parser.SendTasksParser;
import com.specenergocontrol.parser.SignInParser;
import com.specenergocontrol.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Комп on 30.11.2015.
 */
public class SendTasksCommand extends PostJsonRequest {

    protected String url = Constants.BASE_URL + "/audit";

    public SendTasksCommand(Context context, ArrayList<TaskModel> taskModels) {
        super(context);
        setUrl(url);
        setParams(generateJson(taskModels));
        setParser(new SendTasksParser(context, taskModels));
    }

    private String generateJson(ArrayList<TaskModel> taskModels) {
        JSONObject result = new JSONObject();
        try {
            JSONArray mainArray = new JSONArray();
            for (TaskModel model : taskModels) {
                JSONObject taskObject = new JSONObject();
                taskObject.put("Account", model.getAccount());
                taskObject.put("MeteringDeviceScale", model.getMeteringDeviceScale());
                String visitDateStr = Constants.getVisitDateFormatter().format(model.getVisitDate()) + "Z";
                taskObject.put("VisitDate", visitDateStr);
                taskObject.put("Comment", model.getComment());
                taskObject.put("EnergyValues", createEnergiValues(model));
                mainArray.put(taskObject);
            }
            result.put("Data",  mainArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    private JSONArray createEnergiValues(TaskModel task) throws JSONException {
        JSONArray energyValues = new JSONArray();
        for (Zone zone: task.getZones()) {
            JSONObject energyValueObject = new JSONObject();
            energyValueObject.put("Period", zone.getPeriod());
            energyValueObject.put("Value", zone.getValue());
            energyValues.put(energyValueObject);
        }
        return energyValues;
    }
}
