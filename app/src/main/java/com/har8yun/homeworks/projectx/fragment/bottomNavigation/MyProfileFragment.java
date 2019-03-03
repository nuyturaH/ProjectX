package com.har8yun.homeworks.projectx.fragment.bottomNavigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.preferences.SaveSharedPreferences;

import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;


public class MyProfileFragment extends Fragment {

    //shared preferences
    private SaveSharedPreferences sharedPreferences = new SaveSharedPreferences();

    //navigation
    private NavController mNavController;

    //views
    private Button mLogOutButton;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;


    //constructor
    public MyProfileFragment() {
    }


    //************************************** LIFECYCLE METHODS ********************************************
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        initViews(view);
        setMyProfileToolbar();
        setNavigationComponent();

        //View listeners
        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.map_fragment, true).build();
                Navigation.findNavController(v).navigate(R.id.action_my_profile_fragment_to_fragment_launch,null, navOptions);
                sharedPreferences.setLoggedIn(getActivity(),false);
            }
        });
        return view;
    }


    //************************************ OVERRIDE METHODS ****************************************
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_my_profile, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_log_out:
                logOut();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //************************************** METHODS ********************************************
    private void setNavigationComponent() {
        mNavController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(mCollapsingToolbarLayout, mToolbar, mNavController);
    }

    private void setMyProfileToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        setHasOptionsMenu(true);
    }

    private void initViews(View view) {
        mLogOutButton = view.findViewById(R.id.btn_log_out_my_profile);
        mToolbar = view.findViewById(R.id.toolbar);
        mCollapsingToolbarLayout = view.findViewById(R.id.aaa);
    }

    private void logOut() {
        NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.map_fragment, true).build();
        Fragment mNavHostFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavHostFragment.findNavController(mNavHostFragment).navigate(R.id.menu_item_log_out, null, navOptions);
        sharedPreferences.setLoggedIn(getActivity(),false);

        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getContext(),"Signed Out",Toast.LENGTH_SHORT).show();

    }

}