package com.har8yun.homeworks.projectx.fragment.bottomNavigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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


public class TopChartsFragment extends Fragment {

    //navigation
    private NavController mNavController;

    //views
    private Toolbar mToolbarTopCharts;

    //constructor
    public TopChartsFragment() {
    }

    //************************************ LIFECYCLE METHODS ****************************************
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_charts, container, false);

        initViews(view);
        setTopChartsToolbar();
        setNavigationComponent();

        return view;
    }


    //************************************** METHODS ********************************************
    private void initViews(View view) {
        mToolbarTopCharts = view.findViewById(R.id.toolbar_top_charts);
    }

    private void setTopChartsToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbarTopCharts);
        setHasOptionsMenu(true);
    }

    private void setNavigationComponent() {
        mNavController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(setOf(R.id.top_charts_fragment)).build();
        NavigationUI.setupWithNavController(mToolbarTopCharts, mNavController, appBarConfiguration);
    }

}
