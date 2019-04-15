package com.har8yun.homeworks.projectx.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class TaskViewModel extends ViewModel {

    private MutableLiveData<String> mTaskMutableLiveData = new MutableLiveData<>();

    //0-all are enabled, 1-only "loose weight" is disabled, 2-only "build muscles is disabled", 3-all are disabled
    private MutableLiveData<Integer> mDoneTaskMutableLiveData = new MutableLiveData<>();

    public void setTask(String task){
        mTaskMutableLiveData.setValue(task);
    }

    public LiveData<String> getTask(){
        return mTaskMutableLiveData;
    }

    public void setDoneTask(Integer task){
        mDoneTaskMutableLiveData.setValue(task);
    }

    public LiveData<Integer> getDoneTask(){
        return mDoneTaskMutableLiveData;
    }
}
