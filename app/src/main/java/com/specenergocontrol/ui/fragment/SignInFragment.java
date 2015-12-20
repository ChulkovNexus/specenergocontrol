package com.specenergocontrol.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;
import com.specenergocontrol.R;
import com.specenergocontrol.comands.CommandCallback;
import com.specenergocontrol.comands.postrequests.SignInCommand;
import com.specenergocontrol.ui.activity.TasksActivity;

import java.io.Serializable;

/**
 * Created by Комп on 25.06.2015.
 */
public class SignInFragment extends AsyncFragment {

    private EditText loginEditText;
    private EditText passwordEditText;
    private Button button;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_in, container, false);
        loginEditText = (EditText) v.findViewById(R.id.sign_in_login);
        passwordEditText = (EditText) v.findViewById(R.id.sign_in_password);
        button = (Button) v.findViewById(R.id.sign_in_button);
        content = v.findViewById(R.id.sign_in_content);
        progressBar = (ProgressView) v.findViewById(R.id.sign_in_progress);
        initClickListeners();
        return v;
    }



    private void initClickListeners() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginText = loginEditText.getText().toString();
                String passwordText = passwordEditText.getText().toString();
                if (checkCorrectData(loginText, passwordText)){
                    showProgress(true);
                    asyncTaskExecutor.execute(new SignInCommand(getActivity(), loginText, passwordText, false), new CommandCallback() {
                        @Override
                        public void commandSuccessExecuted(Serializable result) {
                            showProgress(false, new Runnable() {
                                @Override
                                public void run() {
                                    getActivity().startActivity(new Intent(getActivity(), TasksActivity.class));
                                    getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    getActivity().finish();
                                }
                            }, false);
                        }

                        @Override
                        public void commandExecutedWithError(int errorCode) {
                            showProgress(false, null, true);
                        }
                    });
                }
            }
        });
    }

    private boolean checkCorrectData(String loginText, String passwordText) {
        if (TextUtils.isEmpty(loginText)) {
            Toast.makeText(getActivity(), R.string.email_valid_error, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(passwordText)) {
            Toast.makeText(getActivity(), R.string.password_valid_error, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
