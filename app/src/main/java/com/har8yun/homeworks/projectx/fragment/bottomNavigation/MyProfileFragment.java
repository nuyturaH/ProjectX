package com.har8yun.homeworks.projectx.fragment.bottomNavigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.preferences.SaveSharedPreferences;

import java.util.zip.Inflater;

import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;


public class MyProfileFragment extends Fragment {
    private SaveSharedPreferences sharedPreferences = new SaveSharedPreferences();

    //views
    private Button mLogOutButton;
    private Toolbar mToolbar2;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;


    //navigation
    private NavController mNavController;

    //constructor
    public MyProfileFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        initViews(view);
        mToolbar2 = view.findViewById(R.id.toolbar);
        mCollapsingToolbarLayout = view.findViewById(R.id.aaa);
        mNavController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        setHasOptionsMenu(true);

        activity.setSupportActionBar(mToolbar2);

        NavigationUI.setupWithNavController(mCollapsingToolbarLayout, mToolbar2, mNavController);



        //setSupportActionBar(mToolbar);
//        setSupportActionBar(mToolbar);
       //NavigationUI.setupActionBarWithNavController(this, mNavController);
        //NavigationUI.setupWithNavController(mBottomNavigationView, mNavController);



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


    private void initViews(View view) {
        mLogOutButton = view.findViewById(R.id.btn_log_out_my_profile);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_my_profile, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.e("hhhh","home");
                mNavController.popBackStack();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
