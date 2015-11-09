package com.specenergocontrol.ui.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.specenergocontrol.R;
import com.specenergocontrol.ui.fragment.SignInFragment;

public class AuthActivity extends AppCompatActivity {

    private static final String SIGN_IN_FRAGMENT_TAG = "sign_in_fragment_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        initToolBar();
        setSignInFragment();
    }

    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.auth_toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
    }

    private void setSignInFragment() {
        SignInFragment pinFragment = new SignInFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.auth_container, pinFragment, SIGN_IN_FRAGMENT_TAG);
        transaction.commit();
    }

}
