package com.har8yun.homeworks.projectx.fragment.tasks;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.model.BuildMusclesViewModel;
import com.har8yun.homeworks.projectx.model.TaskViewModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import static com.har8yun.homeworks.projectx.fragment.tasks.BuildMusclesFragment.PULL_UPS;
import static com.har8yun.homeworks.projectx.fragment.tasks.BuildMusclesFragment.PUSH_UPS;
import static com.har8yun.homeworks.projectx.fragment.tasks.BuildMusclesFragment.SIT_UPS;
import static com.har8yun.homeworks.projectx.fragment.tasks.BuildMusclesFragment.SQUATS;


public class BuildMusclesDialogFragment extends DialogFragment {

    //views
    private TextView mTitleView;
    private TextView mDescriptionView;
    private TextView mTimerView;
    private Button[] mDoneButtons = new Button[4];
    private ImageView[] mImageViews = new ImageView[4];
    private ConstraintLayout mTimerLayout;
    private Button mStopButton;



    //timer
    private CountDownTimer mCountDownTimer;

    //view model
    private BuildMusclesViewModel mBuildMusclesViewModel;
    private TaskViewModel mTaskViewModel;

    //constructor
    public BuildMusclesDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_build_muscles_dialog, container, false);
        initViews(view);
        mTaskViewModel = ViewModelProviders.of(getActivity()).get(TaskViewModel.class);
        mBuildMusclesViewModel = ViewModelProviders.of(getActivity()).get(BuildMusclesViewModel.class);
        mBuildMusclesViewModel.getBuildMuscles().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                switch (s) {
                    case PUSH_UPS:
                        mTitleView.setText("Push-Ups");
                        mDescriptionView.setText("Lay prone on the ground with arms supporting your body. \n" +
                                "Keep your body straight while raising and lowering your body with your arms.");
                        break;
                    case PULL_UPS:
                        mTitleView.setText("Pull-Ups");
                        mDescriptionView.setText("Grab the pullup bar with your palms down." +
                                "Hang to the pullup-bar with straight arms and your legs off the floor." +
                                "Pull yourself up by pulling your elbows down to the floor." +
                                "Go all the way up until your chin passes the be bar." +
                                "Lower yourself until your arms are straight.");
                        break;
                    case SQUATS:
                        mTitleView.setText("Squats");
                        mDescriptionView.setText("Stand with your feet shoulder width apart and your arms stretched forward, " +
                                "then lower your body until your thighs are parallel with the floor." +
                                "Your knees should be extended in the same direction as your toes. Return to the start position and do the next rep." );
                        break;
                    case SIT_UPS:
                        mTitleView.setText("Sit-Ups");
                        mDescriptionView.setText("Lie on your back with your hands behind your ears." +
                                "Then lift your upper body off the floor and slowly up to the sitting position. Don't tug your neck when you get up." +
                                "Slowly go back to the start position and repeat the exercise." );
                        break;
                }
            }
        });

        for (int i = 0; i < mDoneButtons.length - 1; i++) {
            doneButtonAction(mDoneButtons[i], mDoneButtons[i + 1]);
        }

        mDoneButtons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDoneButtons[3].setVisibility(View.INVISIBLE);
                mImageViews[3].setVisibility(View.VISIBLE);
                if (mBuildMusclesViewModel.getUnlockLevel().getValue() < 3) {
                    mBuildMusclesViewModel.setUnlockLevel(mBuildMusclesViewModel.getUnlockLevel().getValue() + 1);
                } else {
                    getDialog().dismiss();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    DoneDialogFragment tasksFragment = new DoneDialogFragment();
                    tasksFragment.show(fm, null);
                    Button mSitUpsButton = getActivity().findViewById(R.id.btn_sit_ups_build_muscles);
                    mSitUpsButton.setEnabled(false);
                    mTaskViewModel.setDoneTask(1);
                }
            }
        });
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNextSet();
            }
        });

        return view;
    }

    private void initViews(View view) {
        mTitleView = view.findViewById(R.id.tv_title_muscles_dialog);
        mDescriptionView = view.findViewById(R.id.tv_description_muscles_dialog);
        mTimerView = view.findViewById(R.id.tv_timer_muscles_dialog);

        mDoneButtons[0] = view.findViewById(R.id.btn_done1_muscles_dialog);
        mDoneButtons[1] = view.findViewById(R.id.btn_done2_muscles_dialog);
        mDoneButtons[2] = view.findViewById(R.id.btn_done3_muscles_dialog);
        mDoneButtons[3] = view.findViewById(R.id.btn_done4_muscles_dialog);

        mImageViews[0] = view.findViewById(R.id.iv_tick1_muscles_dialog);
        mImageViews[1] = view.findViewById(R.id.iv_tick2_muscles_dialog);
        mImageViews[2] = view.findViewById(R.id.iv_tick3_muscles_dialog);
        mImageViews[3] = view.findViewById(R.id.iv_tick4_muscles_dialog);

        mStopButton = view.findViewById(R.id.btn_stop_timer_muscles_dialog);

        mTimerLayout = view.findViewById(R.id.layout_timer_muscles_dialog);
    }

    private void doneButtonAction(Button b1, Button b2) {
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimerLayout.setVisibility(View.VISIBLE);
                b1.setVisibility(View.INVISIBLE);
                mCountDownTimer = new CountDownTimer(90000, 1000) {
                    int counter = 90;
                    public void onTick(long millisUntilFinished) {
                        int currentMinute = counter / 60;
                        int currentSeconds = counter % 60;
                        NumberFormat f = new DecimalFormat("00");
                        mTimerView.setText(String.format("%s:%s", f.format(currentMinute), f.format(currentSeconds)));
                        counter--;
                    }
                    public void onFinish() {
                        this.cancel();
                        mTimerLayout.setVisibility(View.GONE);
                        mTimerView.setText("Finished");
                        openNextSet();
                    }
                }.start();
            }
        });
    }

    private void openNextSet() {
        for (int i = 0; i < mDoneButtons.length; i++) {
            if (!mDoneButtons[i].isEnabled()) {
                mImageViews[i].setVisibility(View.VISIBLE);
                mDoneButtons[i].setTextColor(getResources().getColor(R.color.green));
                mDoneButtons[i].setEnabled(true);
                mCountDownTimer.cancel();
                mTimerLayout.setVisibility(View.GONE);
                break;
            }
        }
    }


}
