package com.specenergocontrol.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;

import com.specenergocontrol.R;
import com.specenergocontrol.model.StreetEntity;
import com.specenergocontrol.model.TaskModel;
import com.specenergocontrol.ui.activity.TasksActivity;
import com.specenergocontrol.ui.adapter.TasksAppartmentsAdapter;
import com.specenergocontrol.utils.RealmHelper;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Комп on 29.11.2015.
 */
public class AppartmentsFragmetn extends ListFragment {

    private static final String EXTRA_ENTITY = "extra_entyty";
    private TasksAppartmentsAdapter myAdapter;
    private StreetEntity buildingEntity;

    public static AppartmentsFragmetn getInstance(String entityKey) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ENTITY, entityKey);

        AppartmentsFragmetn fragmetn = new AppartmentsFragmetn();
        fragmetn.setArguments(bundle);
        return fragmetn;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String key = getArguments().getString(EXTRA_ENTITY);
        Realm realm = Realm.getInstance(getActivity());
        buildingEntity = realm.where(StreetEntity.class).equalTo("primaryKey", key).findFirst();
        realm.close();

        myAdapter = new TasksAppartmentsAdapter(getActivity(), buildingEntity.getChildEntityArray());
        setListAdapter(myAdapter);
        getListView().setDivider(null);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setFillTaskFragment(myAdapter.getItem(position).getTaskId());
            }
        });
        setEmptyText(getString(R.string.there_are_no_tasks));
        ((ActionBarActivity)getActivity()).setTitle(getString(R.string.tasks_title));
        ((TasksActivity)getActivity()).setDrawerIndicatorEnabled(false);
    }

    private void setFillTaskFragment(String taskId) {
        FillTaskFragmetn fragment = FillTaskFragmetn.getInstance(taskId);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.tasks_main_container, fragment, TasksActivity.FRAGMENT_TAG);
        ft.addToBackStack(null);
        ft.commit();
    }
}
