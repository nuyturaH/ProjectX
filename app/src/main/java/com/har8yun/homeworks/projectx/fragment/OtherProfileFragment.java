package com.har8yun.homeworks.projectx.fragment;


import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.har8yun.homeworks.projectx.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtherProfileFragment extends Fragment {

    //views
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private FloatingActionButton mMessageButton;
    private TextView mPointsView;
    private TextView mStatusView;
    private TextView mHeightView;
    private TextView mWeightView;
    private TextView mFirstNameView;
    private TextView mLastNameView;
    private TextView mGenderView;
    private TextView mAgeView;


    public OtherProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        initViews(view);

        return view;
    }

    private void initViews(View view)
    {
        mToolbar = view.findViewById(R.id.toolbar_other_profile);
        mCollapsingToolbarLayout = view.findViewById(R.id.ctl_other_profile);
        mMessageButton = view.findViewById(R.id.fab_message_other_profile);
        mPointsView = view.findViewById(R.id.tv_points_other_profile);
        mStatusView = view.findViewById(R.id.tv_status_other_profile);
        mHeightView = view.findViewById(R.id.tv_height_other_profile);
        mWeightView = view.findViewById(R.id.tv_weight_other_profile);
        mFirstNameView = view.findViewById(R.id.tv_first_name_other_profile);
        mLastNameView = view.findViewById(R.id.tv_last_name_other_profile);
        mGenderView = view.findViewById(R.id.tv_gender_other_profile);
        mAgeView = view.findViewById(R.id.tv_age_other_profile);
    }

}
