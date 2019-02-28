package com.har8yun.homeworks.projectx.fragment.bottomNavigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.preferences.SaveSharedPreferences;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;


public class MyProfileFragment extends Fragment {
    private SaveSharedPreferences sharedPreferences = new SaveSharedPreferences();

    //views
    private Button mLogOutButton;

    //constructor
    public MyProfileFragment() {

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        initViews(view);

        mLogOutButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.map_fragment, true).build();
               Navigation.findNavController(v).navigate(R.id.action_my_profile_fragment_to_fragment_launch,null, navOptions);
               sharedPreferences.setLoggedIn(getActivity(),false);
           }
        });

        return view;
    }

    private void initViews(View view) {
        mLogOutButton = view.findViewById(R.id.btn_log_out_my_profile);
    }

}
