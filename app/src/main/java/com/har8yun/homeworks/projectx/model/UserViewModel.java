package com.har8yun.homeworks.projectx.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {
    private MutableLiveData<User> mUserMutableLiveData = new MutableLiveData<>();

    public void setUser(User user){
        mUserMutableLiveData.setValue(user);
    }

    public LiveData<User> getUser(){
        return mUserMutableLiveData;
    }
}
