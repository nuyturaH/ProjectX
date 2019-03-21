package com.har8yun.homeworks.projectx.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class BuildMusclesViewModel extends ViewModel {

    private MutableLiveData<String> mBuildMusclesMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mUnlockNextMutableLiveData = new MutableLiveData<>();

    public void setBuildMuscles(String task){
        mBuildMusclesMutableLiveData.setValue(task);
    }

    public LiveData<String> getBuildMuscles(){
        return mBuildMusclesMutableLiveData;
    }

    public void setUnlockLevel(Boolean bool){
        mUnlockNextMutableLiveData.setValue(bool);
    }

    public LiveData<Boolean> isNextLevelUnlocked(){
        return mUnlockNextMutableLiveData;
    }

}
