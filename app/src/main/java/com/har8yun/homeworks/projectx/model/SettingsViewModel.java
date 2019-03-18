package com.har8yun.homeworks.projectx.model;

import android.arch.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel
{
    private boolean zoomButtons;
    private boolean userInfo;

    public boolean isZoomButtons() {
        return zoomButtons;
    }

    public void setZoomButtons(boolean zoomButtons) {
        this.zoomButtons = zoomButtons;
    }

    public boolean isUserInfo() {
        return userInfo;
    }

    public void setUserInfo(boolean userInfo) {
        this.userInfo = userInfo;
    }
}
