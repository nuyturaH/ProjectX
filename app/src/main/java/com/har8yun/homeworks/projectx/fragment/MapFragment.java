package com.har8yun.homeworks.projectx.fragment;

import android.Manifest;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.content.pm.PackageManager;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.model.TravelMode;
import com.har8yun.homeworks.projectx.R;
import com.har8yun.homeworks.projectx.adapter.PlaceAutocompleteAdapter;
//import com.har8yun.homeworks.projectx.directionhelpers.FetchURL;
//import com.har8yun.homeworks.projectx.directionhelpers.TaskLoadedCallback;

import com.har8yun.homeworks.projectx.fragment.Tasks.TasksFragment;
import com.har8yun.homeworks.projectx.fragment.event.CreateEventFragment;
import com.har8yun.homeworks.projectx.mapAnim.MapAnimator;
import com.har8yun.homeworks.projectx.mapHelper.TaskLoadedCallback;
import com.har8yun.homeworks.projectx.model.Event;
import com.har8yun.homeworks.projectx.model.EventViewModel;
import com.har8yun.homeworks.projectx.model.TaskViewModel;
import com.har8yun.homeworks.projectx.model.MyLatLng;
import com.har8yun.homeworks.projectx.model.User;
import com.har8yun.homeworks.projectx.preferences.SaveSharedPreferences;
import com.har8yun.homeworks.projectx.model.UserViewModel;
import com.har8yun.homeworks.projectx.util.PermissionChecker;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;


