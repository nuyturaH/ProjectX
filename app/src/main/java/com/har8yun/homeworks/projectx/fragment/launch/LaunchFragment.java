package com.har8yun.homeworks.projectx.fragment.launch;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.fragment.event.CreateEventFragment;
import com.har8yun.homeworks.projectx.model.Event;
import com.har8yun.homeworks.projectx.model.User;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.fragment.NavHostFragment;

import static com.har8yun.homeworks.projectx.fragment.MapFragment.START_POINT_MAP;
import static com.har8yun.homeworks.projectx.fragment.MapFragment.START_ZOOM;
import static com.har8yun.homeworks.projectx.util.NavigationHelper.onClickNavigate;
import static java.lang.String.valueOf;


public class LaunchFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "LaunchFragment";

    //views
    private Button mSignInButton;
    private Button mSignUpButton;
    private BottomNavigationView mBottomNavigationView;
    private MapView mapView;

    private GoogleMap mGoogleMap;

    //firebase
    DatabaseReference mFirebaseDatabse;

    //event
    List<Event> mEventList = new ArrayList<>();

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
        mGoogleMap = googleMap;

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        moveCamera(START_POINT_MAP,START_ZOOM,null);

        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), R.raw.map_style));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        setEventsOnMap();



        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for (final Event event : mEventList) {
                    if (marker.getPosition().latitude == event.getPosition().getLatitude()
                            && marker.getPosition().longitude == event.getPosition().getLongitude()) {
                        Dialog dialog = new Dialog(getContext());
                        dialog.setTitle(event.getTitle());
                        dialog.setContentView(R.layout.dialog_event_information);

                        //setting Dialog Views ----------------------------------------------
                        TextView titleView = dialog.findViewById(R.id.tv_title_dialog);
                        TextView descriptionView = dialog.findViewById(R.id.tv_description_dialog);
                        TextView dateLocationView = dialog.findViewById(R.id.tv_date_location_dialog);
                        TextView creatorView = dialog.findViewById(R.id.tv_creator_username_dialog);

                        Button goingButton = dialog.findViewById(R.id.btn_going_dialog);
                        Button cancelButton = dialog.findViewById(R.id.btn_cancel_dialog);

                        goingButton.setVisibility(View.INVISIBLE);

                        titleView.setText(event.getTitle());
                        descriptionView.setText(event.getDescription());
                        dateLocationView.setText(event.getDate().toGMTString());
                        creatorView.setText(event.getCreator().getUsername());

                        dialog.show();

                        cancelButton.setOnClickListener(v -> {
                            dialog.dismiss();
                        });
                    }
                }
                return true;
            }
        });



    }

    private void setEventsOnMap() {

        mFirebaseDatabse = FirebaseDatabase.getInstance().getReference("events");
        mFirebaseDatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mEventList.clear();

                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    mEventList.add(event);
                    LatLng latLng = new LatLng(event.getPosition().getLatitude(), event.getPosition().getLongitude());

                    MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                    mGoogleMap.addMarker(markerOptions);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }
}