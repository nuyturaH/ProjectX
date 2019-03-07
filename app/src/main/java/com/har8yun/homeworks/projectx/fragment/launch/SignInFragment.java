package com.har8yun.homeworks.projectx.fragment.launch;


import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.model.UserViewModel;
import com.har8yun.homeworks.projectx.preferences.SaveSharedPreferences;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import static com.har8yun.homeworks.projectx.util.NavigationHelper.onClickNavigate;


public class SignInFragment extends Fragment {

    //shared preferences
    private SaveSharedPreferences sharedPreferences = new SaveSharedPreferences();

    //views
    private EditText mUsernameView;
    private EditText mPasswordView;
    private Button mSignInButton;
    private TextView mSuggestionSignIn;

    //Dialog
    ProgressDialog mProgressDialog;

    //user
    private User mCurrentUser;
    private UserViewModel mUserViewModel;

    //collections
    private List<User> mUserList = new ArrayList<>();

    //Firebase
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabase;
    public static final String DATABASE_PATH_NAME = "Users";

    //constructor
    public SignInFragment() {
    }

    //************************************ LIFECYCLE METHODS ****************************************
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        getUsersFromDatabase();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this.getActivity());

        initViews(view);

        onClickNavigate(mSuggestionSignIn, R.id.action_fragment_sign_in_to_fragment_sign_up);

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUsernameView.getText().length() == 0) {
                    mUsernameView.setError(getResources().getString(R.string.username_enter));
                } else if (mPasswordView.getText().length() == 0) {
                    mPasswordView.setError(getResources().getString(R.string.password_enter));
                } else {
                    isValidUser(v);
                }
            }
        });
        return view;
    }


    //************************************** METHODS ********************************************
    private void openAccount(View v) {
        sharedPreferences.setLoggedIn(getActivity(), true);
        mUserViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        mUserViewModel.setUser(mCurrentUser);
        NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.menu_item_log_out, true).build();
        Navigation.findNavController(v).navigate(R.id.action_fragment_sign_in_to_map_fragment, null, navOptions);
    }


    private void isValidUser(View v) {
        final View view = v;
        mProgressDialog.setMessage("Logging In...");
        mProgressDialog.show();

        mFirebaseAuth.signInWithEmailAndPassword(mUsernameView.getText().toString(), mPasswordView.getText().toString())
                .addOnCompleteListener(this.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mProgressDialog.dismiss();
                        if (task.isSuccessful()) {
                            //login
                            getCurrentUserFromDatabase();
                            openAccount(view);
                        } else {
                            if (task.getException().getMessage().equals(getResources().getString(R.string.task_error_password))) {
                                mPasswordView.setError(getResources().getString(R.string.password_wrong));
                            } else if (task.getException().getMessage().equals(getResources().getString(R.string.task_error_username))) {
                                mUsernameView.setError(getResources().getString(R.string.username_availability));
                            }
                        }
                    }
                });
    }

    private void initViews(View view) {
        mUsernameView = view.findViewById(R.id.etv_username_sign_in);
        mPasswordView = view.findViewById(R.id.etv_password_sign_in);
        mSignInButton = view.findViewById(R.id.btn_login_sign_in);
        mSuggestionSignIn = view.findViewById(R.id.tv_suggestion_sign_in);
    }

    public void getUsersFromDatabase() {
        mDatabase = FirebaseDatabase.getInstance().getReference(DATABASE_PATH_NAME);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUserList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User mUser = userSnapshot.getValue(User.class);
                    mUserList.add(mUser);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void getCurrentUserFromDatabase() {
        for (User user : mUserList) {
            if (user.getEmail().equals(mUsernameView.getText().toString())) {
                mCurrentUser = user;
            }
        }
        Log.d("SignIn", mCurrentUser.toString());

    }

}
