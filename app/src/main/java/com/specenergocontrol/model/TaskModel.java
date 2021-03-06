package com.specenergocontrol.model;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by Комп on 06.08.2015.
 */
@RealmClass
public class TaskModel extends RealmObject implements Serializable {

    @PrimaryKey
    private String id;
    private String street;
    private String building;
    private String userName;
    private String city;
    private String meteringDeviceModel;
    private String meteringDeviceNumber;
    private int apartment;
    private boolean complited;
    private String account;
    private String housing;
    @Ignore
    private ArrayList<Zone> zones;
    private String photoFilePath;
    private int meteringDeviceScale;
    private Date visitDate;
    private String comment;
    private String photoUrl;
    private boolean filled;
    private boolean sync;

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getBuilding() {
        return building;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setMeteringDeviceModel(String meteringDeviceModel) {
        this.meteringDeviceModel = meteringDeviceModel;
    }

    public String getMeteringDeviceModel() {
        return meteringDeviceModel;
    }

    public void setMeteringDeviceNumber(String meteringDeviceNumber) {
        this.meteringDeviceNumber = meteringDeviceNumber;
    }

    public String getMeteringDeviceNumber() {
        return meteringDeviceNumber;
    }

    public void setApartment(int apartment) {
        this.apartment = apartment;
    }

    public int getApartment() {
        return apartment;
    }

    public boolean isComplited() {
        return complited;
    }

    public void setComplited(boolean complited) {
        this.complited = complited;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }


    public void setHousing(String housing) {
        this.housing = housing;
    }

    public String getHousing() {
        return housing;
    }

    public void setZones(ArrayList<Zone> zones) {
        this.zones = zones;
    }

    public ArrayList<Zone> getZones() {
        return zones;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setPhotoFilePath(String photoFilePath) {
        this.photoFilePath = photoFilePath;
    }

    public String getPhotoFilePath() {
        return photoFilePath;
    }

    public void setMeteringDeviceScale(int meteringDeviceScale) {
        this.meteringDeviceScale = meteringDeviceScale;
    }

    public int getMeteringDeviceScale() {
        return meteringDeviceScale;
    }

    public Date getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;

    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public boolean isFilled() {
        return filled;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public boolean isSync() {
        return sync;
    }
}
