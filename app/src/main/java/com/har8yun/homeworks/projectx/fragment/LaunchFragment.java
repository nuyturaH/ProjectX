package com.har8yun.homeworks.projectx.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.har8yun.homeworks.projectx.R;

import androidx.navigation.Navigation;


public class LaunchFragment extends Fragment {
    //views
    private Button mSignInButton;
    private Button mSignUpButton;

    LaunchFragment.OnLaunchFragmentActionListener mOnLaunchFragmentActionListener;


    public LaunchFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_launch, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mSignInButton = view.findViewById(R.id.btn_sign_in_launch);
        mSignUpButton = view.findViewById(R.id.btn_sign_up_launch);


        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_launchFragment_to_signInFragment);

//                if (mOnLaunchFragmentActionListener != null) {
//                    mOnLaunchFragmentActionListener.onSignInButtonClicked();
//                }
            }
        });
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_launchFragment_to_signUpFragment);

//                if (mOnLaunchFragmentActionListener != null) {
//                    mOnLaunchFragmentActionListener.onSignUpButtonClicked();
//                    Navigation.findNavController(v).navigate(R.id.fragment_sign_up);
//                }
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
