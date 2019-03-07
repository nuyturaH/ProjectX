package com.har8yun.homeworks.projectx.fragment.myProfile;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.adapter.SkillItemRecyclerAdapter;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.model.UserViewModel;
import com.har8yun.homeworks.projectx.preferences.SaveSharedPreferences;

import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static com.google.android.gms.common.util.CollectionUtils.setOf;
import static com.har8yun.homeworks.projectx.util.NavigationHelper.onClickNavigate;


public class MyProfileFragment extends Fragment {

    //shared preferences
    private SaveSharedPreferences sharedPreferences = new SaveSharedPreferences();

    //navigation
    private NavController mNavController;

    //views
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private FloatingActionButton mEditButton;
    private TextView mPointsView;
    private TextView mStatusView;
    private TextView mHeightView;
    private TextView mWeightView;
    private TextView mFirstNameView;
    private TextView mLastNameView;
    private TextView mGenderView;
    private TextView mAgeView;

    private Fragment mNavHostFragment;

    //user
    private User mCurrentUser;
    private UserViewModel mUserViewModel;


    //constructor
    public MyProfileFragment() {
    }


    //************************************** LIFECYCLE METHODS ********************************************
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        initViews(view);
        setMyProfileToolbar();

        onClickNavigate(mEditButton, R.id.action_my_profile_fragment_to_my_profile_edit_fragment);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUserViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        mUserViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable final User user) {
                Log.e("hhhh", "ViewModel in My profile " + user.toString());
                setNavigationComponent(user);
                mCurrentUser.setUserInfo(user.getUserInfo());
                mCurrentUser = user;
                fillViews();
            }
        });
    }


    //************************************ OVERRIDE METHODS ****************************************
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_my_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_log_out:
                logOut();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //************************************** METHODS ********************************************
    private void setNavigationComponent(final User user) {
        mNavController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        mNavController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.my_profile_fragment) {
                    if (mCurrentUser.getUsername() != null) {
                        NavHostFragment.findNavController(mNavHostFragment).getCurrentDestination().setLabel(user.getUsername());
                    }
                }
            }
        });
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(setOf(R.id.my_profile_fragment)).build();
        NavigationUI.setupWithNavController(mCollapsingToolbarLayout, mToolbar, mNavController, appBarConfiguration);
    }

    private void setMyProfileToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        setHasOptionsMenu(true);
    }

    private void initViews(View view) {
        mToolbar = view.findViewById(R.id.toolbar_my_profile);
        mCollapsingToolbarLayout = view.findViewById(R.id.ctl_my_profile);
        mEditButton = view.findViewById(R.id.fab_edit_my_profile);
        mPointsView = view.findViewById(R.id.tv_points_my_profile);
        mStatusView = view.findViewById(R.id.tv_status_my_profile);
        mHeightView = view.findViewById(R.id.tv_height_my_profile);
        mWeightView = view.findViewById(R.id.tv_weight_my_profile);
        mFirstNameView = view.findViewById(R.id.tv_first_name_my_profile);
        mLastNameView = view.findViewById(R.id.tv_last_name_my_profile);
        mGenderView = view.findViewById(R.id.tv_gender_my_profile);
        mAgeView = view.findViewById(R.id.tv_age_my_profile);
        mNavHostFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);


        mUserViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        mUserViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable final User user) {
                Log.e("hhhh", "ViewModel in My profile " + user.toString());
                mCurrentUser = user;
            }
        });


    }

    private void fillViews() {
        Log.e("hhhh", "fillviews");
        if (mCurrentUser.getUserInfo() != null) {
            if (mCurrentUser.getUserInfo().getFirstName() != null) {
                mFirstNameView.setText(mCurrentUser.getUserInfo().getFirstName());
            }
            if (mCurrentUser.getUserInfo().getLastName() != null) {
                mLastNameView.setText(mCurrentUser.getUserInfo().getLastName());
            }
            if (mCurrentUser.getUserInfo().getGender() != null) {
                if (mCurrentUser.getUserInfo().getGender() == 0) {
                    mGenderView.setText("Male");
                } else {
                    mGenderView.setText("Female");
                }
            }
            if (mCurrentUser.getUserInfo().getBirthDate() != null) {
                //TODO set age
            }
            if (mCurrentUser.getUserInfo().getWeight() != null) {
                mWeightView.setText(String.valueOf(mCurrentUser.getUserInfo().getWeight()));
                Log.d("Profile",String.valueOf(mCurrentUser.getUserInfo().getWeight()));
                Toast.makeText(getContext(),String.valueOf(mCurrentUser.getUserInfo().getWeight()),Toast.LENGTH_SHORT).show();
            }
            if (mCurrentUser.getUserInfo().getHeight() != null) {
                mHeightView.setText(String.valueOf((mCurrentUser.getUserInfo().getHeight())));
            }
            if (mCurrentUser.getUserInfo().getAvatar() != null) {
                //TODO set avatar
            }

            if (mCurrentUser.getUsername() != null) {
                //username setting is in setNavigationComponent()
            }
            if (mCurrentUser.getSkills() != null) {
                initRecyclerView();
            }
        }

    }

    private void logOut() {
        NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.map_fragment, true).build();
        Fragment mNavHostFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavHostFragment.findNavController(mNavHostFragment).navigate(R.id.menu_item_log_out, null, navOptions);
        sharedPreferences.setLoggedIn(getActivity(), false);

        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getContext(), "Signed Out", Toast.LENGTH_SHORT).show();
    }


    private void initRecyclerView() {
        SkillItemRecyclerAdapter skillItemRecyclerAdapter = new SkillItemRecyclerAdapter();
//        mSkillItemRecyclerAdapter.setOnRvItemClickListener(new SkillItemRecyclerAdapter.OnRvItemClickListener() {
//            @Override
//            public void onItemClicked(int pos) {
//                Skill item = mSkillList.get(pos);
//                Toast.makeText(getActivity(), "Clicked : " + item.getSkillName() + ": " + item.getSkillCount(), Toast.LENGTH_SHORT).show();
//            }
//        });
        RecyclerView recyclerView = getView().findViewById(R.id.rv_skills_my_profile);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(skillItemRecyclerAdapter);
        skillItemRecyclerAdapter.addItems(mCurrentUser.getSkills());
    }

}