package com.har8yun.homeworks.projectx.adapter;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.model.Event;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.model.UserViewModel;
import com.har8yun.homeworks.projectx.util.NotificationHelper;

public class WidgetAdapter implements RemoteViewsFactory {

    ArrayList<Event> data;
    Context context;
    SimpleDateFormat sdf;
    int widgetID;


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

    public WidgetAdapter(Context ctx, Intent intent) {
        context = ctx;
        sdf = new SimpleDateFormat("HH:mm:ss");
        widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        data = new ArrayList<Event>();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rView = new RemoteViews(context.getPackageName(), R.layout.item_event);
        rView.setTextViewText(R.id.tv_event_title_item, data.get(position).getTitle());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, hh:mm");
        rView.setTextViewText(R.id.tv_event_date_item, simpleDateFormat.format(data.get(position).getDate()));
        return rView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
        if (mFirebaseUser != null) {
            currentUserId = mFirebaseUser.getUid();
            getUser();
            getEvents();
            data.clear();
            data.addAll(mEventList);
        }
        Log.e("hhhh", "onDataSetChanged: " + mEventList.size());
    }

    @Override
    public void onDestroy() {

    }

    private void getUser() {
        mFirebaseReference = FirebaseDatabase.getInstance().getReference("users");
        mFirebaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (user.getId().equals(currentUserId)) {
                        mUser = user;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    private void getEvents() {
        mFirebaseReference = FirebaseDatabase.getInstance().getReference("events");
        mFirebaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mEventList.clear();
                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    for (String id : mUser.getmGoingEvents()) {
                        Log.e("widget list", "getGoingEvents() " + mUser);
                        if (id.equals(event.getUid())) {
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
}
