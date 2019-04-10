package com.har8yun.homeworks.projectx.fragment.event;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.adapter.ChartsAdapter;
import com.har8yun.homeworks.projectx.adapter.ParticipantsAdapter;
import com.har8yun.homeworks.projectx.model.Event;
import com.har8yun.homeworks.projectx.model.EventViewModel;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.model.UserViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;


public class ParticipantsFragment extends Fragment {

    private static final String TAG = "ParticipantsFragment";

    //views
    private RecyclerView mRecyclerView ;
    private TextView mMessageView;

    //navigation
    private NavController mNavController;
    private Fragment mNavHostFragment;

    //viewmodel
    private EventViewModel mEventViewModel;
    private UserViewModel mUserViewModel;
    Event mCurrentEvent;

    List<User> mParticipantsList = new ArrayList<>();
    List<String> mUidList = new ArrayList<>();

    //Firebase
    DatabaseReference mFirebaseReference;

    //adapter
    private ParticipantsAdapter mParticipantsAdapter;

    User currentUser;


    public ParticipantsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_participants, container, false);


        mMessageView = view.findViewById(R.id.tv_no_participants_yet);

        mUserViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        mUserViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                currentUser = user;
            }
        });
        currentUser = mUserViewModel.getUser().getValue();

        mEventViewModel = ViewModelProviders.of(getActivity()).get(EventViewModel.class);
        mEventViewModel.getEvent().observe(getViewLifecycleOwner(), new Observer<Event>() {
            @Override
            public void onChanged(@Nullable final Event event) {
                mCurrentEvent = event;
                mUidList = event.getParticipants();
            }
        });
        mCurrentEvent = mEventViewModel.getEvent().getValue();
        if(mCurrentEvent.getParticipants() != null && mCurrentEvent.getParticipants().size() > 0) {
            mUidList = mCurrentEvent.getParticipants();
        }
        else{
            mMessageView.setVisibility(View.VISIBLE);
        }

        getUsersFromFirebase();
        Log.d(TAG, "onCreateView: " + mParticipantsList.size());

        initViews(view);
        return view;
    }


    //************************************** METHODS ********************************************
    private void initViews(View view)
    {
        mRecyclerView = view.findViewById(R.id.rv_participants);
        mNavHostFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        Log.d(TAG, "initViews: " + mParticipantsList.size());
        initRecyclerView(view);
    }

    private void initRecyclerView(View view)
    {

        Log.d(TAG, "initRecyclerView: BBBBBBBBBBBBBBBBBB");
        mParticipantsAdapter = new ParticipantsAdapter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mRecyclerView.setAdapter(mParticipantsAdapter);
//        mParticipantsAdapter.addItems(mParticipantsList);

        Log.d(TAG, "initRecyclerView: " + mParticipantsList.size());


        mParticipantsAdapter.setOnRvItemClickListener(new ParticipantsAdapter.OnRvItemClickListener() {
            @Override
            public void onItemClicked(String uid) {
                for (User user : mParticipantsList) {
                    if (user.getId().equals(uid)) {
                        if (currentUser.getId().equals(uid)) {
                            NavHostFragment.findNavController(mNavHostFragment).navigate(R.id.action_participants_fragment_to_my_profile_fragment);
                        } else {
                            mUserViewModel.setOtherUser(user);
                            NavHostFragment.findNavController(mNavHostFragment).navigate(R.id.action_participants_fragment_to_other_profile_fragment);
                        }
                    }
                }

            }
        });
    }

    private void getUsersFromFirebase()
    {
//        Log.d(TAG, "getUsersFromFirebase: " + mUidList.get(0));
        mFirebaseReference = FirebaseDatabase.getInstance().getReference("users");
        mFirebaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mParticipantsList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    for(String id : mUidList)
                    {
                        if(id.equals(user.getId())) {
                            mParticipantsList.add(user);
                            mParticipantsAdapter.addItem(user);
                            Log.d(TAG, "onDataChange: " + mParticipantsList.size());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}
