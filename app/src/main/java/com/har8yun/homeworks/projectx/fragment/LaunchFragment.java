package com.har8yun.homeworks.projectx.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.har8yun.homeworks.projectx.R;


public class LaunchFragment extends Fragment {

    private Button signInButton;
    private Button signUpButton;

    LaunchFragment.OnLaunchFragmentActionListener mOnLaunchFragmentActionListener;


    public LaunchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_launch, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        signInButton = view.findViewById(R.id.btn_sign_in_launch);
        signUpButton = view.findViewById(R.id.btn_sign_up_launch);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnLaunchFragmentActionListener != null) {
                    mOnLaunchFragmentActionListener.onSignInButtonClicked();
                }
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnLaunchFragmentActionListener != null) {
                    mOnLaunchFragmentActionListener.onSignUpButtonClicked();
                }
            }
        });

    }

    public void setOnLaunchFragmentActionListener(LaunchFragment.OnLaunchFragmentActionListener onFragmentActionListener) {
        mOnLaunchFragmentActionListener = onFragmentActionListener;
    }


    public interface OnLaunchFragmentActionListener {
        void onSignInButtonClicked();

        void onSignUpButtonClicked();
    }

}
