package com.har8yun.homeworks.projectx.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.har8yun.homeworks.projectx.adapter.WidgetAdapter;

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetAdapter(getApplicationContext(), intent);
    }

}
