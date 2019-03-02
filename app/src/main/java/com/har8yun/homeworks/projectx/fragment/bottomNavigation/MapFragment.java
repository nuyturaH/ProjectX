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
import androidx.navigation.ui.NavigationUI;


public class MapFragment extends Fragment {

    //views
    private BottomNavigationView mBottomNavigationView;
    private Toolbar mToolbar;
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

        //changeDesignStyle();

        mNavController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        NavigationUI.setupWithNavController(mToolbarMap, mNavController);

        return view;
    }

    //************************************** METHODS ********************************************
    private void changeDesignStyle() {
        mBottomNavigationView.setVisibility(View.VISIBLE);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    private void initViews(View v) {
        mBottomNavigationView = getActivity().findViewById(R.id.bottom_navigation_view_main);
        mToolbar = getActivity().findViewById(R.id.toolbar_main);
        mToolbarMap = v.findViewById(R.id.toolbar_map);
    }


}
