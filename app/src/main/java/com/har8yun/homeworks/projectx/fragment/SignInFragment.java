package com.har8yun.homeworks.projectx.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.activity.MainActivity;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.preferences.SaveSharedPreferences;

import java.util.ArrayList;
import java.util.List;


public class SignInFragment extends Fragment {

    private List<User> userList = new ArrayList<>(); // get this list from database

    private TextInputEditText username;
    private TextInputEditText password;
    private Button signInButton;

    private TextView suggestionSignUp;

    private SaveSharedPreferences sharedPreferences = new SaveSharedPreferences();
    private boolean validUser;
//    private boolean validPassword;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

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

        getUsersFromDatabase();


        firebaseAuth = FirebaseAuth.getInstance();

        username = view.findViewById(R.id.etv_username_sign_in);
        password = view.findViewById(R.id.etv_password_sign_in);
        signInButton = view.findViewById(R.id.btn_login);      // button ի անունը login եմ դրել,որ launch ի signIn ից տարբերվի
        suggestionSignUp = view.findViewById(R.id.tv_suggestion_sign_up2);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
                if (mOnSignInFragmentActionListener != null && validUser) {
                    mOnSignInFragmentActionListener.onLoginButtonClicked();
                }
            }
        });

        suggestionSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSignInFragmentActionListener != null) {
                    mOnSignInFragmentActionListener.onSuggestionSignUpClicked();
                }
            }
        });

    }

    public void getUsersFromDatabase()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userList.clear();
                for(DataSnapshot userSnapshot : dataSnapshot.getChildren())
                {
                    User mUser = userSnapshot.getValue(User.class);
                    userList.add(mUser);
                    Log.d("SignIn",userList.get(0).toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void signInUser() {

        for (int i = 0; i < userList.size(); i++) {
            firebaseAuth.signInWithEmailAndPassword(userList.get(i).getEmail(),userList.get(i).getPassword())
                    .addOnCompleteListener(this.getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                //go to other fragment
                                Toast.makeText(getContext(),"Success",Toast.LENGTH_SHORT).show();
                                Log.d("SignIn","Success");
                            }
                            else {
                                Log.d("SignIn","Unsuccess " + task.getException().getMessage());
                            }

                        }
                    });

            if (!username.getText().toString().trim().equals(userList.get(i).getEmail()) || (!password.getText().toString().trim().equals(userList.get(i).getPassword())) ) {
                password.setError("Wrong Username or Password.");
                validUser = false;
            } else {
                validUser = true;
                sharedPreferences.setLoggedIn(getContext(), true);
            }
        }
    }

    public void setOnSignInFragmentActionListener(OnSignInFragmentActionListener onFragmentActionListener) {
        mOnSignInFragmentActionListener = onFragmentActionListener;
    }


    public interface OnSignInFragmentActionListener {
        void onLoginButtonClicked();

        void onSuggestionSignUpClicked();
    }

}
