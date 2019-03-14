package com.har8yun.homeworks.projectx.fragment;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.model.TaskViewModel;
import com.har8yun.homeworks.projectx.model.UserViewModel;


public class TasksFragment extends DialogFragment {
    //public static vars
    public static final String LOOSE_WEIGHT = "Loose Weight";
    public static final String DEVELOP_STAMINA = "Develop Stamina";
    public static final String BUILD_MUSCLES = "Build Muscles";

    //views
    private Button mLooseWeightButton;
    private Button mDevelopStaminaButton;
    private Button mBuildMusclesButton;

    //viewModel
    private TaskViewModel mTaskViewModel;

    //constructor
    public TasksFragment() {
    }

    //************************************ LIFECYCLE METHODS ****************************************
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_rounded_corners_dialog);

        initViews(view);
        mTaskViewModel = ViewModelProviders.of(getActivity()).get(TaskViewModel.class);


        mLooseWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTaskViewModel.setTask(LOOSE_WEIGHT);
                getDialog().dismiss();
            }
        });

        mDevelopStaminaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTaskViewModel.setTask(DEVELOP_STAMINA);
                getDialog().dismiss();
            }
        });

        mBuildMusclesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTaskViewModel.setTask(BUILD_MUSCLES);
                getDialog().dismiss();
            }
        });

        return view;
    }

    //************************************** METHODS ********************************************
    private void initViews(View view) {
        mLooseWeightButton = view.findViewById(R.id.btn_loose_weight_tasks);
        mDevelopStaminaButton = view.findViewById(R.id.btn_develop_stamina_tasks);
        mBuildMusclesButton = view.findViewById(R.id.btn_build_muscles_tasks);
    }


}
