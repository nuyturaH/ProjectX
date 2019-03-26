package com.har8yun.homeworks.projectx.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.model.TaskViewModel;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;


public class DoneDialogFragment extends DialogFragment {

    //public static vars
    public static final String LOOSE_WEIGHT_DONE = "Loos Weight Done";

    //views
    private TextView mDescriptionView;
    private TextView mOkView;
    private ImageView mArnoldView;

    //tasks
    private TaskViewModel mTaskViewModel;

    //constructor
    public DoneDialogFragment() {
    }


    //************************************ LIFECYCLE METHODS ****************************************
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_done_dialog, container, false);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bg_rounded_corners_dialog);

        initViews(view);

        mTaskViewModel = ViewModelProviders.of(getActivity()).get(TaskViewModel.class);



        Animation zoomAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.zoom);
        mArnoldView.startAnimation(zoomAnimation);

        mOkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTaskViewModel.setTask(LOOSE_WEIGHT_DONE);
                getDialog().dismiss();
            }
        });



        return view;
    }


    //************************************** METHODS ********************************************
    private void initViews(View view) {
        mDescriptionView = view.findViewById(R.id.tv_description_done);
        mArnoldView = view.findViewById(R.id.iv_arnold_done);
        mOkView = view.findViewById(R.id.btn_ok_done);
    }

}
