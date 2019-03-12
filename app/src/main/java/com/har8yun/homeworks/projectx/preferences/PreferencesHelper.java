package com.har8yun.homeworks.projectx.preferences;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.har8yun.homeworks.projectx.Application;

public class PreferencesHelper {
    public static String getCameraPackage(String key) {
        return PreferenceManager.getDefaultSharedPreferences(Application.get()).getString(key, "");
    }

    public static void addCameraPackage(String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(Application.get()).edit();
        editor.putString(key, value);
        editor.commit();
    }

}
