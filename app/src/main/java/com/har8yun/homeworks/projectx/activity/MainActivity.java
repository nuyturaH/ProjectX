package com.har8yun.homeworks.projectx.activity;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.har8yun.homeworks.projectx.application.Application;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.notification.MyWorker;
import com.har8yun.homeworks.projectx.fragment.profile.MyProfileEditFragment;
import com.har8yun.homeworks.projectx.fragment.profile.MyProfileFragment;

import com.har8yun.homeworks.projectx.model.Event;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.model.UserViewModel;
import com.har8yun.homeworks.projectx.preferences.PreferencesHelper;
import com.har8yun.homeworks.projectx.preferences.SaveSharedPreferences;
import com.har8yun.homeworks.projectx.util.ImageLoadingUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import androidx.navigation.ui.NavigationUI;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;


public class MainActivity extends AppCompatActivity {

    //navigation
    private NavController mNavController;

    //shared preferences
    private SaveSharedPreferences sharedPreferences = new SaveSharedPreferences();
    private User mUser;

    //ViewModel
    private UserViewModel mUserViewModel;

    //views
    private BottomNavigationView mBottomNavigationView;
    private Fragment mNavHostFragment;

    //firebase;
    DatabaseReference mFirebaseReference;

    //objects
    private String fileUrl;
    private List<Event> mEventList = new ArrayList<>();
    private Event mNearestEvent;

    //************************************** LIFECYCLE METHODS ********************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String theme = sharedPreferences.getTheme(this);

        if(theme.equals(getResources().getString(R.string.red))) {
            setTheme(R.style.AppTheme);
        }else if(theme.equals(getResources().getString(R.string.blue)))
        {
            setTheme(R.style.AppThemeBlue);
        }else if(theme.equals(getResources().getString(R.string.green)))
        {
            setTheme(R.style.AppThemeGreen);
        }else if(theme.equals(getResources().getString(R.string.violet)))
        {
            setTheme(R.style.AppThemeViolet);
        }

        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        initViews();
        setNavigationComponent();



        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mUser = mUserViewModel.getUser().getValue();
        mUserViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable final User user) {
                mUser = user;
            }
        });

        if (!sharedPreferences.getLoggedStatus(this)) {
            NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.map_fragment, true).build();
            NavHostFragment.findNavController(mNavHostFragment).navigate(R.id.menu_item_log_out, null, navOptions);

        } else {
            mUser = sharedPreferences.getCurrentUser(this);
            mUserViewModel.setUser(mUser);
//            getEvents();
//            Date date = getNearestEventDate(); //this method gets nearest date of events,and gets event in mNearestEvent object

            Constraints constraints = new Constraints.Builder()
                    .setRequiresCharging(true)
                    .setRequiredNetworkType(NetworkType.UNMETERED)
                    .build();

            PeriodicWorkRequest myWorkRequest = new PeriodicWorkRequest.Builder(MyWorker.class,15, TimeUnit.MINUTES,10, TimeUnit.SECONDS)
                    .setConstraints(constraints)
                    .build();
            WorkManager.getInstance().enqueue(myWorkRequest);

            WorkManager.getInstance().getWorkInfoByIdLiveData(myWorkRequest.getId()).observe(this, new Observer<WorkInfo>() {
                @Override
                public void onChanged(@Nullable WorkInfo workInfo) {
                }
            });
        }




    }

    //************************************** OVERRIDE METHODS ********************************************
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavigationUI.onNavDestinationSelected(item, mNavController);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        if(sharedPreferences.getLoggedStatus(getApplicationContext())) {
            sharedPreferences.setCurrentUser(this, mUserViewModel.getUser().getValue());
        }
        super.onPause();
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == MyProfileEditFragment.CAMERA_REQUEST_CODE) {
                if (null != fileUrl) {
                    MyProfileEditFragment myProfileEditFragment =
                            (MyProfileEditFragment) mNavHostFragment.getChildFragmentManager().getFragments().get(0);
                    if (myProfileEditFragment != null && myProfileEditFragment.isVisible()) {
                        (myProfileEditFragment).onUploadFileCreated(fileUrl);
                    }
                }
            }
            else if(requestCode == MyProfileFragment.CAMERA_REQUEST_CODE)
            {
                if (null != fileUrl) {
                    MyProfileFragment myProfileFragment =
                            (MyProfileFragment) mNavHostFragment.getChildFragmentManager().getFragments().get(0);
                    if (myProfileFragment != null && myProfileFragment.isVisible()) {
                        (myProfileFragment).onUploadFileCreated(fileUrl);
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    //************************************** METHODS ********************************************
    private void setNavigationComponent() {
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(mBottomNavigationView, mNavController);
    }

    private void initViews() {
        mBottomNavigationView = findViewById(R.id.bottom_navigation_view_main);
        mNavHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
    }

    public void takePicture(int requestId) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(Application.get().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = ImageLoadingUtil.createImageFile();
            } catch (IOException e) {}
            if (null != photoFile) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri fileUri = FileProvider.getUriForFile(Application.get(),
                            Application.get().getApplicationContext().getPackageName() + ".fileprovider", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                } else {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                }
                String nativePackage = PreferencesHelper.getCameraPackage(Application.KEY_CAMERA_PACKAGE);
                if (!nativePackage.isEmpty()) {
                    takePictureIntent.setPackage(nativePackage);
                }
                fileUrl = photoFile.toURI().toString();
                startActivityForResult(takePictureIntent, requestId); //this goes to onActivityResult
            }
        }
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