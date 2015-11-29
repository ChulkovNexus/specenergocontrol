package com.specenergocontrol.model;

import java.io.Serializable;
import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Комп on 07.08.2015.
 */
public class StreetEntity extends RealmObject implements Serializable {

    @PrimaryKey
    private String primaryKey;
    private String taskId;
    private String entityTitle;
    private RealmList<StreetEntity> childEntityArray = new RealmList<>();
    private boolean complited;
    private boolean isBuilding;
    private boolean isAppartment;
    private String account;

    public String getEntityTitle() {
        return entityTitle;
    }

    public void setEntityTitle(String entityTitle) {
        this.entityTitle = entityTitle;
    }

    public RealmList<StreetEntity> getChildEntityArray() {
        return childEntityArray;
    }

    public void setChildEntityArray(RealmList<StreetEntity> childEntityArray) {
        this.childEntityArray = childEntityArray;
    }

    public boolean isComplited() {
        return complited;
    }

    public void setComplited(boolean complited) {
        this.complited = complited;
    }

    public boolean isBuilding() {
        return isBuilding;
    }

    public void setIsBuilding(boolean isBuilding) {
        this.isBuilding = isBuilding;
    }

    public boolean isAppartment() {
        return isAppartment;
    }

    public void setIsAppartment(boolean isAppartment) {
        this.isAppartment = isAppartment;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }


    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }
}
