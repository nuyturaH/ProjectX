package com.har8yun.homeworks.projectx.fragment.bottomNavigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.har8yun.homeworks.projectx.R;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static com.google.android.gms.common.util.CollectionUtils.setOf;


public class SettingsFragment extends Fragment {

    //navigation
    private NavController mNavController;

    //views
    private Toolbar mToolbarSettings;

    //constructor
    public SettingsFragment() {
    }


    //************************************ LIFECYCLE METHODS ****************************************
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initViews(view);
        setSettingsToolbar();

        setNavigationComponent();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_settings, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    //************************************** METHODS ********************************************
    private void initViews(View view) {
        mToolbarSettings = view.findViewById(R.id.toolbar_settings);
    }

    private void setNavigationComponent() {
        mNavController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(setOf(R.id.settings_fragment)).build();
        NavigationUI.setupWithNavController(mToolbarSettings, mNavController, appBarConfiguration);
    }

    private void setSettingsToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbarSettings);
        setHasOptionsMenu(true);
    }
}