import static com.har8yun.homeworks.projectx.fragment.DoneDialogFragment.LOOSE_WEIGHT_DONE;
import static com.har8yun.homeworks.projectx.fragment.Tasks.TaskInfoFragment.BUILD_MUSCLES;
import static com.har8yun.homeworks.projectx.fragment.Tasks.TaskInfoFragment.LOOSE_WEIGHT;
import static com.har8yun.homeworks.projectx.util.NavigationHelper.onClickNavigate;
import static java.lang.String.*;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        TaskLoadedCallback, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener {


//    public static final String DATABASE_PATH_EVENTS = "Events";

    private static final String TAG = "MapFragment";
    public static final int PLACE_PICKER_REQUEST = 2;
    public static final int REQUEST_LOCATION_PERMISSION_CODE = 1234;
    public static final String MY_LOCATION = "My Location";
    public static final float DEFAULT_ZOOM = 15f;
    public static final float START_ZOOM = 10f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(

            new LatLng(-40, -168), new LatLng(71, 136));
    public static final LatLng START_POINT_MAP = new LatLng(40.183414, 44.514807);


    String[] mPermissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    //views
    private ConstraintLayout mMainLayout;
    private BottomNavigationView mBottomNavigationView;
    private FloatingActionButton mAddEventButton;
    private AutoCompleteTextView mSearchView;
    private FloatingActionButton mLocationButton;
    private FloatingActionButton mTasksButton;
    private ConstraintLayout mSearchLayout;
    private ConstraintLayout mTaskInfoLayout;
    private MapView mapView;
    private ImageView mMagnifyView;
    private ImageView mCurrentLocationView;
    private Button mExitButton;
    private Button mDoneButton;
    private TextView mDistanceView;
    private TextView mCaloriesView;

    ConstraintSet constraintSet = new ConstraintSet();


    //tasks
    private TaskViewModel mTaskViewModel;

    //Map, Location
    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private Location mDeviceLocation;
    private Polyline currentPolyline;
    private Place mPlace;
    private Marker mEventMarker;
    private List<Marker> mMarkerList = new ArrayList<>();
    LatLng destinationPosition;
    List<LatLng> path;

    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;

    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;


    //navigation
    private Fragment mNavHostFragment;

    //preferences
    SaveSharedPreferences sharedPreferences = new SaveSharedPreferences();

    //user
    private List<User> mUserList = new ArrayList<>();
    private User mCurrentUser;
    private UserViewModel mUserViewModel;

    //event
    private List<Event> mEventList = new ArrayList<>();
    private Event mCurrentEvent;
    private EventViewModel mEventViewModel;


    //Firebase
    DatabaseReference mFirebaseDatabse;
    DatabaseReference mFirebaseDatabseUser;


    //constructor
    public MapFragment() {
    }


    //************************************ LIFECYCLE METHODS ****************************************
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        if(mTaskViewModel!=null)
        Log.e("hhhh", "Task ViewModel "+ mTaskViewModel.getTask().getValue());

        // mUser = sharedPreferences.getCurrentUser(getContext());
        initViews(view);
        initSearch();
        showBotNavBar();
        constraintSet.clone(getActivity(), R.layout.fragment_map);
        //getDeviceLocation();

        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO add points

                FragmentManager fm = getActivity().getSupportFragmentManager();
                DoneDialogFragment tasksFragment = new DoneDialogFragment();
                tasksFragment.show(fm, null);
            }
        });


        Toast.makeText(getContext(), "Choose Location for your Event", Toast.LENGTH_LONG).show();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDeviceLocation();
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mUserViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        mUserViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                mCurrentUser = user;
                Log.e("hhhh", "ViewModel " + user.toString());
            }
        });
        mCurrentUser = mUserViewModel.getUser().getValue();

        mEventViewModel = ViewModelProviders.of(getActivity()).get(EventViewModel.class);
        mEventViewModel.getEvent().observe(getViewLifecycleOwner(), new Observer<Event>() {
            @Override
            public void onChanged(@Nullable final Event event) {
                mCurrentEvent = event;
            }
        });
        mCurrentEvent = mEventViewModel.getEvent().getValue();



    }

    @Override
    public void onResume() {
        super.onResume();
        mTaskViewModel = ViewModelProviders.of(getActivity()).get(TaskViewModel.class);
        mTaskViewModel.getTask().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s != null) {
                    switch (s) {
                        case LOOSE_WEIGHT:
                            if (mDeviceLocation != null) {
                                mGoogleMap.clear();
                                changeMapDesign();
                                looseWeight();
                                startUserLocationsRunnable();
                                MapAnimator.getInstance().setPrimaryLineColor(getResources().getColor(R.color.colorPrimary));
                                MapAnimator.getInstance().setSecondaryLineColor(getResources().getColor(R.color.colorPrimaryLight));
                                MapAnimator.getInstance().animateRoute(mGoogleMap, path);
                                mGoogleMap.addMarker(new MarkerOptions().position(destinationPosition).title("Destination"));
                                break;
                            }
                        case LOOSE_WEIGHT_DONE:
                            mGoogleApiClient.stopAutoManage(getActivity());
                            mGoogleApiClient.disconnect();
                            NavHostFragment.findNavController(mNavHostFragment).navigate(R.id.action_global_map_fragment);
                            mTaskViewModel.setTask(null);
                            stopLocationUpdates();

                            mTaskViewModel.setTask(null);
                            break;
                    }
                }
            }
        });

    }


    @Override
    public void onPause() {
        super.onPause();

        if (mFusedLocationProviderClient != null) {
            mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }
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
        mMainLayout = v.findViewById(R.id.layout_main_map);
        mLocationButton = v.findViewById(R.id.fab_my_location_map);
        mSearchView = v.findViewById(R.id.et_search_map_fragment);
        mMagnifyView = v.findViewById(R.id.iv_magnify_map_fragment);
        mTasksButton = v.findViewById(R.id.fab_tasks_map);
        mExitButton = v.findViewById(R.id.btn_exit_map);
        mDoneButton = v.findViewById(R.id.btn_done_map);
        mSearchLayout = v.findViewById(R.id.layout_search_map);
        mTaskInfoLayout = v.findViewById(R.id.layout_task_info_map);
        mCurrentLocationView = v.findViewById(R.id.iv_current_location_map);
        mDistanceView = v.findViewById(R.id.tv_distance_map);
        mCaloriesView = v.findViewById(R.id.tv_calories_map);
        mNavHostFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);


        mapView = v.findViewById(R.id.mv_map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        onClickNavigate(mTasksButton, R.id.action_map_fragment_to_tasks_fragment);
        mTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                TasksFragment tasksFragment = new TasksFragment();
                tasksFragment.show(fm, null);
            }
        });

        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleApiClient.stopAutoManage(getActivity());
                mGoogleApiClient.disconnect();
                NavHostFragment.findNavController(mNavHostFragment).navigate(R.id.action_global_map_fragment);
                mTaskViewModel.setTask(null);
                stopLocationUpdates();
            }
        });


        mGoogleApiClient = new GoogleApiClient
                .Builder(getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this.getActivity(), this)
                .build();

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(getContext(), mGoogleApiClient, LAT_LNG_BOUNDS, null);
        mSearchView.setAdapter(mPlaceAutocompleteAdapter);


        mLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionChecker.hasLocationPermission(getContext())) {
                    getDeviceLocation();
                    moveCamera(new LatLng(mDeviceLocation.getLatitude(), mDeviceLocation.getLongitude()), DEFAULT_ZOOM, MY_LOCATION);
                } else {
                    requestLocationPermissions();
                    checkLocationPermission();
                    Log.d(TAG, "onClick: PERFSDFSDFSDFSDFSDFSDFS");
                }
            }
        });


