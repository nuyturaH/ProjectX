package com.har8yun.homeworks.projectx.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.adapter.SkillItemRecyclerAdapter;
import com.har8yun.homeworks.projectx.model.Event;
import com.har8yun.homeworks.projectx.model.Skill;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.model.UserViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import androidx.navigation.fragment.NavHostFragment;


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


    //viewmodel
    private UserViewModel mUserViewModel;

    //user
    private User otherUser;


    public OtherProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_profile, container, false);

        mUserViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        mUserViewModel.getOtherUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable final User user) {
                otherUser = user;
            }
        });
        otherUser = mUserViewModel.getOtherUser().getValue();


        initViews(view);
        fillViews();

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

    private void fillViews() {

        Log.e("hhhh", "fillviews");
        if (otherUser.getUserInfo() != null) {
            if (otherUser.getUserInfo().getFirstName() != null) {
                mFirstNameView.setText(otherUser.getUserInfo().getFirstName());
            }
            if (otherUser.getUserInfo().getLastName() != null) {
                mLastNameView.setText(otherUser.getUserInfo().getLastName());
            }
            if (otherUser.getUserInfo().getGender() != null) {
                if (otherUser.getUserInfo().getGender() == 0) {
                    mGenderView.setText("Male");
                } else {
                    mGenderView.setText("Female");
                }
            }
            if (otherUser.getUserInfo().getBirthDate() != null) {
                Date date = otherUser.getUserInfo().getBirthDate();
                int b = date.getYear();
                int y = Calendar.getInstance().get(Calendar.YEAR);
                int m = Calendar.getInstance().get(Calendar.MONTH);
                int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                int ss = (y - b)%100;
                if (date.getMonth() < m && date.getDay() < d) {
                    mAgeView.setText(String.valueOf(ss));
                } else {
                    mAgeView.setText(String.valueOf(ss - 1));
                }

            }
            if (otherUser.getUserInfo().getWeight() != null) {
                mWeightView.setText(String.valueOf(otherUser.getUserInfo().getWeight()) + " kg");
            }
            if (otherUser.getUserInfo().getHeight() != null) {
                mHeightView.setText(String.valueOf((otherUser.getUserInfo().getHeight())) + " m");
            }
            if (otherUser.getUserInfo().getAvatar() != null) {
                //TODO set avatar
                setAvatar();
            }

            if (otherUser.getUsername() != null) {
                //username setting is in setNavigationComponent()
            }
            if (otherUser.getSkills() != null) {
//                initRecyclerView();
            }
        }

    }

    private void setAvatar()
    {

    }

    private void initRecyclerView() {
        SkillItemRecyclerAdapter skillItemRecyclerAdapter = new SkillItemRecyclerAdapter();

        Map<String,Integer> map = otherUser.getSkills();
        List<Skill> list = new ArrayList<>();


        for(String currentKey : map.keySet()){
            Skill skill = new Skill();
            skill.setSkillName(currentKey);
            skill.setSkillCount(map.get(currentKey));
            list.add(skill);
        }

        RecyclerView recyclerView = getView().findViewById(R.id.rv_skills_other_profile);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(skillItemRecyclerAdapter);
        skillItemRecyclerAdapter.addItems(list);
    }

}
