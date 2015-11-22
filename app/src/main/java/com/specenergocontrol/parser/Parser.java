package com.specenergocontrol.parser;

import org.json.JSONException;

import java.io.Serializable;
import java.text.ParseException;

/**
 * Created by Комп on 30.03.2015.
 */
public interface Parser<T extends Serializable> {

    String TOKEN = "Token";
    String ID = "Id";
    String CITY = "City";
    String STREET = "Street";
    String BUILDING = "Building";
    String APARTMENT = "Apartment";
    String USER_NAME = "UserName";
    String METERING_DEVICE_MODEL = "MeteringDeviceModel";
    String METERING_DEVICE_NUMBER = "MeteringDeviceNumber";
    String ENERGY_VALUE_DATE = "EnergyValueDate";
    String ENERGY_VALUE = "EnergyValue";
    String DATA = "Data";
    String METERING_DEVICE_SCALE = "MeteringDeviceScale";
    String HOUSING = "Housing";
    String ACCOUNT = "Account";
    String ZONES = "Zones";
    String TYPE = "Type";
    String NAME = "Name";


    public T parse(String string) throws JSONException, ParseException;
}
