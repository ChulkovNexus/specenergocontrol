package com.specenergocontrol.comands;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.specenergocontrol.ui.activity.AuthActivity;
import com.specenergocontrol.utils.StoreUtils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by alexander on 05.05.15.
 */
public class ErrorHandler {

    public static void parseError(Activity activity, String string) {
        Log.d("123", string);
        try {
            JSONObject errorObj = new JSONObject(string);
            int errorCode = errorObj.getInt("status");
            if (errorCode == 403){
                StoreUtils.getInstance(activity).clearData();
                Intent intent = new Intent(activity, AuthActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
                activity.finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
