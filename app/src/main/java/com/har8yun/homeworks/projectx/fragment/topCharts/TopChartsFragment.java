package com.har8yun.homeworks.projectx.fragment.topCharts;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.model.UserViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static com.google.android.gms.common.util.CollectionUtils.setOf;
import static com.har8yun.homeworks.projectx.fragment.launch.SignInFragment.DATABASE_PATH_NAME;


public class TopChartsFragment extends Fragment {

    //final Strings
    private static final String TAG = "TopChartsFragment";
    public static final String PULL_UPS = "Pull-ups";
    public static final String PUSH_UPS = "Push-ups";
    public static final String PARALLEL_DIPS = "Parellel Dips";
    public static final String JUGGLING = "Juggling";
    public static final String POINTS = "Points";

    //navigation
    private NavController mNavController;
    private Fragment mNavHostFragment;

    //views
    private Toolbar mToolbarTopCharts;
    private TabLayout mTabLayout;
    private RecyclerView mRecyclerView;

    //adapter
    private ChartsAdapter mChartsAdapter;

    //Firebase
    private DatabaseReference mFirebaseDatabse;

    //viewmodel
    private UserViewModel mUserViewModel;

    //user
    List<User> mUserList = new ArrayList<>();
    User otherUser;
    User currentUser;

    List<User> mPullUpsList = new ArrayList<>();
    List<User> mPushUpsList = new ArrayList<>();
    List<User> mParallelDipsList = new ArrayList<>();
    List<User> mJugglingList = new ArrayList<>();
    List<User> mPointsList = new ArrayList<>();

    //constructor
    public TopChartsFragment() {
    }

    //************************************ LIFECYCLE METHODS ****************************************
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_charts, container, false);

        mUserViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        mUserViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                currentUser = user;
            }
        });
        currentUser = mUserViewModel.getUser().getValue();

        mFirebaseDatabse = FirebaseDatabase.getInstance().getReference(DATABASE_PATH_NAME);
        getUsersFromFirebase();
        initViews(view);
        initRecyclerView(mPullUpsList, PULL_UPS);
        setTopChartsToolbar();
        setNavigationComponent();

        return view;
    }

    //************************************** METHODS ********************************************
    private void initViews(View view) {
        mToolbarTopCharts = view.findViewById(R.id.toolbar_top_charts);
        mTabLayout = view.findViewById(R.id.tabs);
        mRecyclerView = view.findViewById(R.id.rv_charts);
        mNavHostFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        initRecyclerView(mPullUpsList, PULL_UPS);
                        break;
                    case 1:
                        initRecyclerView(mPushUpsList, PUSH_UPS);
                        break;
                    case 2:
                        initRecyclerView(mParallelDipsList, PARALLEL_DIPS);
                        break;
                    case 3:
                        initRecyclerView(mJugglingList, JUGGLING);
                        break;
                    case 4:
                        initRecyclerView(mPointsList, POINTS);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });



    }

    private void initRecyclerView(List<User> listToPass, String key) {
        mChartsAdapter = new ChartsAdapter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mRecyclerView.setAdapter(mChartsAdapter);

        sortItems(listToPass, key);

        List<User> tenList = new ArrayList<>();
        for (int i = 0; i < 10 && i < listToPass.size(); i++) {
            tenList.add(listToPass.get(i));
        }

        mChartsAdapter.addItems(tenList, key);

        mChartsAdapter.setOnRvItemClickListener(new ChartsAdapter.OnRvItemClickListener() {
            @Override
            public void onItemClicked(String uid) {
                for (User user : mUserList) {
                    if (user.getId().equals(uid)) {
                        if (currentUser.getId().equals(uid)) {
                            NavHostFragment.findNavController(mNavHostFragment).navigate(R.id.action_top_charts_fragment_to_my_profile_fragment);
                        } else {
                            mUserViewModel.setOtherUser(user);
                            NavHostFragment.findNavController(mNavHostFragment).navigate(R.id.action_top_charts_fragment_to_other_profile_fragment);
                        }
                    }
                }

            }
        });
    }

    private void getUsersFromFirebase() {
        mFirebaseDatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUserList.clear();
                mPushUpsList.clear();
                mPullUpsList.clear();
                mParallelDipsList.clear();
                mJugglingList.clear();
                mPointsList.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    mUserList.add(user);
                    if (user.getSkills() != null) {
                        if (user.getSkills().containsKey(PULL_UPS)) {
                            mPullUpsList.add(user);
                        }
                        if (user.getSkills().containsKey(PUSH_UPS)) {
                            mPushUpsList.add(user);

                        }
                        if (user.getSkills().containsKey(PARALLEL_DIPS)) {
                            mParallelDipsList.add(user);
                        }
                        if (user.getSkills().containsKey(JUGGLING)) {
                            mJugglingList.add(user);
                        }
                        if (user.getPoints() != null) {
                            mPointsList.add(user);
                        }
                    }
                }
                initRecyclerView(mPushUpsList, PULL_UPS);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void sortItems(List<User> listToSort, String key) {

        Collections.sort(listToSort, new Comparator<User>() {
            @Override
            public int compare(User t1, User t2) {
                if (!key.equals(POINTS)) {
                    return t2.getSkills().get(key).compareTo(t1.getSkills().get(key));
                } else {
                    return t2.getPoints().compareTo(t1.getPoints());
                }
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
