package com.har8yun.homeworks.projectx;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.har8yun.homeworks.projectx.fragment.BuildMusclesFragment;
import com.har8yun.homeworks.projectx.model.TaskViewModel;

import androidx.navigation.Navigation;

import static com.har8yun.homeworks.projectx.fragment.TasksFragment.BUILD_MUSCLES_INFO;
import static com.har8yun.homeworks.projectx.fragment.TasksFragment.DEVELOP_STAMINA_INFO;
import static com.har8yun.homeworks.projectx.fragment.TasksFragment.LOOSE_WEIGHT_INFO;
import static com.har8yun.homeworks.projectx.util.NavigationHelper.onClickNavigate;


public class TaskInfoFragment extends DialogFragment {

    //public static vars
    public static final String LOOSE_WEIGHT = "Loose Weight";
    public static final String DEVELOP_STAMINA = "Develop Stamina";
    public static final String BUILD_MUSCLES = "Build Muscles";

    //views
    private TextView mTitleView;
    private TextView mDescriptionView;
    private Button mOkButton;

    //view model
    private TaskViewModel mTaskViewModel;

    //constructor
    public TaskInfoFragment() {
    }

    //************************************ LIFECYCLE METHODS ****************************************
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_task_info, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_rounded_corners_dialog);
        initViews(view);


        mTaskViewModel = ViewModelProviders.of(getActivity()).get(TaskViewModel.class);
        mTaskViewModel.getTask().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s != null) {
                    switch (s) {
                        case LOOSE_WEIGHT_INFO:
                            mTitleView.setText("Loose Weight");
                            mDescriptionView.setText("Reach red marker and you will burn 110 calories! " +
                                    "Do this task every day and in a month you will loose 1kg!");
//                            mTaskViewModel.setTask(LOOSE_WEIGHT);
                            break;
                        case BUILD_MUSCLES_INFO:
                            mTitleView.setText("Build Muscles");
                            mDescriptionView.setText("Description imitation");
//                            mTaskViewModel.setTask(BUILD_MUSCLES);
                            //buildMuscles();
                            break;
                    }
                }
            }
        });


        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTaskViewModel != null) {
                    switch (mTaskViewModel.getTask().getValue()) {
                        case LOOSE_WEIGHT_INFO:
                            mTaskViewModel.setTask(LOOSE_WEIGHT);
                            break;
                        case BUILD_MUSCLES_INFO:
                            mTaskViewModel.setTask(BUILD_MUSCLES);
                            break;
                    }
                    getDialog().dismiss();
                }
            }
        });

        return view;
    }


    //************************************** METHODS ********************************************
    private void initViews(View view) {
        mTitleView = view.findViewById(R.id.tv_title_task_info);
        mDescriptionView = view.findViewById(R.id.tv_description_task_info);
        mOkButton = view.findViewById(R.id.btn_build_muscles_tasks);
    }

}
