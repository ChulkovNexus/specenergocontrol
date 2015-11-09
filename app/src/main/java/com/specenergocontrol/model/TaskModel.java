package com.specenergocontrol.model;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Комп on 06.08.2015.
 */
public class TaskModel extends RealmObject implements Serializable {

    @PrimaryKey
    private String id;
    private String street;
    private String building;
    private String userName;
    private String city;
    private String meteringDeviceModel;
    private String energyValueDate;
    private int energyValue;
    private String meteringDeviceNumber;
    private int apartment;
    private boolean complited;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public void setEnergyValueDate(String energyValueDate) {
        this.energyValueDate = energyValueDate;
    }

    public String getEnergyValueDate() {
        return energyValueDate;
    }

    public void setEnergyValue(int energyValue) {
        this.energyValue = energyValue;
    }

    public int getEnergyValue() {
        return energyValue;
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
}
