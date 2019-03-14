package com.har8yun.homeworks.projectx.fragment.event;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.model.Event;
import com.har8yun.homeworks.projectx.model.EventViewModel;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.model.UserViewModel;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import static com.google.android.gms.common.util.CollectionUtils.setOf;


public class CreateEventFragment extends Fragment {

    //view
    private Toolbar mToolbarCreateEvent;
    private EditText mTitleView;
    private EditText mDescriptionView;
    private TextView mTimeView;
    private Button mSaveButton;
    private TextView mLocationView;

    //navigation
    private NavController mNavController;

    //Object
    private Event mEvent;
    private EventViewModel mEventViewModel;
    private UserViewModel mUserViewModel;
    private User mCurrentUser;

    //Firebase
    DatabaseReference mDatabaseReference= FirebaseDatabase.getInstance().getReference("events");


    //constructor
    public CreateEventFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);


        mEventViewModel = ViewModelProviders.of(getActivity()).get(EventViewModel.class);
        mEventViewModel.getEvent().observe(getViewLifecycleOwner(), new Observer<Event>() {
            @Override
            public void onChanged(@Nullable final Event event) {
                mEvent = event;
            }
        });
        mEvent = mEventViewModel.getEvent().getValue();
        initViews(view);


        if (mEventViewModel.isToEdit()) {
            initEditViews();
        }
        Toast.makeText(getContext(), "" + mEvent.getPosition(), Toast.LENGTH_LONG).show();

        mUserViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        mCurrentUser = mUserViewModel.getUser().getValue();

//        mDatabaseReference = FirebaseDatabase.getInstance().getReference("events");


        setCreateEventToolbar();
        setNavigationComponent();
        //onClickNavigate(mSaveButton, R.id.action_map_fragment_to_create_event_fragment);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEventViewModel.isToEdit())
                {
                    editEvent();
                }
                else {
                    initEvent();
                }
                Navigation.findNavController(v).navigate(R.id.action_create_event_fragment_to_map_fragment);
            }
        });


        return view;
    }

    private void initViews(View v) {
        mToolbarCreateEvent = v.findViewById(R.id.toolbar_create_event);
        mTitleView = v.findViewById(R.id.etv_title_create_event);
        mDescriptionView = v.findViewById(R.id.etv_description_create_event);
        mTimeView = v.findViewById(R.id.tv_time_create_event);
        mSaveButton = v.findViewById(R.id.btn_save_create_event);
        mLocationView = v.findViewById(R.id.tv_location_create_event);

        mLocationView.setText(mEvent.getPlace());

    }


    private void initEvent() {
        mEvent.setCreator(mCurrentUser);
        mEvent.setTitle(mTitleView.getText().toString());
        mEvent.setDescription(mDescriptionView.getText().toString());

        mEventViewModel.setEvent(mEvent);
        addEventToFirebase();

    }

    private void initEditViews()
    {
        mTitleView.setText(mEvent.getTitle());
        mDescriptionView.setText(mEvent.getDescription());
        //mTimeView.setText(mEvent.getDate().toString());
        mSaveButton.setText("Save Changes");

    }

    private void editEvent()
    {
        mEvent.setTitle(mTitleView.getText().toString());
        mEvent.setDescription(mDescriptionView.getText().toString());

        mEventViewModel.setEvent(mEvent);
        updateEventInFirebase();
    }

    private void addEventToFirebase() {
        mEvent.setUid(mDatabaseReference.push().getKey());
        mDatabaseReference.child(mEvent.getUid()).setValue(mEvent);
    }

    private void updateEventInFirebase()
    {
        mDatabaseReference.child(mEvent.getUid()).setValue(mEvent);
    }
    public void updateEventInFirebase(Event event)
    {
        mDatabaseReference.child(event.getUid()).setValue(event);
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
