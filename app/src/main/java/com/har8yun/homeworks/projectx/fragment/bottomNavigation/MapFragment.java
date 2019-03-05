package com.har8yun.homeworks.projectx.fragment.bottomNavigation;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.preferences.SaveSharedPreferences;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.model.UserViewModel;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static com.google.android.gms.common.util.CollectionUtils.setOf;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    //views
    private BottomNavigationView mBottomNavigationView;
    private Toolbar mToolbarMap;

    //User
    User mUser;

    private MapView mapView;

    private GoogleMap mGoogleMap;

    //navigation
    private NavController mNavController;

    //preferences
    SaveSharedPreferences sharedPreferences = new SaveSharedPreferences();

    //user
    private User mCurrentUser;
    private UserViewModel mUserViewModel;

    //constructor
    public MapFragment() {
    }


    //************************************ LIFECYCLE METHODS ****************************************
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

       // mUser = sharedPreferences.getCurrentUser(getContext());
        initViews(view);
        showBotNavBar();
        setNavigationComponent();
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(setOf(R.id.map_fragment, R.id.settings_fragment)).build();



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUserViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        mUserViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                Log.e("hhhh", "ViewModel "+user.toString());
            }
        });
    }


    //************************************** METHODS ********************************************
    private void setNavigationComponent() {
        mNavController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(mToolbarMap, mNavController);
        if (getArguments() != null) {
            mCurrentUser = new User();
            mCurrentUser = getArguments().getParcelable("signUpUserToMap");
            Log.e("hhhh", "bundle "+mCurrentUser.toString());
        }
    }

    private void showBotNavBar() {
        mBottomNavigationView.setVisibility(View.VISIBLE);
    }

    private void initViews(View v) {
        mBottomNavigationView = getActivity().findViewById(R.id.bottom_navigation_view_main);
        mToolbarMap = v.findViewById(R.id.toolbar_map);
        mapView = v.findViewById(R.id.mv_map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;

        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.3092293,-122.1136845))
                .title("Captain America"));

        //googleMap.addMarker()

    }
}
