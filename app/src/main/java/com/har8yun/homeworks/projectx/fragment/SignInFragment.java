package com.har8yun.homeworks.projectx.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.activity.MainActivity;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.preferences.SaveSharedPreferences;

import java.util.List;


public class SignInFragment extends Fragment {

    private List<User> userList; // get this list from database

    private TextInputEditText username;
    private TextInputEditText password;
    private Button signInButton;

    private SaveSharedPreferences sharedPreferences = new SaveSharedPreferences();
    private boolean validUser;
//    private boolean validPassword;

    OnSignInFragmentActionListener mOnSignInFragmentActionListener;


    public SignInFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        username = view.findViewById(R.id.etv_username_sign_in);
        password = view.findViewById(R.id.etv_password_sign_in);
        signInButton = view.findViewById(R.id.btn_login);      // button ի անունը login եմ դրել,որ launch ի signIn ից տարբերվի


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
                if (mOnSignInFragmentActionListener != null && validUser) {
                    mOnSignInFragmentActionListener.onLoginButtonClicked();
                }
            }
        });

    }

    public void signInUser() {
        for (int i = 0; i < userList.size(); i++)
            if (username.getText().toString() != userList.get(i).getUsername() || password.getText().toString() != userList.get(i).getPassword()) {
                password.setError("Wrong Username or Password.");
                validUser = false;
            } else {
                validUser = true;
                sharedPreferences.setLoggedIn(getContext(),true);
            }

    }

    public void setOnSignInFragmentActionListener(OnSignInFragmentActionListener onFragmentActionListener) {
        mOnSignInFragmentActionListener = onFragmentActionListener;
    }


    public interface OnSignInFragmentActionListener {
        void onLoginButtonClicked();
    }

}
