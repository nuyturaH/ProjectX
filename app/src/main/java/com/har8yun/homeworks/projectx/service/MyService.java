package com.har8yun.homeworks.projectx.service;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.har8yun.homeworks.projectx.adapter.MyFactory;

public class MyService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyFactory(getApplicationContext(), intent);
    }

}
