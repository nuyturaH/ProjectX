package com.har8yun.homeworks.projectx.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.har8yun.homeworks.projectx.model.User;

public class SaveSharedPreferences {

    public static final String TAG = SaveSharedPreferences.class.getSimpleName();
    public static final String USER_KEY = "user_key";
    public static final String ZOOM_BUTTONS = "zoom_buttons";
    public static final String THEME = "theme change";

    public static final String RED = "red";

    User mUser;

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

    public void setZoom(Context context, boolean isZoom) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(ZOOM_BUTTONS, isZoom);
        editor.apply();
    }

    public boolean getZoom(Context context) {
        return getPreferences(context).getBoolean(ZOOM_BUTTONS, false);
    }

    public void setTheme(Context context, String color) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(THEME, color);
        editor.apply();
    }

    public String getTheme(Context context) {
        return getPreferences(context).getString(THEME, RED);
    }

    public void setCurrentUser(Context context,User mUser)
    {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        Gson gson = new Gson();
        String json = gson.toJson(mUser);
        editor.putString(USER_KEY,json);
        editor.commit();
    }

    public User getCurrentUser(Context context)
    {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        Gson gson = new Gson();
        String json = getPreferences(context).getString(USER_KEY, "");
        User mUser = gson.fromJson(json, User.class);
        return mUser;
    }
}
