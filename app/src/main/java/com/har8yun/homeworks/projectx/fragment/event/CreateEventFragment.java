package com.har8yun.homeworks.projectx.fragment.event;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.model.Event;
import com.har8yun.homeworks.projectx.model.EventViewModel;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.model.UserViewModel;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import static com.google.android.gms.common.util.CollectionUtils.setOf;
import static com.har8yun.homeworks.projectx.util.NavigationHelper.onClickNavigate;


public class CreateEventFragment extends Fragment {

    //view
    private Toolbar mToolbarCreateEvent;
    private EditText mTitleView;
    private EditText mDescriptionView;
    private TextView mTimeView;
    private Button mSaveButton;

    //navigation
    private NavController mNavController;

    //Object

    private Event mEvent;
    private EventViewModel mEventViewModel;
    private UserViewModel mUserViewModel;
    private User mCurrentUser;


    //constructor
    public CreateEventFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);


        mEventViewModel = ViewModelProviders.of(getActivity()).get(EventViewModel.class);
        mUserViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);

        mCurrentUser = mUserViewModel.getUser().getValue();
        initViews(view);
        setCreateEventToolbar();
        setNavigationComponent();
        onClickNavigate(mSaveButton, R.id.action_map_fragment_to_create_event_fragment);


        return view;
    }

    private void initViews(View v) {
        mToolbarCreateEvent = v.findViewById(R.id.toolbar_create_event);
        mTitleView = v.findViewById(R.id.etv_title_create_event);
        mDescriptionView = v.findViewById(R.id.etv_description_create_event);
        mTimeView = v.findViewById(R.id.tv_time_create_event);
        mSaveButton = v.findViewById(R.id.btn_save_create_event);


        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initEvent();
                Navigation.findNavController(v).navigate(R.id.action_create_event_fragment_to_map_fragment);
            }
        });
    }

    private void initEvent() {
        mEvent = new Event();
        mEvent.setCreator(mCurrentUser);
        mEvent.setTitle(mTitleView.getText().toString());
        mEvent.setDescription(mDescriptionView.getText().toString());

        mEventViewModel.setEvent(mEvent);

    }

    private void setCreateEventToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbarCreateEvent);
        setHasOptionsMenu(true);
    }

        private void setNavigationComponent() {
        mNavController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(mToolbarCreateEvent, mNavController);
    }


}
