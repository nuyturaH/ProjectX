package com.har8yun.homeworks.projectx.fragment.launch;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.preferences.SaveSharedPreferences;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import static com.har8yun.homeworks.projectx.util.NavigationHelper.onClickNavigate;


public class SignInFragment extends Fragment {

    private SaveSharedPreferences sharedPreferences = new SaveSharedPreferences();

    //views
    private EditText mUsernameView;
    private EditText mPasswordView;
    private Button mSignInButton;
    private TextView mSuggestionSignIn;

    //collections
    private List<User> mUserList = new ArrayList<>(); // get this list from database

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

        initViews(view);

        onClickNavigate(mSuggestionSignIn, R.id.action_fragment_sign_in_to_fragment_sign_up);

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUsernameView.getText().length() == 0) {
                    mUsernameView.setError(getResources().getString(R.string.username_enter));
                }else if (mPasswordView.getText().length() == 0) {
                    mPasswordView.setError(getResources().getString(R.string.password_enter));
                }else if (isValidUser()) {
                    openAccount(v);
                }

            }
        });
        return view;
    }


    //************************************** METHODS ********************************************
    private void openAccount(View v) {
        sharedPreferences.setLoggedIn(getActivity(), true);
        NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.fragment_launch, true).build();
        Navigation.findNavController(v).navigate(R.id.action_fragment_sign_in_to_map_fragment,null,navOptions);
    }

    private boolean isValidUser() {
        for (int i = 0; i < mUserList.size(); i++) {
            if (mUsernameView.getText().toString().equals(mUserList.get(i).getUsername())) {
                if (mPasswordView.getText().toString().toCharArray().equals(mUserList.get(i).getPassword())){
                    return true;
                }
                mPasswordView.setError(getResources().getString(R.string.password_wrong));
                return false;
            }
        }
        mUsernameView.setError(getResources().getString(R.string.username_availability));
        return false;
    }

    private void initViews(View view) {
        mUsernameView = view.findViewById(R.id.etv_username_sign_in);
        mPasswordView = view.findViewById(R.id.etv_password_sign_in);
        mSignInButton = view.findViewById(R.id.btn_login_sign_in);
        mSuggestionSignIn = view.findViewById(R.id.tv_suggestion_sign_in);
    }

}
