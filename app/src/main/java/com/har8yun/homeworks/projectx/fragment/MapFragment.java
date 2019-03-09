package com.har8yun.homeworks.projectx.fragment;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.adapter.PlaceAutocompleteAdapter;
import com.har8yun.homeworks.projectx.model.Event;
import com.har8yun.homeworks.projectx.model.EventViewModel;
import com.har8yun.homeworks.projectx.model.Skill;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.preferences.SaveSharedPreferences;
import com.har8yun.homeworks.projectx.model.UserViewModel;
import com.har8yun.homeworks.projectx.util.PermissionChecker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.navigation.NavController;

import static com.google.android.gms.common.util.CollectionUtils.setOf;
import static com.har8yun.homeworks.projectx.util.NavigationHelper.onClickNavigate;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {


//    public static final String DATABASE_PATH_EVENTS = "Events";

    private static final String TAG = "MapFragment";
    public static final int PLACE_PICKER_REQUEST = 2;
    public static final int REQUEST_LOCATION_PERMISSION_CODE = 1;
    public static final String MY_LOCATION = "My Location";
    public static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(

            new LatLng(-40, -168), new LatLng(71, 136));

    String[] mPermissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};

    //views
    private BottomNavigationView mBottomNavigationView;
    private FloatingActionButton mAddEventButton;
    private AutoCompleteTextView mSearchView;
    private FloatingActionButton mLocationButton;
//    private FloatingActionButton mTaskButton;
    private Spinner mTaskSpinner;
    private MapView mapView;
    private SupportMapFragment mMapFragment;
    private ImageView mMagnifyView;
    private ImageView mPlacePicker;

    //Map, Location
    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private Location mDeviceLocation;

    //navigation
    private NavController mNavController;

    //preferences
    SaveSharedPreferences sharedPreferences = new SaveSharedPreferences();

    //user
    private User mCurrentUser;
    private UserViewModel mUserViewModel;

    //event
    private List<Event> mEventList = new ArrayList<>();
    private EventViewModel mEventViewModel;

    //Firebase
    DatabaseReference mDatabase;


    //constructor
    public MapFragment() {
    }


    //************************************ LIFECYCLE METHODS ****************************************
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // mUser = sharedPreferences.getCurrentUser(getContext());
        initViews(view);
        initSearch();
        showBotNavBar();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUserViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        mUserViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                Log.e("hhhh", "ViewModel " + user.toString());
            }
        });

        mEventViewModel = ViewModelProviders.of(getActivity()).get(EventViewModel.class);

    }
    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }



    //************************************** METHODS ********************************************

    private void showBotNavBar() {
        mBottomNavigationView.setVisibility(View.VISIBLE);
    }

    private void initViews(View v) {
        mBottomNavigationView = getActivity().findViewById(R.id.bottom_navigation_view_main);
        mAddEventButton = v.findViewById(R.id.fab_add_event_map);
        mLocationButton = v.findViewById(R.id.fab_my_location_map);
        mSearchView = v.findViewById(R.id.et_search_map_fragment);
        mMagnifyView = v.findViewById(R.id.iv_magnify_map_fragment);
        mTaskSpinner = v.findViewById(R.id.spinner_task_map);
        mPlacePicker = v.findViewById(R.id.iv_current_location_map);

        mapView = v.findViewById(R.id.mv_map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        mTaskSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String currentItemName = parent.getItemAtPosition(position).toString();

                if (position == 0) {
                    view.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        mGoogleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this.getActivity(), this)
                .build();

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(getContext(),mGoogleApiClient,LAT_LNG_BOUNDS,null);
        mSearchView.setAdapter(mPlaceAutocompleteAdapter);

//        mMapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.mv_map);
//        mMapFragment.getMapAsync(this);

        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PermissionChecker.hasLocationPermission(getContext()))
                {
                    getDeviceLocation();
                }
                else {
                   requestLocationPermissions();
                   PermissionChecker.createLocationRequest(); //TODO
                }
            }
        });

        mPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startPicking(builder.build(getActivity()),PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });


        onClickNavigate(mAddEventButton, R.id.action_map_fragment_to_create_event_fragment);
//        mTaskButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new AlertDialog.Builder(getContext())
//                        .setTitle("Title")
//                        .setMessage("Message")
//                        .setPositiveButton("Ok", null)
//                        .show();
//            }
//        });


    }

    private void startPicking(Intent intent,int code)
    {

    }

    protected void requestLocationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(mPermissions,REQUEST_LOCATION_PERMISSION_CODE);
        }
    }


    private void getDeviceLocation()
    {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        try {
            Task mTaskLocation = mFusedLocationProviderClient.getLastLocation();
            mTaskLocation.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful() && task.getResult() !=null)
                    {
                        Toast.makeText(getContext(),"Location Found",Toast.LENGTH_SHORT).show();
                        mDeviceLocation = (Location) task.getResult();
                        moveCamera(new LatLng(mDeviceLocation.getLatitude(),mDeviceLocation.getLongitude()),DEFAULT_ZOOM,MY_LOCATION);

                    }
                    else{
                        Log.d("Map",task.getException().getMessage());
                        Toast.makeText(getContext(),"Location Not Found",Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }catch (SecurityException e)
        {
            Log.d("Map",e.getMessage());
        }

    }



    private void moveCamera(LatLng latLng,float zoom,String title)
    {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

        MarkerOptions mMarkerOptions = new MarkerOptions()
                .position(latLng)
                .title(title);
        mGoogleMap.addMarker(mMarkerOptions);
    }
    private void initSearch()
    {
        Log.d("Map","initSearch");
        mSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_DONE
                || event.getAction() == KeyEvent.ACTION_DOWN
                || event.getAction() == KeyEvent.KEYCODE_ENTER)
                {
                    Log.d("Map","geoLocate MAP");
                    geoLocate();
                }
                return false;
            }
        });
        mMagnifyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geoLocate();
            }
        });
    }

    private void geoLocate()
    {
        Log.d("Map","geoLocate");
        String searchString  = mSearchView.getText().toString();
        Geocoder mGeocoder = new Geocoder(getContext());
        List<Address> mAddressList = new ArrayList<>();
        try {
            mAddressList = mGeocoder.getFromLocationName(searchString,1);

        }catch (IOException e)
        {
            Log.d("MAP","IOException " + e.getMessage());
        }
        if(mAddressList.size() > 0)
        {
            Address address = mAddressList.get(0);
            Log.d("Map","address " + address.toString());
            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM,address.getAddressLine(0));
        }

    }
    


    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;

        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.3092293, -122.1136845))
                .title("Captain America"));

        //googleMap.addMarker()

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void addEvent()   //this method sets event  TODO: call this method when event is created
    {
        mEventList.add(mEventViewModel.getEvent().getValue());
    }

    @Override
    public void onDestroy() {
        mDatabase = FirebaseDatabase.getInstance().getReference("events");
        super.onDestroy();
        for (Event event : mEventList) {
            event.setUid(mDatabase.push().getKey());
            mDatabase.child(event.getUid()).setValue(event);

        }
    }
}
