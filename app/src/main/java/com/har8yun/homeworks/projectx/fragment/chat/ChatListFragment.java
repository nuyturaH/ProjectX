package com.har8yun.homeworks.projectx.fragment.chat;


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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.adapter.ChatListAdapter;
import com.har8yun.homeworks.projectx.model.User;

import java.util.ArrayList;
import java.util.List;


public class ChatListFragment extends Fragment {


    private List<User> mUsersList = new ArrayList<>();
    private ChatListAdapter mRecyclerAdapter;

    //View
    private View view;
    private RecyclerView recyclerView;

    //Firebase
    private DatabaseReference mDatabase;


    public ChatListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        getUsersFromBundle();
        initRecyclerView();
    }

    private void initRecyclerView()
    {
        mRecyclerAdapter = new ChatListAdapter();

        mRecyclerAdapter.setmListener(
                new ChatListAdapter.OnUserItemClickListener() {
                    @Override
                    public void onUserSelected(User user) {
                        openChat();
                    }

                });

        recyclerView = view.findViewById(R.id.rv_users);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mRecyclerAdapter);

        mRecyclerAdapter.addItems(mUsersList);

    }

    private void getUsersFromBundle()   //TODO: this method gets Users from MapFragment,when user clicks on chat with other users
    {

    }


    private void openChat()     //TODO: this method opens ChatFragment
    {}
}
