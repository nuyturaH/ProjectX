package com.har8yun.homeworks.projectx.fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.har8yun.homeworks.projectx.BuildMusclesDialogFragment;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.model.BuildMusclesViewModel;
import com.har8yun.homeworks.projectx.model.UserViewModel;
import com.har8yun.homeworks.projectx.util.BlurDrawable;
import com.jackandphantom.blurimage.BlurImage;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import pl.droidsonroids.gif.GifImageView;

import static com.har8yun.homeworks.projectx.GlideOptions.bitmapTransform;

public class BuildMusclesFragment extends Fragment {

    //static vars
    public static final String PUSH_UPS = "Push-ups";
    public static final String PULL_UPS = "Pull-ups";
    public static final String SQUATS = "Squats";
    public static final String SIT_UPS = "Sit-ups";

    //views
    private BottomNavigationView mBottomNavigationView;
    private Button mPushUpsButton;
    private Button mPullUpsButton;
    private Button mSquatsButton;
    private Button mSitUpsButton;
    private Toolbar mToolbarBuildMuscles;
    private GifImageView mGifImageView;
    private ConstraintLayout mConstraintLayout;
    private ImageView mBlurView;


    //navigation
    private NavController mNavController;

    //view model
    private BuildMusclesViewModel mBuildMusclesViewModel;

    //constructor
    public BuildMusclesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_build_muscles, container, false);

        initViews(view);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        BuildMusclesDialogFragment buildMusclesDialogFragment = new BuildMusclesDialogFragment();

        mBuildMusclesViewModel = ViewModelProviders.of(getActivity()).get(BuildMusclesViewModel.class);


        setNavigationComponent();
        hideBotNavBar();

        mPushUpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBuildMusclesViewModel.setBuildMuscles(PUSH_UPS);
                buildMusclesDialogFragment.show(fm, null);
            }
        });

        mPullUpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBuildMusclesViewModel.setBuildMuscles(PULL_UPS);
                buildMusclesDialogFragment.show(fm, null);
            }
        });

        mSquatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBuildMusclesViewModel.setBuildMuscles(SQUATS);
                buildMusclesDialogFragment.show(fm, null);
            }
        });

        mSitUpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBuildMusclesViewModel.setBuildMuscles(SIT_UPS);
                buildMusclesDialogFragment.show(fm, null);
            }
        });


        return view;
    }


    private void initViews(View view) {
        mToolbarBuildMuscles = view.findViewById(R.id.toolbar_build_muscles);
        mPushUpsButton = view.findViewById(R.id.btn_push_ups_build_muscles);
        mPullUpsButton = view.findViewById(R.id.btn_pull_ups_build_muscles);
        mSquatsButton = view.findViewById(R.id.btn_squats_build_muscles);
        mSitUpsButton = view.findViewById(R.id.btn_sit_ups_build_muscles);
        mBottomNavigationView = getActivity().findViewById(R.id.bottom_navigation_view_main);
        mGifImageView = view.findViewById(R.id.gifImageView3);
        mConstraintLayout = view.findViewById(R.id.push_ups_build_muscles);
        mBlurView = view.findViewById(R.id.iv_transparent_build_muscles3);
    }


    private void setNavigationComponent() {
        mNavController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(mToolbarBuildMuscles, mNavController);
    }

    private void hideBotNavBar() {
        mBottomNavigationView.setVisibility(View.GONE);
    }

}
