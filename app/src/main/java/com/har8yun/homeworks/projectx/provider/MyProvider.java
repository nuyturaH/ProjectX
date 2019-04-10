package com.har8yun.homeworks.projectx.provider;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.service.WidgetService;

public class MyProvider extends AppWidgetProvider {

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int i : appWidgetIds) {
            updateWidget(context, appWidgetManager, i);
        }
    }

    void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_events);
        setUpdateTV(rv, context, appWidgetId);
        setList(rv, context, appWidgetId);
        appWidgetManager.updateAppWidget(appWidgetId, rv);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lvList);
    }

    void setUpdateTV(RemoteViews rv, Context context, int appWidgetId) {
        rv.setTextViewText(R.id.tvUpdate,"Updated " + sdf.format(new Date(System.currentTimeMillis())));
        Intent updIntent = new Intent(context, MyProvider.class);
        updIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] { appWidgetId });
        PendingIntent updPIntent = PendingIntent.getBroadcast(context, appWidgetId, updIntent, 0);
        rv.setOnClickPendingIntent(R.id.tvUpdate, updPIntent);
    }

    void setList(RemoteViews rv, Context context, int appWidgetId) {
        Intent adapter = new Intent(context, WidgetService.class);
        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        rv.setRemoteAdapter(R.id.lvList, adapter);
    }


}
