package com.specenergocontrol.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.specenergocontrol.model.User;

/**
 * Created by Комп on 31.03.2015.
 */
public class StoreUtils {
    private static final long TOKEN_LIFETIME = 20 * 1000 * 60;

    private static final String USER_PREFERENCES = "user_preferences";
    private static final String TOKEN = "token";
    private static final String PIN_SETTED = "pin_setted";
    private static final String SENT_TOKEN_TO_SERVER = "sent_token_to_server";
    private static final String TOKEN_SET_TIME = "token_set_time";

    //User
    public static final String ID = "id";
    public static final String NAME = "name";
//    public static final String EMAIL = "email";
//    public static final String BALANCE = "balance";
//    public static final String NICKNAME = "nickname";
//    public static final String REGISTER_DATE = "register_date";
//    public static final String STATUS = "status";
//    public static final String IMAGE = "image";
//    public static final String AUTOLOGIN = "autologin";
//    public static final String NOTIFICATION = "notification";
//    public static final String BONUS_BALANCE = "bonus_balance";
//    public static final String PHONE = "phone";
//    public static final String PROMO = "promo_code";
    private static final String SESSION_ID = "session_id";
    private static final String TEST_BASE = "test_base";
    //

    private final SharedPreferences mPreferences;

    private static volatile StoreUtils instance;
    private String sessionId;

    public StoreUtils(Context context) {
        mPreferences = context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static StoreUtils getInstance(Context context) {
        StoreUtils localInstance = instance;
        if (localInstance == null) {
            synchronized (StoreUtils.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new StoreUtils(context);
                }
            }
        }
        return localInstance;
    }

    public String getToken() {
        return mPreferences.getString(TOKEN, "");
    }

    public void setToken(String token) {

        mPreferences
                .edit()
                .putLong(TOKEN_SET_TIME, System.currentTimeMillis())
                .putString(TOKEN, token)
                .commit();
    }

    public User getUser() {
        User user = new User();
        user.setId(mPreferences.getString(ID, ""));
//        user.setId(mPreferences.getInt(PHONE, 0));
        user.setName(mPreferences.getString(NAME, ""));
//        user.setEmail(mPreferences.getString(EMAIL, ""));
//        user.setBalance(mPreferences.getFloat(BALANCE, 0));
//        user.setBalance(mPreferences.getFloat(BONUS_BALANCE, 0));
//        user.setNickname(mPreferences.getString(NICKNAME, ""));
//        Date registerDate = new Date(mPreferences.getLong(REGISTER_DATE, 0));
//        user.setRegisterDate(registerDate);
//        user.setStatus(mPreferences.getInt(STATUS, 0));
//        user.setImage(mPreferences.getString(IMAGE, ""));
//        user.setAutologin(mPreferences.getInt(AUTOLOGIN, 0));
//        user.setNotification(mPreferences.getInt(NOTIFICATION, 0));
//        user.setPromo(mPreferences.getString(PROMO, ""));
        return user;
    }
//
    public void setUser(User user) {
        SharedPreferences.Editor edit = mPreferences.edit();
        edit.putString(ID, user.getId());
//        edit.putInt(PHONE, user.getPhone());
        edit.putString(NAME, user.getName());
//        edit.putString(EMAIL, user.getEmail());
//        edit.putFloat(BALANCE, (float) user.getBalance());
//        edit.putFloat(BONUS_BALANCE, (float) user.getBalance());
//        edit.putString(NICKNAME, user.getNickname());
//        edit.putLong(REGISTER_DATE, user.getRegisterDate().getTime());
//        edit.putInt(STATUS, user.getStatus());
//        edit.putString(IMAGE, user.getImage());
//        edit.putInt(AUTOLOGIN, user.getAutologin());
//        edit.putInt(NOTIFICATION, user.getNotification());
//        edit.putString(PROMO, user.getPromo());
        edit.commit();
    }

    public void clearData() {
        SharedPreferences.Editor edit = mPreferences.edit();
        edit.remove(TOKEN);
        edit.remove(TEST_BASE);
        edit.commit();
    }

    public void setDeviceTokenSended(boolean sended) {
        SharedPreferences.Editor edit = mPreferences.edit();
        edit.putBoolean(SENT_TOKEN_TO_SERVER, sended);
        edit.commit();
    }

    public boolean wasDeviceTokenSended() {
        return mPreferences.getBoolean(SENT_TOKEN_TO_SERVER, false);
    }

    public boolean getTokenLife() {
        long tokenSetTime = mPreferences.getLong(TOKEN_SET_TIME, 0);
        if (System.currentTimeMillis() - tokenSetTime < TOKEN_LIFETIME){
            return true;
        }
        return false;
    }

    public void setTestApi(boolean test) {
        SharedPreferences.Editor edit = mPreferences.edit();
        edit.putBoolean(TEST_BASE, test);
        edit.commit();
    }

    public boolean getTestApi() {
        return mPreferences.getBoolean(TEST_BASE, false);
    }

}
