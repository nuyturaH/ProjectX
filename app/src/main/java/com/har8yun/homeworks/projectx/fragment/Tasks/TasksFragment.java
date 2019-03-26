package com.har8yun.homeworks.projectx.fragment.Tasks;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.model.TaskViewModel;


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
        mBuildMusclesButton = view.findViewById(R.id.btn_ok_done);
    }


}
