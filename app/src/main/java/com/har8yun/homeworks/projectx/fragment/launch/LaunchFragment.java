package com.har8yun.homeworks.projectx.fragment.launch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.har8yun.homeworks.projectx.R;

import static com.har8yun.homeworks.projectx.util.NavigationHelper.onClickNavigate;


public class LaunchFragment extends Fragment {

    //views
    private Button mSignInButton;
    private Button mSignUpButton;
    private BottomNavigationView mBottomNavigationView;
    private Toolbar mToolbar;

    //constructor
    public LaunchFragment() {
    }

    //************************************* LIFECYCLE METHODS **************************************
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_launch, container, false);

        initViews(view);
        changeDesignStyle();

        onClickNavigate(mSignInButton, R.id.action_launch_fragment_to_sign_in_fragment);
        onClickNavigate(mSignUpButton, R.id.action_launch_fragment_to_sign_up_fragment);

        return view;
    }

    //**************************************** METHODS *********************************************
    private void changeDesignStyle() {
        mBottomNavigationView.setVisibility(View.INVISIBLE);
        mToolbar.setBackground(null);
    }

    private void initViews(View v) {
        mSignInButton = v.findViewById(R.id.btn_sign_in_launch);
        mSignUpButton = v.findViewById(R.id.btn_sign_up_launch);
        mBottomNavigationView = getActivity().findViewById(R.id.bottom_navigation_view_main);
        mToolbar = getActivity().findViewById(R.id.toolbar_main);
    }

}