//        mPlacePicker.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//
//                try {
//                    startPicking(builder.build(getActivity()));
//                } catch (GooglePlayServicesRepairableException e) {
//                    e.printStackTrace();
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    e.printStackTrace();
//                }
//            }
//        });


//        onClickNavigate(mAddEventButton, R.id.action_map_fragment_to_create_event_fragment);
        mAddEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MarkerOptions markerOptions = new MarkerOptions().position(mGoogleMap.getCameraPosition().target);
                mEventMarker = mGoogleMap.addMarker(markerOptions);
                mMarkerList.add(mEventMarker);
                Event event = new Event();
                MyLatLng myLatLng = new MyLatLng(mEventMarker.getPosition().latitude, mEventMarker.getPosition().longitude);
                event.setPosition(myLatLng);
                event.setPlace(getAddress(myLatLng.getLatitude(),myLatLng.getLongitude()));
                mEventViewModel.setEvent(event);
                mEventViewModel.setToEdit(false);

                NavHostFragment.findNavController(mNavHostFragment).navigate(R.id.action_map_fragment_to_create_event_fragment);
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
                    Event mEvent = eventSnapshot.getValue(Event.class);
                    Log.d(TAG, "onDataChange: " + mEvent.getPosition());
                    mEventList.add(mEvent);
                    LatLng latLng = new LatLng(mEvent.getPosition().getLatitude(), mEvent.getPosition().getLongitude());

                    MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                    mGoogleMap.addMarker(markerOptions);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        Toast.makeText(getContext(), valueOf(mEventList.size()), Toast.LENGTH_LONG).show();

    }


    protected void requestLocationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(mPermissions, REQUEST_LOCATION_PERMISSION_CODE);
        }
