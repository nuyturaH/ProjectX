package com.har8yun.homeworks.projectx.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.har8yun.homeworks.projectx.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LaunchFragment extends Fragment {


    public LaunchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_launch, container, false);
    }

}
