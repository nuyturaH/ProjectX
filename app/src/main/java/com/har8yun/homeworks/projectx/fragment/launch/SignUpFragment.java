package com.har8yun.homeworks.projectx.fragment.launch;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.preferences.SaveSharedPreferences;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import static com.har8yun.homeworks.projectx.fragment.launch.SignInFragment.DATABASE_PATH_NAME;
import static com.har8yun.homeworks.projectx.util.NavigationHelper.onClickNavigate;


public class SignUpFragment extends Fragment {

    //shared preferences
    private SaveSharedPreferences sharedPreferences = new SaveSharedPreferences();

    //views
    private EditText mUsernameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mConfirmPasswordView;
    private Button mSignUpButton;
    private TextView mSuggestionSignUp;

    //Dialog
    ProgressDialog mProgressDialog;

    //collections
    private List<User> mUserList = new ArrayList<>();

    //Firebase
    private FirebaseAuth mFirebaseAuth;

    //constructor
    public SignUpFragment() {

    }

    //************************************ LIFECYCLE METHODS ****************************************
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        mProgressDialog = new ProgressDialog(this.getActivity());

        initViews(view);
        setUsernameErrors();
        setEmailError();
        setPasswordError();
        setConfirmPasswordError();
        addUserToList();

        mFirebaseAuth = FirebaseAuth.getInstance();

        onClickNavigate(mSuggestionSignUp, R.id.action_fragment_sign_up_to_fragment_sign_in);

        return view;
    }


    //************************************** METHODS ********************************************
    private void addUserToList() {
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUsernameView.getText().length() == 0){
                    mUsernameView.setError(getResources().getString(R.string.username_enter));
                }else if (mEmailView.getText().length() == 0){
                        mEmailView.setError(getResources().getString(R.string.email_enter));
                }else if (mPasswordView.getText().length() == 0){
                        mPasswordView.setError(getResources().getString(R.string.password_enter));
                }else if (mConfirmPasswordView.getText().length() == 0){
                        mConfirmPasswordView.setError(getResources().getString(R.string.confirm_password_enter));
                }else if (mUsernameView.getError() == null && mEmailView.getError() == null
                        && mPasswordView.getError() == null && mConfirmPasswordView.getError()==null){
                    if (!mConfirmPasswordView.getText().toString().equals(mPasswordView.getText().toString())
                            && mConfirmPasswordView.getText().length() != 0){
                        mConfirmPasswordView.setError(getResources().getString(R.string.confirm_password_matching));
                    }else{
                        User mUser = new User();
                        mUser.setUsername(mUsernameView.getText().toString());
                        mUser.setEmail(mEmailView.getText().toString());
                        mUserList.add(mUser);

                        mProgressDialog.setMessage("Creating account ...");
                        mProgressDialog.show();

                        registerUserToFirebase(mUser); //add user to firebase
                        openAccount(v);
                    }

                }
            }
        });
    }

    private void setConfirmPasswordError() {
        mConfirmPasswordView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if (!mConfirmPasswordView.getText().toString().equals(mPasswordView.getText().toString())
                            && mConfirmPasswordView.getText().length()!=0){
                        mConfirmPasswordView.setError(getResources().getString(R.string.confirm_password_enter));
                    }
                }
            }
        });
    }

    private void setPasswordError() {
        mPasswordView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if (mPasswordView.getText().length()<6 && mPasswordView.getText().length()!=0){
                        mPasswordView.setError(getResources().getString(R.string.password_length));
                    }
                }
            }
        });
    }

    private void setEmailError() {
        mEmailView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                        if (!isValidEmail(mEmailView.getText().toString().trim()) && mEmailView.getText().length()!=0) {
                            mEmailView.setError(getResources().getString(R.string.email_wrong));
                    }
                }
            }
        });
    }


    private void setUsernameErrors() {
        mUsernameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>20){
                   mUsernameView.setError(getResources().getString(R.string.username_length_long));
                   mUsernameView.setTextColor(Color.RED);
                }else {
                    mUsernameView.setTextColor(Color.BLACK);
                }
                if (!s.toString().matches("[a-zA-Z0-9]*")){
                   mUsernameView.setError(getResources().getString(R.string.username_content));
                }
                for (User u: mUserList) {
                    if (u.getUsername().equals(s.toString())){
                        mUsernameView.setError(getResources().getString(R.string.username_existence));
                        mUsernameView.setTextColor(Color.RED);
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mUsernameView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                        if (mUsernameView.getText().length() < 6 && mUsernameView.getText().length() != 0){
                        mUsernameView.setError(getResources().getString(R.string.username_length_short));
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

    private void openAccount(View v) {
        sharedPreferences.setLoggedIn(getActivity(), true);
        NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.menu_item_log_out, true).build();
        Navigation.findNavController(v).navigate(R.id.action_fragment_sign_up_to_map_fragment,null,navOptions);
    }


    private void initViews(View v){
        mUsernameView = v.findViewById(R.id.etv_username_sign_up);
        mEmailView = v.findViewById(R.id.etv_email_sign_up);
        mPasswordView = v.findViewById(R.id.etv_password_sign_up);
        mConfirmPasswordView = v.findViewById(R.id.etv_confirm_password_sign_up);
        mSignUpButton = v.findViewById(R.id.btn_register_sign_up);
        mSuggestionSignUp = v.findViewById(R.id.tv_suggestion_sign_up);
    }

    private void registerUserToFirebase(User user) {

        final User mUser = user;

        mFirebaseAuth.createUserWithEmailAndPassword(user.getEmail(),mPasswordView.getText().toString())
                .addOnCompleteListener(this.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        mProgressDialog.dismiss();
                        //checking if success
                        if (task.isSuccessful()) {
                            //registered
                            FirebaseDatabase.getInstance()
                                    .getReference(DATABASE_PATH_NAME)
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(mUser)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                //Toast.makeText(getContext(), "User Added To Firebase",Toast.LENGTH_SHORT).show();

                                            } else {
                                                //Toast.makeText(getContext(), "User Not Added To Firebase",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        } else {
                            String s = task.getException().getMessage();
                            Toast.makeText(getActivity(), ""+s, Toast.LENGTH_SHORT).show();
                            //not registered
                        }
                    }
                });
    }

}
