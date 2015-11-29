package com.specenergocontrol.utils;

import android.content.Context;

import com.specenergocontrol.model.TaskModel;
import com.specenergocontrol.model.Zone;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Alexander on 10.11.2015.
 */
public class RealmHelper {

    public static ArrayList<TaskModel> loadTask(Context context, String taskID) {
        Realm realm = Realm.getInstance(context);
        RealmResults<TaskModel> r = realm.where(TaskModel.class).equalTo("id", taskID).findAll();
        ArrayList<TaskModel> taskModels = new ArrayList<>(r);
        for (TaskModel task: taskModels) {
            loadZones(context, task);
        }
        realm.close();
        return taskModels;
    }

    public static ArrayList<TaskModel> loadStreetEntity(Context context, String taskID) {
        Realm realm = Realm.getInstance(context);
        RealmResults<TaskModel> r = realm.where(TaskModel.class).equalTo("id", taskID).findAll();
        ArrayList<TaskModel> taskModels = new ArrayList<>(r);
        for (TaskModel task: taskModels) {
            loadZones(context, task);
        }
        realm.close();
        return taskModels;
    }

    public static void saveTasksList(ArrayList<TaskModel> tasksList, Context context) {
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(tasksList);
        for (TaskModel task: tasksList) {
            saveZones(context, task, realm);
        }
        realm.commitTransaction();
        realm.close();
    }


    private static void saveZones(Context context, TaskModel taskModel, Realm realm){
        ArrayList<Zone> zones = taskModel.getZones();
        for (Zone zone: zones) {
            zone.setAccount(taskModel.getAccount());
        }
        realm.copyToRealmOrUpdate(zones);
        realm.close();
    }

    private static void loadZones(Context context, TaskModel taskModel){
        Realm realm = Realm.getInstance(context);
        RealmResults<Zone> r = realm.where(Zone.class).equalTo("account", taskModel.getAccount()).findAll();
        taskModel.setZones(new ArrayList<>(r));
        realm.close();
    }
}
