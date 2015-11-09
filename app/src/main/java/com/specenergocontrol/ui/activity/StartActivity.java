package com.specenergocontrol.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;

import com.specenergocontrol.R;
import com.specenergocontrol.utils.StoreUtils;

/**
 * Created by Комп on 25.07.2015.
 */
public class StartActivity extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        if (TextUtils.isEmpty(StoreUtils.getInstance(getBaseContext()).getToken())){
            startActivity(new Intent(getBaseContext(), AuthActivity.class));
        } else {
            startActivity(new Intent(getBaseContext(), TasksActivity.class));
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
