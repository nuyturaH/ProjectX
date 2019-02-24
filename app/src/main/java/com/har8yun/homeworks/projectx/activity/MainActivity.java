package com.har8yun.homeworks.projectx.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.fragment.LaunchFragment;
import com.har8yun.homeworks.projectx.fragment.SignInFragment;
import com.har8yun.homeworks.projectx.fragment.SignUpFragment;
import com.har8yun.homeworks.projectx.model.User;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private LaunchFragment launchFragment;
    private SignInFragment signInFragment;
    private SignUpFragment signUpFragment;

    private SignInFragment.OnSignInFragmentActionListener mOnSignInFragmentActionListener = new SignInFragment.OnSignInFragmentActionListener() {
        @Override
        public void onLoginButtonClicked() {
            //replaceFragment(...);   fragment which is set after SignIn
        }
    };

    private LaunchFragment.OnLaunchFragmentActionListener mOnLaunchFragmentActionListener = new LaunchFragment.OnLaunchFragmentActionListener() {
        @Override
        public void onSignInButtonClicked() {
            signInFragment = new SignInFragment();
            signInFragment.setOnSignInFragmentActionListener(mOnSignInFragmentActionListener);
            addFragment(signInFragment);
        }

        @Override
        public void onSignUpButtonClicked() {
            signUpFragment = new SignUpFragment();
            //TODO set ActionListener for signUpFragment
            addFragment(signUpFragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        launchFragment = new LaunchFragment();
        launchFragment.setOnLaunchFragmentActionListener(mOnLaunchFragmentActionListener);
        addFragment2(launchFragment);
    }

    private void addFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right,R.anim.enter_from_right, R.anim.exit_to_right);

        fragmentTransaction.add(R.id.layout_main, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void addFragment2(Fragment fragment) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.layout_main, fragment);
        fragmentTransaction.commit();
    }



    private void replaceFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.layout_main, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}