//        ActivityCompat.requestPermissions(this.getActivity(),
//                mPermissions,
//                REQUEST_LOCATION_PERMISSION_CODE);
        Log.d(TAG, "requestLocationPermissions: BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");

    }


    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        try {
            Task mTaskLocation = mFusedLocationProviderClient.getLastLocation();
            mTaskLocation.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful() && task.getResult() != null) {
//                        Toast.makeText(getContext(), "Location Found", Toast.LENGTH_SHORT).show();
                        mDeviceLocation = (Location) task.getResult();
//                        moveCamera(new LatLng(mDeviceLocation.getLatitude(), mDeviceLocation.getLongitude()), DEFAULT_ZOOM, MY_LOCATION);

                    } else {
//                        Log.d("Map", task.getException().getMessage());
                        Toast.makeText(getContext(), "Location Not Found", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        } catch (SecurityException e) {
            Log.d("Map", e.getMessage());
        }

    }


    private void moveCamera(LatLng latLng, float zoom, String title) {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void initSearch() {
        Log.d("Map", "initSearch");
        mSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    Log.d("Map", "geoLocate MAP");
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

    private void geoLocate() {
        Log.d("Map", "geoLocate");
        String searchString = mSearchView.getText().toString();
        Geocoder mGeocoder = new Geocoder(getContext());
        List<Address> mAddressList = new ArrayList<>();
        try {
            mAddressList = mGeocoder.getFromLocationName(searchString, 1);
//            mGeocoder.get
            mAddressList = mGeocoder.getFromLocationName(searchString, 1);

        } catch (IOException e) {
            Log.d("MAP", "IOException " + e.getMessage());
        }
        if (mAddressList.size() > 0) {
            Address address = mAddressList.get(0);
            Log.d("Map", "address " + address.toString());
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
        }

    }

    public String getAddress(double lat, double lng) {
        String add="";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            add = obj.getAddressLine(0);
//            add = add + "\n" + obj.getCountryName();
//            add = add + "\n" + obj.getCountryCode();
//            add = add + "\n" + obj.getAdminArea();
//            add = add + "\n" + obj.getPostalCode();
//            add = add + "\n" + obj.getSubAdminArea();
//            add = add + "\n" + obj.getLocality();
//            add = add + "\n" + obj.getSubThoroughfare();
            Log.e(TAG, "getAddress: "+add );
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return add;
    }


    private boolean goingToEvent;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;

        mGoogleMap.setOnCameraIdleListener(this);
        mGoogleMap.setOnCameraMoveListener(this);

        moveCamera(START_POINT_MAP,START_ZOOM,null);

        setSettings();


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }


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
                        ImageView editView = dialog.findViewById(R.id.iv_to_change_event_dialog);
                        Button goingButton = dialog.findViewById(R.id.btn_going_dialog);
                        Button cancelButton = dialog.findViewById(R.id.btn_cancel_dialog);

                        titleView.setText(event.getTitle());
                        descriptionView.setText(event.getDescription());
                        dateLocationView.setText("AAAA");
                        creatorView.setText(event.getCreator().getUsername());

                        //checking if current user is the creator of event ------------------------------------------
                        if (event.getCreator().getId().equals(mCurrentUser.getId())) {
                            editView.setVisibility(View.VISIBLE);
                            editView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mEventViewModel.setEvent(event);
                                    mEventViewModel.setToEdit(true);
                                    dialog.dismiss();
                                    NavHostFragment.findNavController(mNavHostFragment).navigate(R.id.action_map_fragment_to_create_event_fragment);
                                }
                            });
                        }

                        //checking if current user is going to current event -------------------------------------
                        mFirebaseDatabseUser = FirebaseDatabase.getInstance().getReference("users");
                        mFirebaseDatabseUser.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                mUserList.clear();
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    User user = userSnapshot.getValue(User.class);
                                    mUserList.add(user);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });

                        for (String id : event.getParticipants())
                        {
                            if (id.equals(mCurrentUser.getId())) {
                                goingToEvent = true;
                                goingButton.setText("Not Going");
                            }
                        }

                        dialog.show();
                        CreateEventFragment cef = new CreateEventFragment();

                        goingButton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                goingToEvent = !goingToEvent;
                                if (goingToEvent) {
                                    goingButton.setText("Not Going");
                                    event.getParticipants().add(mCurrentUser.getId());

                                    cef.updateEventInFirebase(event);
                                } else {
                                    goingButton.setText("Going");
                                    event.getParticipants().remove(mCurrentUser.getId());
                                    cef.updateEventInFirebase(event);
                                }

                            }
                        });
                        cancelButton.setOnClickListener(v -> {
                            goingToEvent = false;
                            dialog.dismiss();
                        });
                    }
                }
                return true;
            }
        });

    }

    private void setSettings()
    {
        mGoogleMap.getUiSettings().setZoomControlsEnabled(sharedPreferences.getZoom(getContext()));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }


    private LatLng getDestinationPoint(LatLng source, double brng, double dist) {
        dist = dist / 6371;
        brng = Math.toRadians(brng);

        double lat1 = Math.toRadians(source.latitude), lon1 = Math.toRadians(source.longitude);
        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(dist) +
                Math.cos(lat1) * Math.sin(dist) * Math.cos(brng));
        double lon2 = lon1 + Math.atan2(Math.sin(brng) * Math.sin(dist) *
                        Math.cos(lat1),
                Math.cos(dist) - Math.sin(lat1) *
                        Math.sin(lat2));
        if (Double.isNaN(lat2) || Double.isNaN(lon2)) {
            return null;
        }
        return new LatLng(Math.toDegrees(lat2), Math.toDegrees(lon2));
    }

    private void looseWeight() {
        double angle = ThreadLocalRandom.current().nextDouble(0, 360);
        LatLng sourcePosition = new LatLng(mDeviceLocation.getLatitude(), mDeviceLocation.getLongitude());
        String origin = mDeviceLocation.getLatitude() + "," + mDeviceLocation.getLongitude();

        destinationPosition = getDestinationPoint(sourcePosition, angle, 1);
        String destination = destinationPosition.latitude + "," + destinationPosition.longitude;

        mGoogleMap.addMarker(new MarkerOptions().position(sourcePosition).title("Origin")
//                .icon(convertToBitmap(getResources().getDrawable(R.drawable.map_marker_radius), 130, 130)));
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

//        mGoogleMap.addMarker(new MarkerOptions().position(destinationPosition).title("Destination"));

        //Define list to get all latlng for the route
        path = new ArrayList();

        //Execute Directions API request
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(getResources().getString(R.string.google_maps_key))
                .build();
        DirectionsApiRequest req = DirectionsApi.getDirections(context, origin, destination);
        try {
            DirectionsResult res = req.mode(TravelMode.WALKING).await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs != null) {
                    for (int i = 0; i < route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j = 0; j < leg.steps.length; j++) {
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length > 0) {
                                    for (int k = 0; k < step.steps.length; k++) {
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getLocalizedMessage());
        }

    }

    public static void setAnimation(GoogleMap myMap, final List<LatLng> directionPoint, final Bitmap bitmap) {
        Marker marker = myMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                .position(directionPoint.get(0))
                .flat(true));

        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(directionPoint.get(0), DEFAULT_ZOOM));
        animateMarker(myMap, marker, directionPoint, false);
    }

    private static void animateMarker(GoogleMap myMap, final Marker marker, final List<LatLng> directionPoint,
                                      final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = myMap.getProjection();
        final long duration = 30000;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            int i = 0;

            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                if (i < directionPoint.size())
                    marker.setPosition(directionPoint.get(i));
                i++;

                if (t < 1.0) {
                    handler.postDelayed(this, 15);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    public BitmapDescriptor convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(mutableBitmap);
    }

    public double calculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }

    private void changeMapDesign() {
        if (mSearchLayout.getVisibility() == View.VISIBLE) {

            mSearchLayout.setVisibility(View.INVISIBLE);
            mTasksButton.hide();
            mAddEventButton.hide();
            mBottomNavigationView.setVisibility(View.GONE);
            mExitButton.setVisibility(View.VISIBLE);
            mDoneButton.setVisibility(View.VISIBLE);
            mCurrentLocationView.setVisibility(View.GONE);
            mTaskInfoLayout.setVisibility(View.VISIBLE);

        } else {
            mSearchLayout.setVisibility(View.VISIBLE);
            mTasksButton.show();
            mAddEventButton.show();
            mBottomNavigationView.setVisibility(View.VISIBLE);
            mExitButton.setVisibility(View.GONE);
            mDoneButton.setVisibility(View.GONE);
            mCurrentLocationView.setVisibility(View.VISIBLE);
            mTaskInfoLayout.setVisibility(View.GONE);
        }
    }


    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                Log.e("hhhh", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }

                //Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Position");
