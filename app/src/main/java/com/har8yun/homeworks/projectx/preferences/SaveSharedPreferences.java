package com.har8yun.homeworks.projectx.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SaveSharedPreferences {

    public static final String TAG = SaveSharedPreferences.class.getSimpleName();

    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }


    public void setLoggedIn(Context context, boolean loggedIn) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(TAG, loggedIn);
        editor.apply();
    }

    public boolean getLoggedStatus(Context context) {
        return getPreferences(context).getBoolean(TAG, false);
    }
}
