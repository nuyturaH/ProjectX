package com.har8yun.homeworks.projectx.activity;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.har8yun.homeworks.projectx.Application;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.fragment.myProfile.MyProfileEditFragment;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.model.UserViewModel;
import com.har8yun.homeworks.projectx.preferences.PreferencesHelper;
import com.har8yun.homeworks.projectx.preferences.SaveSharedPreferences;
import com.har8yun.homeworks.projectx.util.ImageLoadingUtil;

import java.io.File;
import java.io.IOException;

import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import androidx.navigation.ui.NavigationUI;


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

    //objects
    private String fileUrl;

    //************************************** LIFECYCLE METHODS ********************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initViews();
        setNavigationComponent();



        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mUser = mUserViewModel.getUser().getValue();
        mUserViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable final User user) {
//                Log.e("hhhh", "ViewModel in My profile Edit " + user.toString());
                mUser = user;
            }
        });

        if (!sharedPreferences.getLoggedStatus(this)) {
            NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.map_fragment, true).build();
            NavHostFragment.findNavController(mNavHostFragment).navigate(R.id.menu_item_log_out, null, navOptions);

        } else {
            mUser = sharedPreferences.getCurrentUser(this);
            mUserViewModel.setUser(mUser);
//            Log.d("MainActivity", mUser.toString());
        }


//        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
//        Log.d("Main",u.getUid());
    }

    //************************************** OVERRIDE METHODS ********************************************
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavigationUI.onNavDestinationSelected(item, mNavController);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        sharedPreferences.setCurrentUser(this, mUserViewModel.getUser().getValue());
        Log.d("MainActivity setUser", mUserViewModel.getUser().getValue().toString());
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
        }
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


}