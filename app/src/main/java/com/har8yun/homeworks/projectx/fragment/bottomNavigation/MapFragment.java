package com.har8yun.homeworks.projectx.fragment.bottomNavigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.har8yun.homeworks.projectx.R;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static com.google.android.gms.common.util.CollectionUtils.setOf;


public class MapFragment extends Fragment {

    //views
    private BottomNavigationView mBottomNavigationView;
    private Toolbar mToolbarMap;

    //navigation
    private NavController mNavController;

    //constructor
    public MapFragment() {
    }


    //************************************ LIFECYCLE METHODS ****************************************
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        initViews(view);
        showBotNavBar();
        setNavigationComponent();
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(setOf(R.id.map_fragment, R.id.settings_fragment)).build();



        return view;
    }


    //************************************** METHODS ********************************************
    private void setNavigationComponent() {
        mNavController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(mToolbarMap, mNavController);
    }

    private void showBotNavBar() {
        mBottomNavigationView.setVisibility(View.VISIBLE);
    }

    private void initViews(View v) {
        mBottomNavigationView = getActivity().findViewById(R.id.bottom_navigation_view_main);
        mToolbarMap = v.findViewById(R.id.toolbar_map);
    }


}
