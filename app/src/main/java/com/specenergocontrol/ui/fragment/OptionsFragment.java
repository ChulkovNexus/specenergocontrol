package com.specenergocontrol.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.ProgressView;
import com.specenergocontrol.R;
import com.specenergocontrol.comands.CommandCallback;
import com.specenergocontrol.comands.postrequests.SignInCommand;
import com.specenergocontrol.ui.activity.StartActivity;
import com.specenergocontrol.utils.RealmHelper;
import com.specenergocontrol.utils.StoreUtils;

import java.io.Serializable;

/**
 * Created by Комп on 11.12.2015.
 */
public class OptionsFragment extends AsyncFragment {

    private EditText loginEditText;
    private EditText passwordEditText;
    private Button signInButton;
    private Button signOutButton;
    private TextView nameText;
    private CheckBox testBaseCheckbox;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_options, container, false);
        nameText = (TextView) v.findViewById(R.id.sign_in_user_name_text);
        loginEditText = (EditText) v.findViewById(R.id.sign_in_login);
        passwordEditText = (EditText) v.findViewById(R.id.sign_in_password);
        testBaseCheckbox = (CheckBox) v.findViewById(R.id.test_base_checkbox);
        signInButton = (Button) v.findViewById(R.id.sign_in_button);
        signOutButton = (Button) v.findViewById(R.id.sign_out_button);
        content = v.findViewById(R.id.sign_in_content);
        progressBar = (ProgressView) v.findViewById(R.id.sign_in_progress);
        initClickListeners();

        String userName = StoreUtils.getInstance(getActivity()).getUser().getName();
        nameText.setText(userName);

        ((ActionBarActivity)getActivity()).setTitle(R.string.options_title);
        return v;
    }

    private void initClickListeners() {
        testBaseCheckbox.setChecked(StoreUtils.getInstance(getActivity()).getTestApi());
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String loginText = loginEditText.getText().toString();
                String passwordText = passwordEditText.getText().toString();
                if (checkCorrectData(loginText, passwordText)) {
                    showProgress(true);
                    asyncTaskExecutor.execute(new SignInCommand(getActivity(), loginText, passwordText, testBaseCheckbox.isChecked()), new CommandCallback() {
                        @Override
                        public void commandSuccessExecuted(Serializable result) {
                            showProgress(false, new Runnable() {
                                @Override
                                public void run() {
                                    StoreUtils.getInstance(getActivity()).setTestApi(testBaseCheckbox.isChecked());
                                    String userName = StoreUtils.getInstance(getActivity()).getUser().getName();
                                    nameText.setText(userName);
                                }
                            }, true);
                        }

                        @Override
                        public void commandExecutedWithError(int errorCode) {
                            showProgress(false, null, true);
                        }
                    });
                }
            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showQuietDialog();
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
