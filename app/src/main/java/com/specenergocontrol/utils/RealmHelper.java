package com.specenergocontrol.utils;

import android.app.Activity;
import android.content.Context;

import com.specenergocontrol.model.StreetEntity;
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
        return taskModels;
    }

    public static ArrayList<TaskModel> loadStreetEntity(Context context, String taskID) {
        Realm realm = Realm.getInstance(context);
        RealmResults<TaskModel> r = realm.where(TaskModel.class).equalTo("id", taskID).findAll();
        ArrayList<TaskModel> taskModels = new ArrayList<>(r);
        for (TaskModel task: taskModels) {
            loadZones(context, task);
        }
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
    }


    public static void saveZones(Context context, TaskModel taskModel, Realm realm){
        ArrayList<Zone> zones = taskModel.getZones();
        for (Zone zone: zones) {
            zone.setPrimaryKey(taskModel.getId() + zone.getName());
            zone.setAccount(taskModel.getAccount());
        }
        realm.copyToRealmOrUpdate(zones);
    }

    private static void loadZones(Context context, TaskModel taskModel){
        Realm realm = Realm.getInstance(context);
        RealmResults<Zone> r = realm.where(Zone.class).equalTo("account", taskModel.getAccount()).findAll();
        taskModel.setZones(new ArrayList<>(r));
    }

    public static ArrayList<TaskModel> loadComplitedTasks(Context context) {
        Realm realm = Realm.getInstance(context);
        RealmResults<TaskModel> r = realm.where(TaskModel.class).equalTo("filled", true).findAll();
        ArrayList<TaskModel> taskModels = new ArrayList<>(r);
        for (TaskModel task: taskModels) {
            loadZones(context, task);
        }
        return taskModels;
    }

    public static void removeFilledTasks(Context context) {
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();
        RealmResults<TaskModel> taskModels = realm.where(TaskModel.class).equalTo("filled", true).findAll();
        for (TaskModel task: taskModels) {
            removeZones(context, task);
        }
        taskModels.clear();
        realm.commitTransaction();
    }

    private static void removeZones(Context context, TaskModel task) {
        Realm realm = Realm.getInstance(context);
        realm.where(Zone.class).equalTo("account", task.getAccount()).findAll().clear();
    }

    public static void clearAll(Context context) {
        Realm instance = Realm.getInstance(context);
        instance.beginTransaction();
        instance.clear(TaskModel.class);
        instance.clear(Zone.class);
        instance.clear(StreetEntity.class);
        instance.commitTransaction();
    }
}
