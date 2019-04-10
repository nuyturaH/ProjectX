package com.har8yun.homeworks.projectx.notification;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.har8yun.homeworks.projectx.activity.MainActivity;
import com.har8yun.homeworks.projectx.model.Event;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.model.UserViewModel;
import com.har8yun.homeworks.projectx.util.NotificationHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {

    private NotificationHelper notificationHelper;
    private Date mNearestEventDate;
    private Date mCurrentDate;
    DatabaseReference mFirebaseReference;
    private UserViewModel mUserViewModel;
    private User mUser;
    private List<Event> mEventList = new ArrayList<>();
    Event mNearestEvent;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
    String currentUserId = "";

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        currentUserId = mFirebaseUser.getUid();
        //TODO getUser methody mUseri mej gruma henc ed pahin sign in exac userin,, isk getEvents methody mEventList um grum a ed useri eventnery

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

    private void getUser()
    {
        mFirebaseReference = FirebaseDatabase.getInstance().getReference("users");
        mFirebaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if(user.getId().equals(currentUserId))
                    {
                        mUser = user;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getEvents()
    {
        mFirebaseReference = FirebaseDatabase.getInstance().getReference("events");
        mFirebaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mEventList.clear();
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    for(String id : mUser.getmGoingEvents())
                    {
                        if(id.equals(event.getUid())) {
                            mEventList.add(event);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private Date getNearestEventDate()
    {
        Date nearestDate = new Date(Long.MIN_VALUE);
        Date currentDate = Calendar.getInstance().getTime();

        for(Event event : mEventList)
        {
            if(event.getDate().after(nearestDate) && event.getDate().before(currentDate))
            {
                nearestDate = event.getDate();
                mNearestEvent = event;
            }
        }
        return nearestDate;
    }
}