//                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

                //move map camera
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
            }
        }
    };


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user'mPastDistance response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getContext())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mGoogleMap.setMyLocationEnabled(true);
                        Log.e("hhhh", "changing");
                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getContext(), "permission denied", Toast.LENGTH_LONG).show();
                }
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    private static final int LOCATION_UPDATE_INTERVAL = 3000;
    double mPastDistance;
    double mStep;
    LatLng mPreviousStep;

    private void startUserLocationsRunnable() {
        mHandler.postDelayed(mRunnable = new Runnable() {
            @Override
            public void run() {
                getDeviceLocation();
                double d = calculationByDistance(new LatLng(mDeviceLocation.getLatitude(), mDeviceLocation.getLongitude()), destinationPosition);
                if (mPreviousStep != null) {
                    if (mDeviceLocation.getLatitude() != mPreviousStep.latitude || mDeviceLocation.getLongitude() != mPreviousStep.longitude) {
                        mStep = calculationByDistance(new LatLng(mDeviceLocation.getLatitude(), mDeviceLocation.getLongitude()), mPreviousStep);
                        mPastDistance = mPastDistance + mStep * 1000;
                        int burnedCalories = (int)(mPastDistance * 0.055);
                        mCaloriesView.setText(" • " + String.valueOf(burnedCalories) + " calories are burned");
                    }
                }
                mPreviousStep = new LatLng(mDeviceLocation.getLatitude(), mDeviceLocation.getLongitude());
                DecimalFormat df2 = new DecimalFormat("#.##");
                mDistanceView.setText(" • " + df2.format(d) + " km to destination");
                mHandler.postDelayed(mRunnable, LOCATION_UPDATE_INTERVAL);
            }
        }, LOCATION_UPDATE_INTERVAL);
    }

    private void stopLocationUpdates() {
        mHandler.removeCallbacks(mRunnable);
    }


    @Override
    public void onCameraMove() {
        if (mTaskViewModel.getTask().getValue() == null) {
            Log.e("hhhh", " " +mTaskViewModel.getTask().getValue());
            float biasedValue = 0.435f;
            constraintSet.setVerticalBias(mCurrentLocationView.getId(), biasedValue);
            constraintSet.applyTo(mMainLayout);
        }
    }


    @Override
    public void onCameraIdle() {
        if (mTaskViewModel.getTask().getValue() == null) {
            Log.e("hhhh", " "+ mTaskViewModel.getTask().getValue());

            float biasedValue = 0.45f;
            constraintSet.setVerticalBias(mCurrentLocationView.getId(), biasedValue);
            constraintSet.applyTo(mMainLayout);
        }

    }


    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null) {
            currentPolyline.remove();
        }
        currentPolyline = mGoogleMap.addPolyline((PolylineOptions) values[0]);
    }
}
