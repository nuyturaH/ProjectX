package com.har8yun.homeworks.projectx.fragment.event;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;

import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.model.Event;
import com.har8yun.homeworks.projectx.model.EventViewModel;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.model.UserViewModel;

import androidx.navigation.ui.AppBarConfiguration;

import static com.google.android.gms.common.util.CollectionUtils.setOf;
import static com.har8yun.homeworks.projectx.util.NavigationHelper.onClickNavigate;


public class CreateEventFragment extends Fragment {

    //view

    private EditText mTitleView;
    private EditText mDescriptionView;
    private TextView mTimeView;
    private Button mSaveButton;

    //Object

    private Event mEvent;
    private EventViewModel mEventViewModel;
    private UserViewModel mUserViewModel;
    private User mCurrentUser;


    public CreateEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);


        mEventViewModel = ViewModelProviders.of(getActivity()).get(EventViewModel.class);
        mUserViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);

        mCurrentUser = mUserViewModel.getUser().getValue();
        initViews(view);


        return view;
    }

    private void initViews(View v)
    {
        mTitleView = v.findViewById(R.id.etv_title_create_event);
        mDescriptionView = v.findViewById(R.id.etv_description_create_event);
        mTimeView = v.findViewById(R.id.tv_time);
        mSaveButton = v.findViewById(R.id.btn_save_create_event);



        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initEvent();
                //TODO open MapFragment
            }
        });
    }

    private void initEvent()
    {
        mEvent = new Event();
        mEvent.setCreator(mCurrentUser);
        mEvent.setTitle(mTitleView.getText().toString());
        mEvent.setDescription(mDescriptionView.getText().toString());

        mEventViewModel.setEvent(mEvent);

    }



}
