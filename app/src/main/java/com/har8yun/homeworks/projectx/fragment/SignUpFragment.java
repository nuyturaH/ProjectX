package com.har8yun.homeworks.projectx.fragment;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.model.User;

import java.util.ArrayList;
import java.util.List;


public class SignUpFragment extends Fragment {

    private EditText usernameView;
    private EditText emailView;
    private EditText passwordView;
    private EditText confirmPasswordView;
    private Button signUpButton;

    private List<User> userList = new ArrayList<>();

    private OnSignUpFragmentInteractionListener mListener;

    public SignUpFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        init(view);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User mUser = new User();
                mUser.setUsername(usernameView.getText().toString());
                mUser.setEmail(emailView.getText().toString());
                mUser.setPassword(usernameView.getText().toString().toCharArray());
                mUser.setUsername(usernameView.getText().toString());
                //...

                userList.add(mUser);
            }
        });


        return view;
    }




    private void init(View v){
        usernameView = v.findViewById(R.id.etv_username_sign_up);
        emailView = v.findViewById(R.id.etv_email_sign_up);
        passwordView = v.findViewById(R.id.etv_password_sign_up);
        confirmPasswordView = v.findViewById(R.id.etv_confirm_password_sign_up);
        signUpButton = v.findViewById(R.id.btn_register_sign_up);
    }

    public interface OnSignUpFragmentInteractionListener {
        void onFragmentInteraction();
    }

}
