package com.har8yun.homeworks.projectx.activity;


import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.preferences.SaveSharedPreferences;

import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private NavController mNavController;
    private SaveSharedPreferences sharedPreferences = new SaveSharedPreferences();

    //views
    private BottomNavigationView mBottomNavigationView;
    private Toolbar mToolbar;
    private Fragment mNavHostFragment;

    //**************************************LIFECYCLE METHODS ********************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setNavigationComponent();

        if (!sharedPreferences.getLoggedStatus(this)){
            NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.map_fragment, true).build();
            NavHostFragment.findNavController(mNavHostFragment).navigate(R.id.fragment_launch,null,navOptions);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        NavigationUI.onNavDestinationSelected(item, mNavController);
        NavigationUI.setupActionBarWithNavController(this, mNavController);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavHostFragment.findNavController(mNavHostFragment).navigateUp();
        return super.onSupportNavigateUp();
    }

    //************************************** METHODS ********************************************
    private void setNavigationComponent() {
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        setSupportActionBar(mToolbar);
        NavigationUI.setupActionBarWithNavController(this, mNavController);
        NavigationUI.setupWithNavController(mBottomNavigationView, mNavController);
    }

    private void initViews() {
        mToolbar = findViewById(R.id.toolbar_main);
        mBottomNavigationView = findViewById(R.id.bottom_navigation_view_main);
        mNavHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
    }

}