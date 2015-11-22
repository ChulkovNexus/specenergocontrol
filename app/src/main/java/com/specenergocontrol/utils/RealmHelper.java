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

    public static ArrayList<TaskModel> loadTasks(Context context, String street, String building) {
        Realm realm = Realm.getInstance(context);
        RealmResults<TaskModel> r = realm.where(TaskModel.class).equalTo("street", street).equalTo("building", building).findAll();
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


    private static void saveZones(Context context, TaskModel taskModel, Realm realm){
        ArrayList<Zone> zones = taskModel.getZones();
        for (Zone zone: zones) {
            zone.setTaskId(taskModel.getId());
        }
        realm.copyToRealmOrUpdate(zones);
    }

    private static void loadZones(Context context, TaskModel taskModel){
        Realm realm = Realm.getInstance(context);
        RealmResults<Zone> r = realm.where(Zone.class).equalTo("taskId", taskModel.getId()).findAll();
        taskModel.setZones(new ArrayList<>(r));
    }
}
