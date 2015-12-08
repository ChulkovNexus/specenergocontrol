package com.specenergocontrol.ui.fragment;

import android.os.Bundle;

import com.specenergocontrol.model.StreetEntity;
import com.specenergocontrol.ui.adapter.TasksAppartmentsAdapter;
import com.specenergocontrol.utils.RealmHelper;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by Комп on 07.12.2015.
 */
public class AppartmentsFilledFragmetn extends AppartmentsFragmetn {

    public static AppartmentsFilledFragmetn getInstance(String entityKey) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ENTITY, entityKey);

        AppartmentsFilledFragmetn fragmetn = new AppartmentsFilledFragmetn();
        fragmetn.setArguments(bundle);
        return fragmetn;
    }

    @Override
    protected void loadFromBase() {
        String key = getArguments().getString(EXTRA_ENTITY);
        Realm realm = Realm.getInstance(getActivity());
        buildingEntity = realm.where(StreetEntity.class).equalTo("primaryKey", key).findFirst();
        realm.close();
        ArrayList<StreetEntity> childarray = new ArrayList<>();
        for (StreetEntity appartment: buildingEntity.getChildEntityArray()) {
            if(appartment.isComplited()){
                childarray.add(appartment);
            }
        }
        myAdapter = new TasksAppartmentsAdapter(getActivity(), childarray);
    }
}
