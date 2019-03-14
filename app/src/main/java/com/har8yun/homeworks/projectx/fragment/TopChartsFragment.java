package com.har8yun.homeworks.projectx.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.adapter.ChartsAdapter;
import com.har8yun.homeworks.projectx.model.Skill;
import com.har8yun.homeworks.projectx.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static com.google.android.gms.common.util.CollectionUtils.setOf;
import static com.har8yun.homeworks.projectx.fragment.launch.SignInFragment.DATABASE_PATH_NAME;


public class TopChartsFragment extends Fragment {

    //final Strings
    public static final String PULL_UPS = "Pull-ups";
    public static final String PUSH_UPS = "Push-ups";
    public static final String PARALLEL_DIPS = "Parellel Dips"; //TODO check this one!!!!
    public static final String JUGGLING = "Juggling";
    public static final String POINTS = "Points";

    //navigation
    private NavController mNavController;

    //views
    private Toolbar mToolbarTopCharts;
    private TabLayout mTabLayout;
    private TabItem mTabPullUps;
    private RecyclerView mRecyclerView;

    //adapter
    private ChartsAdapter mChartsAdapter;

    //Firebase
    private DatabaseReference mFirebaseDatabse;

    //user
    List<User> mUserList = new ArrayList<>();

    List<User> mPullUpsList = new ArrayList<>();
    List<User> mPushUpsList = new ArrayList<>();

    //constructor
    public TopChartsFragment() {
    }

    //************************************ LIFECYCLE METHODS ****************************************
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_charts, container, false);

        getUsersFromFirebase();
        initViews(view);
        setTopChartsToolbar();
        setNavigationComponent();

        return view;
    }


    //************************************** METHODS ********************************************
    private void initViews(View view) {
        mToolbarTopCharts = view.findViewById(R.id.toolbar_top_charts);
        mTabLayout = view.findViewById(R.id.tabs);
        mTabPullUps = view.findViewById(R.id.ti_pull_ups);
        mRecyclerView = view.findViewById(R.id.rv_charts);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0)
                    initRecyclerView(mPullUpsList,PULL_UPS);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//        mTabPullUps.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                initRecyclerView(mPullUpsList,PULL_UPS);
//            }
//        });


    }

    private void initRecyclerView(List<User> listToPass,String key) {
        mChartsAdapter = new ChartsAdapter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mRecyclerView.setAdapter(mChartsAdapter);

        sortItems(listToPass,key);

        mChartsAdapter.addItems(listToPass,key);
    }

    private void getUsersFromFirebase() {
        mFirebaseDatabse = FirebaseDatabase.getInstance().getReference(DATABASE_PATH_NAME);
        mFirebaseDatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUserList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    mUserList.add(user);
                    if (user.getSkills() != null) {
                        if (user.getSkills().containsKey(PULL_UPS)) {
                            mPullUpsList.add(user);
                        } else if (user.getSkills().containsKey(PUSH_UPS)) {
                            mPushUpsList.add(user);
                        } //...TODO for other skills
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void sortItems(List<User> listToSort,String key) {

        Collections.sort(listToSort, new Comparator<User>() {
            @Override
            public int compare(User t1, User t2) {
                return t2.getSkills().get(key).compareTo(t1.getSkills().get(key));
            }
        });
    }

    private void setTopChartsToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbarTopCharts);
        setHasOptionsMenu(true);
    }

    private void setNavigationComponent() {
        mNavController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(setOf(R.id.top_charts_fragment)).build();
        NavigationUI.setupWithNavController(mToolbarTopCharts, mNavController, appBarConfiguration);
    }

}
