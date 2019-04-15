package com.har8yun.homeworks.projectx.fragment.tasks;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.model.MarkerOptions;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.mapAnim.MapAnimator;
import com.har8yun.homeworks.projectx.model.TaskViewModel;
import com.har8yun.homeworks.projectx.model.User;

import androidx.navigation.fragment.NavHostFragment;

import static com.har8yun.homeworks.projectx.fragment.tasks.DoneDialogFragment.LOOSE_WEIGHT_DONE;
import static com.har8yun.homeworks.projectx.fragment.tasks.TaskInfoFragment.LOOSE_WEIGHT;


public class TasksFragment extends DialogFragment {
    //public static vars
    public static final String LOOSE_WEIGHT_INFO = "Loose Weight Info";
    public static final String BUILD_MUSCLES_INFO = "Build Muscles Info";

    //views
    private Button mLooseWeightButton;
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
        mTaskViewModel.getDoneTask().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                switch (integer){
                    case 1:
                        mLooseWeightButton.setBackgroundColor(Color.parseColor("#B71C1C"));
                        mLooseWeightButton.setEnabled(false);
                        break;
                    case 2:
                        mBuildMusclesButton.setBackgroundColor(Color.parseColor("#B71C1C"));
                        mBuildMusclesButton.setEnabled(false);
                        break;
                    case 3:
                        mLooseWeightButton.setBackgroundColor(Color.parseColor("#B71C1C"));
                        mBuildMusclesButton.setBackgroundColor(Color.parseColor("#B71C1C"));
                        mLooseWeightButton.setEnabled(false);
                        mBuildMusclesButton.setEnabled(false);
                        break;
                }
            }
        });




        FragmentManager fm = getActivity().getSupportFragmentManager();
        TaskInfoFragment taskInfoFragment = new TaskInfoFragment();


        mLooseWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTaskViewModel.setTask(LOOSE_WEIGHT_INFO);
                getDialog().dismiss();
                taskInfoFragment.show(fm, null);
            }
        });


        mBuildMusclesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTaskViewModel.setTask(BUILD_MUSCLES_INFO);
                getDialog().dismiss();
                taskInfoFragment.show(fm, null);
            }
        });

        return view;
    }

    //************************************** METHODS ********************************************
    private void initViews(View view) {
        mLooseWeightButton = view.findViewById(R.id.btn_loose_weight_tasks);
        mBuildMusclesButton = view.findViewById(R.id.btn_build_muscles_tasks);
    }


}
