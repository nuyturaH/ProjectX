package com.har8yun.homeworks.projectx.util.workManager;

import android.content.Context;
import android.support.annotation.NonNull;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class EventWorker extends Worker {

    public EventWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        return null;
    }
}
