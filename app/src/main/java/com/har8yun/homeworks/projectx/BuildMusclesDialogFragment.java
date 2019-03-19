package com.har8yun.homeworks.projectx;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.har8yun.homeworks.projectx.model.BuildMusclesViewModel;
import com.har8yun.homeworks.projectx.model.User;

import static com.har8yun.homeworks.projectx.fragment.BuildMusclesFragment.PULL_UPS;
import static com.har8yun.homeworks.projectx.fragment.BuildMusclesFragment.PUSH_UPS;
import static com.har8yun.homeworks.projectx.fragment.BuildMusclesFragment.SIT_UPS;
import static com.har8yun.homeworks.projectx.fragment.BuildMusclesFragment.SQUATS;


public class BuildMusclesDialogFragment extends DialogFragment {

    //views
    private TextView mTitleView;
    private TextView mDescriptionView;
    private Button mDoneButton1;
    private TextView mTimerView;

    public int counter;

    //view model
    private BuildMusclesViewModel mBuildMusclesViewModel;

    //constructor
    public BuildMusclesDialogFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_build_muscles_dialog, container, false);

        mTitleView = view.findViewById(R.id.tv_title_muscles_dialog);
        mDescriptionView = view.findViewById(R.id.tv_description_muscles_dialog);
        mDoneButton1 = view.findViewById(R.id.btn_done1_muscles_dialog);
        mTimerView = view.findViewById(R.id.tv_timer_muscles_dialog);


        mBuildMusclesViewModel = ViewModelProviders.of(getActivity()).get(BuildMusclesViewModel.class);
        mBuildMusclesViewModel.getBuildMuscles().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                switch (s) {
                    case PUSH_UPS:
                        mTitleView.setText("Push-Ups");
                        mDescriptionView.setText("Push-Ups description");
                        break;
                    case PULL_UPS:
                        mTitleView.setText("Pull-Ups");
                        mDescriptionView.setText("Pull-Ups description");
                        break;
                    case SQUATS:
                        mTitleView.setText("Squats");
                        mDescriptionView.setText("Squats description");
                        break;
                    case SIT_UPS:
                        mTitleView.setText("Sit-Ups");
                        mDescriptionView.setText("Sit-Ups description");
                        break;
                }
            }
        });


        mDoneButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CountDownTimer(180000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        int allSeconds = 180 - counter;
                        int currentMinute = allSeconds / 60;
                        int currentSeconds = allSeconds % 60;
                        if (currentSeconds<10){
                            mTimerView.setText(String.valueOf(currentMinute)+":0"+String.valueOf(currentSeconds));
                        }else{
                            mTimerView.setText(String.valueOf(currentMinute)+":"+String.valueOf(currentSeconds));
                        }
                        counter++;
                    }

                    public void onFinish() {
                        mTimerView.setText("Finished");
                    }
                }.start();
            }
        });


        return view;
    }

}
