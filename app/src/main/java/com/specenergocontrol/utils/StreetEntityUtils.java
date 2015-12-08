package com.specenergocontrol.utils;

import android.content.Context;

import com.specenergocontrol.model.StreetEntity;
import com.specenergocontrol.model.TaskModel;

import java.util.ArrayList;
import java.util.Collection;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Комп on 29.11.2015.
 */
public class StreetEntityUtils {

    public static void createPrimary (StreetEntity streetEntity) {
        String primary = streetEntity.getEntityTitle() + streetEntity.isBuilding() + streetEntity.isAppartment() + streetEntity.getAccount();
        streetEntity.setPrimaryKey(primary);
    }

    public static StreetEntity containsReferenceTo(Collection<StreetEntity> collection,
                                                  StreetEntity element) {
        if (collection == null)
            throw new NullPointerException("collection cannot be null");

        for (StreetEntity o : collection) {
            if (((StreetEntity) o).isBuilding() == element.isBuilding() && element.getEntityTitle().equals(((StreetEntity) o).getEntityTitle())
                    && element.isAppartment() == ((StreetEntity) o).isAppartment()) {
                return o;
            }
        }
        return null;
    }

    public static void createStreetEntities(Context context) {
        ArrayList<StreetEntity> streets = new ArrayList<>();
        Realm realm = Realm.getInstance(context);
        //Clear StreetEntity table
        realm.beginTransaction();
        realm.where(StreetEntity.class).findAll().clear();
        realm.commitTransaction();

        //create StreetEntities from TaskModels in base
        RealmResults<TaskModel> allTasks = realm.where(TaskModel.class).findAll();
        allTasks.sort("street");
        StreetEntity lastStreet = new StreetEntity();
        for (TaskModel task : allTasks) {
            if (task.getStreet().equals(lastStreet.getEntityTitle())) {
                createBuildingForTask(task, lastStreet);
            } else {

                lastStreet = new StreetEntity();
                lastStreet.setEntityTitle(task.getStreet());
                StreetEntityUtils.createPrimary(lastStreet);
                createBuildingForTask(task, lastStreet);
                streets.add(lastStreet);
            }
        }
        for (StreetEntity street: streets) {
            checkFilled(street);
        }
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(streets);
        realm.commitTransaction();
    }

    //устанавливает Complited родителю если все ChildEntity complited
    private static void checkFilled(StreetEntity streetEntity) {
        RealmList<StreetEntity> buildings = streetEntity.getChildEntityArray();
        boolean streetComplited = true;
        for (StreetEntity building : buildings) {
            RealmList<StreetEntity> homes = building.getChildEntityArray();
            if (homes == null) {
                continue;
            } else {
                boolean complited = true;
                for (StreetEntity home : homes) {
                    if (home.isComplited()==false) {
                        complited = false;
                    } else {
                        building.setConteinsFilled(true);
                    }
                }
                building.setComplited(complited);
            }
            if (building.isComplited()==false)
                streetComplited = false;

            if (building.isComplited()==true || building.isConteinsFilled())
                streetEntity.setConteinsFilled(true);
        }
        streetEntity.setComplited(streetComplited);
    }

    private static void createBuildingForTask(TaskModel task, StreetEntity lastStreet) {
        StreetEntity building = new StreetEntity();
        building.setIsBuilding(true);
        building.setEntityTitle(task.getBuilding());
        StreetEntityUtils.createPrimary(building);
        if (task.getApartment()==0) {
            building.setTaskId(task.getId());
            building.setAccount(task.getAccount());
            building.setComplited(task.isFilled());
        }
        StreetEntity listBuilding = StreetEntityUtils.containsReferenceTo(lastStreet.getChildEntityArray(), building);
        if (listBuilding != null) {
            createAppartment(task, listBuilding);
        } else {
            lastStreet.getChildEntityArray().add(building);
            createAppartment(task, building);
        }
    }

    private static void createAppartment(TaskModel task, StreetEntity building) {
        StreetEntity appartment = new StreetEntity();
        appartment.setIsAppartment(true);
        appartment.setEntityTitle(String.valueOf(task.getApartment()));
        appartment.setTaskId(task.getId());
        appartment.setComplited(task.isFilled());
        appartment.setAccount(task.getAccount());
        StreetEntityUtils.createPrimary(appartment);
        building.getChildEntityArray().add(appartment);
    }
}
