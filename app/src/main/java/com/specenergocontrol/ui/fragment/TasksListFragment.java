package com.specenergocontrol.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;


import com.rey.material.widget.ProgressView;
import com.specenergocontrol.R;
import com.specenergocontrol.model.StreetEntity;
import com.specenergocontrol.model.TaskModel;
import com.specenergocontrol.ui.activity.TasksActivity;
import com.specenergocontrol.ui.adapter.TasksStreetAdapter;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Комп on 07.07.2015.
 */
public class TasksListFragment extends AsyncFragment {

    private ExpandableListView listView;
    private TasksStreetAdapter myAdapter;
    private ArrayList<StreetEntity> streetEntitiesList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tasks_list, container, false);
        listView = (ExpandableListView) v.findViewById(R.id.tasks_street_list_view);
        myAdapter = new TasksStreetAdapter(getActivity(), streetEntitiesList);
        listView.setAdapter(myAdapter);
        listView.setEmptyView(v.findViewById(R.id.tasks_street_list_view));
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String street = myAdapter.getGroup(groupPosition).getEntityTitle();
                String building = myAdapter.getChild(groupPosition, childPosition).getEntityTitle();
                ArrayList<TaskModel> tasksList = loadTasks(street, building);
                if (tasksList.size()>1) {
                    setAppartmantsFragment(tasksList);
                }else {
                    setFillTaskFragment(tasksList.get(0));
                }
                return false;
            }
        });
        progressBar = (ProgressView) v.findViewById(R.id.tasks_street_progress);
        content = v.findViewById(R.id.tasks_street_content_view);
        reloadFromBase();
        return v;

    }


    private void setAppartmantsFragment(ArrayList<TaskModel> taskModel) {

    }

    private void setFillTaskFragment(TaskModel task) {
        FillTaskFragmetn fragment = FillTaskFragmetn.getInstance(task);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.tasks_main_container, fragment, TasksActivity.FRAGMENT_TAG);
        ft.commit();
        ((TasksActivity)getActivity()).currentFragmentChanged(fragment);
    }

    private ArrayList<TaskModel> loadTasks(String street, String building) {
        Realm realm = Realm.getInstance(getActivity());
        RealmResults<TaskModel> r = realm.where(TaskModel.class).equalTo("street", street).equalTo("building", building).findAll();
        return new ArrayList<>(r);
    }

    @Override
    public void reloadFromBase() {
        Realm realm = Realm.getInstance(getActivity());
        RealmResults<StreetEntity> r = realm.where(StreetEntity.class).equalTo("isBuilding", false).findAll();
        streetEntitiesList.clear();
        streetEntitiesList.addAll(r);
        myAdapter.notifyDataSetChanged();
    }
}