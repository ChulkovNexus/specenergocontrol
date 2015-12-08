package com.specenergocontrol.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.specenergocontrol.R;
import com.specenergocontrol.comands.AsyncTaskExecutor;
import com.specenergocontrol.comands.Command;
import com.specenergocontrol.comands.CommandCallback;
import com.specenergocontrol.comands.getrequests.GetTasksCommand;
import com.specenergocontrol.comands.postrequests.SendTasksCommand;
import com.specenergocontrol.model.StreetEntity;
import com.specenergocontrol.model.TaskModel;
import com.specenergocontrol.ui.fragment.AsyncFragment;
import com.specenergocontrol.ui.fragment.TasksListFragment;
import com.specenergocontrol.ui.fragment.MenuFragment;
import com.specenergocontrol.utils.RealmHelper;
import com.specenergocontrol.utils.StreetEntityUtils;

import org.json.JSONException;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeoutException;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Комп on 30.06.2015.
 */
public class TasksActivity extends ActionBarActivity {

    public static final String FRAGMENT_TAG = "dashboard_fragment_tag";
    private DrawerLayout mDrawerLayout;
    private Fragment currentFragment;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    public SyncHelper syncHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        syncHelper = new SyncHelper();
        initToolBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        syncHelper.attach(this);
        if (currentFragment == null) {
            setMenuFragment();
            setTasksFragment();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        syncHelper.detach();
        mDrawerLayout.closeDrawers();
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setFocusableInTouchMode(false);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,  mDrawerLayout, toolbar,
                0, 0
        );
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mDrawerToggle.setHomeAsUpIndicator(getDrawerToggleDelegate().getThemeUpIndicator());
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    public void closeDrawer(){
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    public void setDrawerIndicatorEnabled(boolean enabled) {
        mDrawerToggle.setDrawerIndicatorEnabled(enabled);
    }

    private void setTasksFragment() {
        currentFragment = new TasksListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.tasks_main_container, currentFragment, FRAGMENT_TAG);
        transaction.commit();
    }

    private void setMenuFragment() {
        MenuFragment fragment = new MenuFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.menu_container, fragment, FRAGMENT_TAG);
        transaction.commit();
    }

    public void currentFragmentChanged(Fragment fragment) {
        closeDrawer();
        currentFragment = fragment;
    }

    public void startSync() {
        syncHelper.startSync();
    }

    private class SyncHelper {
        private final AsyncTaskExecutor asyncTaskExecutor;
        private ProgressDialog progress;

        public SyncHelper() {
            asyncTaskExecutor = new AsyncTaskExecutor();
        }

        public void attach(Activity activity) {
            asyncTaskExecutor.attach(activity);
        }

        public void detach() {
            asyncTaskExecutor.detach();
        }

        public void startSync() {
            Context context = asyncTaskExecutor.getActivity();
            progress =  ProgressDialog.show(context, context.getString(R.string.syncronize_title), context.getString(R.string.wait_text), true);
            asyncTaskExecutor.execute(new Command(asyncTaskExecutor.getActivity()) {
                @Override
                public Serializable execute() throws ParseException, JSONException, IOException, TimeoutException {
                    ArrayList<TaskModel> filledTaskModels = RealmHelper.loadComplitedTasks(asyncTaskExecutor.getActivity());
                    if (filledTaskModels!=null && !filledTaskModels.isEmpty()) {
                        SendTasksCommand sendTasksCommand = new SendTasksCommand(asyncTaskExecutor.getActivity(), filledTaskModels);
                        sendTasksCommand.execute();
                        RealmHelper.removeFilledTasks(asyncTaskExecutor.getActivity());
                    }

                    GetTasksCommand getTasksCommand = new GetTasksCommand(asyncTaskExecutor.getActivity());
                    ArrayList<TaskModel> tasksList = (ArrayList<TaskModel>) getTasksCommand.execute();

                    RealmHelper.saveTasksList(tasksList, asyncTaskExecutor.getActivity());
                    StreetEntityUtils.createStreetEntities(asyncTaskExecutor.getActivity());
                    return super.execute();
                }
            }, new CommandCallback() {
                @Override
                public void commandSuccessExecuted(Serializable result) {
                    //TODO try change it to realm callback
                    ((AsyncFragment) currentFragment).reloadFromBase();
                    progress.dismiss();
                }

                @Override
                public void commandExecutedWithError(int errorCode) {
                    progress.dismiss();
                }
            });
        }

    }
}
