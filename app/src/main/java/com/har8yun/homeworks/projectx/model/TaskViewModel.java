package com.har8yun.homeworks.projectx.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class TaskViewModel extends ViewModel {

    private MutableLiveData<String> mTaskMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> mDoneTaskMutableLiveData = new MutableLiveData<>();//0-all are enabeled, 1

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
