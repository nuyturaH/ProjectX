package com.har8yun.homeworks.projectx.fragment.launch;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.har8yun.homeworks.projectx.R;

import static com.har8yun.homeworks.projectx.util.NavigationHelper.onClickNavigate;


public class LaunchFragment extends Fragment implements OnMapReadyCallback {

    //views
    private Button mSignInButton;
    private Button mSignUpButton;
    private BottomNavigationView mBottomNavigationView;

    private MapView mapView;

    private GoogleMap mGoogleMap;

    //constructor
    public LaunchFragment() {
    }

    //************************************* LIFECYCLE METHODS **************************************
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_launch, container, false);

        initViews(view);
        changeDesignStyle();

        onClickNavigate(mSignInButton, R.id.action_launch_fragment_to_sign_in_fragment);
        onClickNavigate(mSignUpButton, R.id.action_launch_fragment_to_sign_up_fragment);

        return view;
    }


    //**************************************** METHODS *********************************************
    private void changeDesignStyle() {
        mBottomNavigationView.setVisibility(View.GONE);
    }

    private void initViews(View v) {
        mSignInButton = v.findViewById(R.id.btn_sign_in_launch);
        mSignUpButton = v.findViewById(R.id.btn_sign_up_launch);
        mBottomNavigationView = getActivity().findViewById(R.id.bottom_navigation_view_main);
        mapView = v.findViewById(R.id.mv_launch);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        //mGoogleMap = googleMap;

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.3092293,-122.1136845))
                .title("Captain America"));

        //googleMap.addMarker()

    }
}