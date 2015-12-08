package com.specenergocontrol.ui.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.specenergocontrol.R;
import com.specenergocontrol.model.StreetEntity;
import com.specenergocontrol.model.TaskModel;
import com.specenergocontrol.model.Zone;
import com.specenergocontrol.ui.activity.TasksActivity;
import com.specenergocontrol.ui.activity.StartActivity;
import com.specenergocontrol.utils.RealmHelper;
import com.specenergocontrol.utils.StoreUtils;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by Комп on 13.07.2015.
 */
public class MenuFragment extends Fragment {

    private enum State {
        Tasks,
        DoesntSendedTasks,
        Search
    }

    private View tasksButton;
    private View notSendedTasks;
    private View serchButton;
    private View logoutButton;

    private State currentState = State.Tasks;
    private ArrayList<View> buttonsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        buttonsList.add(tasksButton = v.findViewById(R.id.menu_tasks_button));
        buttonsList.add(notSendedTasks = v.findViewById(R.id.menu_doesnt_sended_tasks_button));
        buttonsList.add(serchButton = v.findViewById(R.id.menu_search_button));
        logoutButton = v.findViewById(R.id.menu_logout_button);

        tasksButton.setBackgroundColor(getResources().getColor(R.color.app_yellow_color));
        initClickListeners();
        return v;
    }

    private void initClickListeners() {
        tasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentState != State.Tasks) {
                    currentState = State.Tasks;
                    TasksListFragment fragment = new TasksListFragment();
                    setFragment(fragment, v);
                }
            }
        });
        notSendedTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentState != State.DoesntSendedTasks) {
                    currentState = State.DoesntSendedTasks;
                    FilledTasksListFragment fragment = new FilledTasksListFragment();
                    setFragment(fragment, v);
                }
            }
        });
        serchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuietDialog();
            }
        });
    }

    private void setFragment(Fragment fragment, View button) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.tasks_main_container, fragment, TasksActivity.FRAGMENT_TAG);
        ft.commit();
        ((TasksActivity)getActivity()).currentFragmentChanged(fragment);
        setAnotherButtonsUnchecked();
        button.setBackgroundColor(getResources().getColor(R.color.app_yellow_color));
    }

    private void setAnotherButtonsUnchecked() {
        for (View button:buttonsList) {
            button.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
    }

    private void showQuietDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.queit_dialog_allert)
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                StoreUtils.getInstance(getActivity()).clearData();
                                RealmHelper.clearAll(getActivity());
                                Intent intent = new Intent(getActivity(), StartActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                getActivity().startActivity(intent);
                                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                getActivity().finish();
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(android.R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
        builder.create().show();
    }
}
