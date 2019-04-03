package com.har8yun.homeworks.projectx.fragment.event;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.model.Event;
import com.har8yun.homeworks.projectx.model.EventViewModel;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.model.UserViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import static com.google.android.gms.common.util.CollectionUtils.setOf;


public class CreateEventFragment extends Fragment {

    //view
    private Toolbar mToolbarCreateEvent;
    private EditText mTitleView;
    private EditText mDescriptionView;
    private TextView mDateView;
    private Button mSaveButton;
    private TextView mLocationView;
    private BottomNavigationView mBottomNavigationView;

    //calendar
    private Calendar cal = Calendar.getInstance();

    //navigation
    private NavController mNavController;

    //Object
    private Event mEvent;
    private EventViewModel mEventViewModel;
    private UserViewModel mUserViewModel;
    private User mCurrentUser;
    private boolean pass1, pass2, pass3 = false;

    //Firebase
    DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("events");


    //constructor
    public CreateEventFragment() {
    }


    //************************************** METHODS ********************************************
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        hideBotNavBar();


        if (mEventViewModel.isToEdit()) {
            initEditViews();
        }

        mUserViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        mCurrentUser = mUserViewModel.getUser().getValue();


        setCreateEventToolbar();
        setNavigationComponent();

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEventViewModel.isToEdit()) {
                    editEvent();
                    Navigation.findNavController(v).navigate(R.id.action_create_event_fragment_to_map_fragment);
                } else {

                    initEvent();

                }

            }
        });

        mDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass3 = true;
                chooseDate();

            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_create_event, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_event:
                removeEventDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void removeEventDialog() {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete this event")

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        removeEventFromFirebase();
                        dialog.dismiss();
                    }

                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();

    }

    private void chooseDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mEvent.setDate(new Date());
                cal.setTime(mEvent.getDate());
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthOfYear);
                cal.set(Calendar.DATE, dayOfMonth);
                mEvent.setDate(cal.getTime());
                chooseTime();
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    private void chooseTime() {
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                cal.setTime(mEvent.getDate());
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);
                mEvent.setDate(cal.getTime());
                mEventViewModel.setEvent(mEvent);
                SimpleDateFormat simpleDateformat2 = new SimpleDateFormat("MMM dd, hh:mm");
                mDateView.setText(simpleDateformat2.format(mEvent.getDate()));
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    private void initViews(View v) {
        mToolbarCreateEvent = v.findViewById(R.id.toolbar_create_event);
        mTitleView = v.findViewById(R.id.etv_title_create_event);
        mDescriptionView = v.findViewById(R.id.etv_description_create_event);
        mDateView = v.findViewById(R.id.tv_date_create_event);
        mSaveButton = v.findViewById(R.id.btn_save_create_event);
        mLocationView = v.findViewById(R.id.tv_location_create_event);
        mBottomNavigationView = getActivity().findViewById(R.id.bottom_navigation_view_main);
        mLocationView.setText(mEvent.getPlace());
    }


    private void initEvent() {
        mEvent.setCreator(mCurrentUser);
        if (mTitleView.getText().toString().isEmpty()) {
            mTitleView.setError("Field must be filled");

            pass1 = false;

        } else {
            mEvent.setTitle(mTitleView.getText().toString());
            pass1 = true;
        }
        if (mDescriptionView.getText().toString().isEmpty()) {
            mDescriptionView.setError("Field must be filled");
            pass2 = false;
        } else {
            mEvent.setDescription(mDescriptionView.getText().toString());
            pass2 = true;
        }

        if (pass1 && pass2 && pass3) {
            mEventViewModel.setEvent(mEvent);
            addEventToFirebase();
            Navigation.findNavController(getView()).navigate(R.id.action_create_event_fragment_to_map_fragment);
        }

    }

    private void initEditViews() {
        mTitleView.setText(mEvent.getTitle());
        mDescriptionView.setText(mEvent.getDescription());
        mDateView.setText(mEvent.getDate().toString());
        mSaveButton.setText("Save Changes");

    }

    private void editEvent() {
        mEvent.setTitle(mTitleView.getText().toString());
        mEvent.setDescription(mDescriptionView.getText().toString());

        mEventViewModel.setEvent(mEvent);
        updateEventInFirebase();
    }

    private void addEventToFirebase() {
        mEvent.setUid(mDatabaseReference.push().getKey());
        mDatabaseReference.child(mEvent.getUid()).setValue(mEvent);
    }

    private void updateEventInFirebase() {
        mDatabaseReference.child(mEvent.getUid()).setValue(mEvent);
    }

    public void updateEventInFirebase(Event event) {
        mDatabaseReference.child(event.getUid()).setValue(event);
    }

    public void removeEventFromFirebase() {
        mDatabaseReference.child(mEvent.getUid()).removeValue();
        Navigation.findNavController(getView()).navigate(R.id.action_create_event_fragment_to_map_fragment);

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

    private void hideBotNavBar() {
        mBottomNavigationView.setVisibility(View.GONE);
    }


}
