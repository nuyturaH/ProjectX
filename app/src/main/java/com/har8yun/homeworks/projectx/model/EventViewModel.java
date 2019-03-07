package com.har8yun.homeworks.projectx.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class EventViewModel extends ViewModel {

    private MutableLiveData<Event> mEventMutableLiveData = new MutableLiveData<>();

    public void setEvent(Event event){
        mEventMutableLiveData.setValue(event);
    }

    public LiveData<Event> getEvent(){
        return mEventMutableLiveData;
    }
}
