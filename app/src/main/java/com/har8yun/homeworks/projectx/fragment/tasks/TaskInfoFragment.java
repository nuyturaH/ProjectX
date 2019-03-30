package com.har8yun.homeworks.projectx.fragment.tasks;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.model.TaskViewModel;

import androidx.navigation.fragment.NavHostFragment;

import static com.har8yun.homeworks.projectx.fragment.tasks.TasksFragment.BUILD_MUSCLES_INFO;
import static com.har8yun.homeworks.projectx.fragment.tasks.TasksFragment.LOOSE_WEIGHT_INFO;


public class TaskInfoFragment extends DialogFragment {

    //public static vars
    public static final String LOOSE_WEIGHT = "Loose Weight";
    public static final String BUILD_MUSCLES = "Build Muscles";

    //views
    private TextView mTitleView;
    private TextView mDescriptionView;
    private Button mOkButton;

    //navigation
    private Fragment mNavHostFragment;

    //view model
    private TaskViewModel mTaskViewModel;

    //constructor
    public TaskInfoFragment() {
    }

    //************************************ LIFECYCLE METHODS ****************************************
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
                            break;
                        case BUILD_MUSCLES_INFO:
                            mTitleView.setText("Build Muscles");
                            mDescriptionView.setText("More strength is more muscle. Do this task every day and you will have great body." );
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
                            NavHostFragment.findNavController(mNavHostFragment).navigate(R.id.action_map_fragment_to_build_muscles_fragment);
                            mTaskViewModel.setTask(null);
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
        mOkButton = view.findViewById(R.id.btn_ok_done);
        mNavHostFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
    }

}
