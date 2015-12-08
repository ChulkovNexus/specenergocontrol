package com.specenergocontrol.ui.fragment;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

import com.specenergocontrol.R;
import com.specenergocontrol.model.StreetEntity;
import com.specenergocontrol.ui.activity.TasksActivity;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Комп on 07.12.2015.
 */
public class FilledTasksListFragment extends TasksListFragment {

    @Override
    public void reloadFromBase() {
        Realm realm = Realm.getInstance(getActivity());
        RealmResults<StreetEntity> r = realm.where(StreetEntity.class).equalTo("isBuilding", false).
                equalTo("isAppartment", false).equalTo("conteinsFilled", true).findAll();
        streetEntitiesList.clear();
        streetEntitiesList.addAll(r);
        myAdapter.notifyDataSetChanged();
    }

    protected void setAppartmantsFragment(StreetEntity taskModel) {
        AppartmentsFilledFragmetn fragment = AppartmentsFilledFragmetn.getInstance(taskModel.getPrimaryKey());
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.tasks_main_container, fragment, TasksActivity.FRAGMENT_TAG);
        ft.addToBackStack(null);
        ft.commit();
        ((TasksActivity)getActivity()).currentFragmentChanged(fragment);
    }

    @Override
    protected void setTitle() {
        ((ActionBarActivity)getActivity()).setTitle(R.string.not_synced_tasks_title);
    }
}
