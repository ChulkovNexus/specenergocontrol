package com.specenergocontrol.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;


import com.rey.material.widget.ProgressView;
import com.specenergocontrol.R;
import com.specenergocontrol.model.StreetEntity;
import com.specenergocontrol.model.TaskModel;
import com.specenergocontrol.ui.activity.TasksActivity;
import com.specenergocontrol.ui.adapter.TasksStreetAdapter;
import com.specenergocontrol.utils.RealmHelper;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Комп on 07.07.2015.
 */
public class TasksListFragment extends AsyncFragment {

    private ExpandableListView listView;
    protected TasksStreetAdapter myAdapter;
    protected ArrayList<StreetEntity> streetEntitiesList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.tasks_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sync) {
            ((TasksActivity)getActivity()).startSync();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

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
                if (TextUtils.isEmpty(myAdapter.getChild(groupPosition, childPosition).getTaskId())) {
                    setAppartmantsFragment(myAdapter.getChild(groupPosition, childPosition));
                } else {
                    setFillTaskFragment(myAdapter.getChild(groupPosition, childPosition).getTaskId());
                }
                return false;
            }
        });
        progressBar = (ProgressView) v.findViewById(R.id.tasks_street_progress);
        content = v.findViewById(R.id.tasks_street_content_view);
        reloadFromBase();
        setTitle();
        ((TasksActivity)getActivity()).setDrawerIndicatorEnabled(true);
        return v;

    }

    protected void setTitle() {
        ((ActionBarActivity)getActivity()).setTitle(R.string.tasks_title);
    }

    protected void setAppartmantsFragment(StreetEntity taskModel) {
        AppartmentsFragmetn fragment = AppartmentsFragmetn.getInstance(taskModel.getPrimaryKey());
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.tasks_main_container, fragment, TasksActivity.FRAGMENT_TAG);
        ft.addToBackStack(null);
        ft.commit();
        ((TasksActivity)getActivity()).currentFragmentChanged(fragment);
    }

    private void setFillTaskFragment(String taskID) {
        FillTaskFragmetn fragment = FillTaskFragmetn.getInstance(taskID);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.tasks_main_container, fragment, TasksActivity.FRAGMENT_TAG);
        ft.addToBackStack(null);
        ft.commit();
        ((TasksActivity)getActivity()).currentFragmentChanged(fragment);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof TasksActivity) {
            ((TasksActivity)getActivity()).currentFragmentChanged(this);
        }
    }
    @Override
    public void reloadFromBase() {
        Realm realm = Realm.getInstance(getActivity());
        RealmResults<StreetEntity> r = realm.where(StreetEntity.class).equalTo("isBuilding", false).
                equalTo("isAppartment", false).findAll();
        streetEntitiesList.clear();
        streetEntitiesList.addAll(r);
        myAdapter.notifyDataSetChanged();
    }
}