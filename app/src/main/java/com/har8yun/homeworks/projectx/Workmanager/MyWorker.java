package com.har8yun.homeworks.projectx.Workmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {


    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Worker.Result doWork() {
        Log.e("workmng", "doWork: start");

        //TODO yete motaka 1 jamva yntacqum event ka, notification cuyc tur


        Log.e("workmng", "doWork: end");

        return Worker.Result.success();
    }
}

