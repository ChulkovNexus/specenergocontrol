package com.specenergocontrol.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.specenergocontrol.R;
import com.specenergocontrol.model.TaskModel;

/**
 * Created by Комп on 21.08.2015.
 */
public class FillTaskFragmetn extends AsyncFragment {

    private static final String EXTRA_TASK = "extra_task";
    private TaskModel task;

    public static FillTaskFragmetn getInstance(TaskModel task) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TASK, task);

        FillTaskFragmetn fragment = new FillTaskFragmetn();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fill_task, container, false);
        task = (TaskModel) getArguments().getSerializable(EXTRA_TASK);
        return v;
    }

}
