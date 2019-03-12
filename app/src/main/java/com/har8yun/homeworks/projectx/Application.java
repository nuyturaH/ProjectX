package com.har8yun.homeworks.projectx;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;


import com.har8yun.homeworks.projectx.preferences.PreferencesHelper;

import java.util.List;

public class Application extends android.app.Application {

    public static final String KEY_CAMERA_PACKAGE = "key_camera_package";
    public static Application application;

    public static Application get() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        detectCameraPackage();
    }

    public void detectCameraPackage() {
        if (PreferencesHelper.getCameraPackage(KEY_CAMERA_PACKAGE).equals("")) {
            PackageManager packageManager = Application.get().getPackageManager();
            List<ApplicationInfo> list = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
            for (int i = 0; i < list.size(); ++i) {
                if ((list.get(i).flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                    if (list.get(i).loadLabel(packageManager).toString().equalsIgnoreCase(getResources().getString(R.string.Camera))) {
                        PreferencesHelper.addCameraPackage(KEY_CAMERA_PACKAGE, list.get(i).packageName);
                        return;
                    }
                }
            }
        }
    }
}