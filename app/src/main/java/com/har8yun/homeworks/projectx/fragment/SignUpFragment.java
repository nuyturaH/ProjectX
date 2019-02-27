package com.har8yun.homeworks.projectx.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.activity.MainActivity;
import com.har8yun.homeworks.projectx.model.User;

import java.util.ArrayList;
import java.util.List;


public class SignUpFragment extends Fragment {

    //views
    private EditText usernameView;
    private EditText emailView;
    private EditText passwordView;
    private EditText confirmPasswordView;
    private Button signUpButton;

    //error messages
    private String[] usernameErrorMessages = new String[5];
    private String[] emailErrorMessages = new String[2];
    private String[] passwordErrorMessages = new String[2];
    private String[] confirmPasswordErrorMessages = new String[2];

    //collections
    private List<User> userList = new ArrayList<>(); //in future it will be users list of database

    private OnSignUpFragmentInteractionListener mListener;

    private FirebaseAuth firebaseAuth;

    public SignUpFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        initViews(view);
        initErrorMessages();

        setUsernameErrors();
        setEmailError();
        setPasswordError();
        setConfirmPasswordError();

        firebaseAuth = FirebaseAuth.getInstance();

        addUserToList();

        return view;
    }


    //************************************** METHODS ********************************************
    private void addUserToList() {
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (usernameView.getText().length() == 0) {
                    usernameView.setError(usernameErrorMessages[0]);
                } else if (emailView.getText().length() == 0) {
                    emailView.setError(emailErrorMessages[0]);
                } else if (passwordView.getText().length() == 0) {
                    passwordView.setError(passwordErrorMessages[0]);
                } else if (confirmPasswordView.getText().length() == 0) {
                    confirmPasswordView.setError(confirmPasswordErrorMessages[0]);
                } else if (usernameView.getError() == null && emailView.getError() == null
                        && passwordView.getError() == null && confirmPasswordView.getError() == null) {
                    if (!confirmPasswordView.getText().toString().equals(passwordView.getText().toString())
                            && confirmPasswordView.getText().length() != 0) {
                        confirmPasswordView.setError(confirmPasswordErrorMessages[1]);
                    } else {
                        User mUser = new User();
                        mUser.setUsername(usernameView.getText().toString());
                        mUser.setEmail(emailView.getText().toString());
                        mUser.setPassword(passwordView.getText().toString());
                        Toast.makeText(getContext(),
                                "User Added",
                                Toast.LENGTH_SHORT).show();


                        userList.add(mUser);
                        registerUserToFirebase(mUser); //add user to firebase
                    }

                }
            }
        });
    }

    private void registerUserToFirebase(User user) {

        final User mUser = user;

        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword().toString())
                .addOnCompleteListener(this.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //checking if success
                        if (task.isSuccessful()) {
                                //registered
                            FirebaseDatabase.getInstance()
                                    .getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(mUser)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(getContext(), "User Added To Firebase",Toast.LENGTH_SHORT).show();
                                        Log.d("SIgnUp","added to firebase");

                                    } else {
                                        Toast.makeText(getContext(), "User Not Added To Firebase",Toast.LENGTH_SHORT).show();
                                        Log.d("SIgnUp","not added to firebase");
                                    }
                                }
                            });

                        } else {
                                //not registered
                        }
                    }
                });

    }

    private void setConfirmPasswordError() {
        confirmPasswordView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!confirmPasswordView.getText().toString().equals(passwordView.getText().toString())
                            && confirmPasswordView.getText().length() != 0) {
                        confirmPasswordView.setError(confirmPasswordErrorMessages[1]);
                    }
                }
            }
        });
    }

    private void setPasswordError() {
        passwordView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (passwordView.getText().length() < 6 && passwordView.getText().length() != 0) {
                        passwordView.setError(passwordErrorMessages[1]);
                    }
                }
            }
        });
    }

    private void setEmailError() {
        emailView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
//                    if (emailView.getText().length()==0){
//                        emailView.setError(emailErrorMessages[0]);
//                    }else
                    if (!isValidEmail(emailView.getText().toString().trim()) && emailView.getText().length() != 0) {
                        emailView.setError(emailErrorMessages[1]);
                    }
                }
            }
        });
    }


    private void setUsernameErrors() {
        usernameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 20) {
                    usernameView.setError(usernameErrorMessages[4]);
                    usernameView.setTextColor(Color.RED);
                } else {
                    usernameView.setTextColor(Color.BLACK);
                }
                if (!s.toString().matches("[a-zA-Z0-9]*")) {
                    usernameView.setError(usernameErrorMessages[1]);
                }
                for (User u : userList) {
                    if (u.getUsername().equals(s.toString())) {
                        usernameView.setError(usernameErrorMessages[2]);
                        usernameView.setTextColor(Color.RED);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        usernameView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
//                    if (usernameView.getText().length()==0){
//                        usernameView.setError(usernameErrorMessages[0]);
//                    }else
                    if (usernameView.getText().length() < 6 && usernameView.getText().length() != 0) {
                        usernameView.setError(usernameErrorMessages[3]);
                    }
                }
            }
        });
    }


    private boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void initErrorMessages() {
        usernameErrorMessages[0] = getResources().getString(R.string.username_enter);
        usernameErrorMessages[1] = getResources().getString(R.string.username_content);
        usernameErrorMessages[2] = getResources().getString(R.string.username_existence);
        usernameErrorMessages[3] = getResources().getString(R.string.username_length_short);
        usernameErrorMessages[4] = getResources().getString(R.string.username_length_long);

        emailErrorMessages[0] = getResources().getString(R.string.email_enter);
        emailErrorMessages[1] = getResources().getString(R.string.email_wrong);

        passwordErrorMessages[0] = getResources().getString(R.string.password_enter);
        passwordErrorMessages[1] = getResources().getString(R.string.password_length);

        confirmPasswordErrorMessages[0] = getResources().getString(R.string.confirm_password_enter);
        confirmPasswordErrorMessages[1] = getResources().getString(R.string.confirm_password_matching);
    }

    private void initViews(View v) {
        usernameView = v.findViewById(R.id.etv_username_sign_up);
        emailView = v.findViewById(R.id.etv_email_sign_up);
        passwordView = v.findViewById(R.id.etv_password_sign_up);
        confirmPasswordView = v.findViewById(R.id.etv_confirm_password_sign_up);
        signUpButton = v.findViewById(R.id.btn_register_sign_up);
    }


    //************************************* INTERFACES ******************************************
    public interface OnSignUpFragmentInteractionListener {
        void onFragmentInteraction();
    }

}
