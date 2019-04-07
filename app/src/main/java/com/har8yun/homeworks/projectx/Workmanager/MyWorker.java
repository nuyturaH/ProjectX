package com.har8yun.homeworks.projectx.Workmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.har8yun.homeworks.projectx.activity.MainActivity;
import com.har8yun.homeworks.projectx.util.NotificationHelper;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {

    private NotificationHelper notificationHelper;
    private Date mNearestEventDate;
    private Date mCurrentDate;

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        notificationHelper = new NotificationHelper(context);

    }

    @NonNull
    @Override
    public Worker.Result doWork() {
        Log.e("workmng", "doWork: start");

        if (mNearestEventDate != null) {
            mCurrentDate = new Date();
            if (mNearestEventDate.getTime() - mCurrentDate.getTime() <= 3600000) {
                notificationHelper.createNotification("Upcoming Event", /*TODO get nearest event title*/"mmmmmmmm");
            }
        }
        Log.e("workmng", "doWork: end");
        return Worker.Result.success();
    }
}

