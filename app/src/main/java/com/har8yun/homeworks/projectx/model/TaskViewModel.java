package com.har8yun.homeworks.projectx.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class TaskViewModel extends ViewModel {

    private MutableLiveData<String> mTaskMutableLiveData = new MutableLiveData<>();

    public void setTask(String task){
        mTaskMutableLiveData.setValue(task);
    }

    public LiveData<String> getTask(){
        return mTaskMutableLiveData;
    }
}
